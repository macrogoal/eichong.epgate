package com.ec.epcore.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ec.constants.EpConstants;
import com.ec.constants.ErrorCodeConstants;
import com.ec.constants.UserConstants;
import com.ec.epcore.cache.BespCache;
import com.ec.epcore.cache.ElectricPileCache;
import com.ec.epcore.cache.EpGunCache;
import com.ec.epcore.cache.RateInfoCache;
import com.ec.epcore.cache.UserCache;
import com.ec.epcore.cache.UserOrigin;
import com.ec.epcore.cache.UserRealInfo;
import com.ec.epcore.config.GameConfig;
import com.ec.epcore.net.client.EpCommClient;
import com.ec.epcore.net.codec.EpEncoder;
import com.ec.epcore.net.proto.EpBespResp;
import com.ec.epcore.net.proto.EpCancelBespResp;
import com.ec.epcore.sender.EpMessageSender;
import com.ec.net.proto.Iec104Constant;
import com.ec.netcore.client.ITcpClient;
import com.ec.utils.StringUtil;
import com.ormcore.dao.DB;
import com.ormcore.model.TblBespoke;
	
public class EpBespokeService {

	private static final Logger logger = LoggerFactory
			.getLogger(EpBespokeService.class);

	public static TblBespoke getUnStopBespokeFromDb(int pkEpId, int pkEpGunNo) {
		TblBespoke besp = new TblBespoke();
		besp.setPkEpId(pkEpId);
		besp.setPkEpGunNo(pkEpGunNo);
		TblBespoke dbBesp = null;
		List<TblBespoke> bespList = DB.bespDao.getUnStopBesp(besp);
		if (bespList != null && bespList.size() > 0) {
			dbBesp = bespList.get(0);
		}
		return dbBesp;

	}
	
	public static void getUnStopBespokeByUserIdFromDb(UserCache u) {
		
		TblBespoke besp = new TblBespoke();
		besp.setUserId(u.getId());
		
		
		List<TblBespoke> bespList = DB.bespDao.getUnStopBespByUserId(besp);
		if (bespList == null ) 
			return ;
	
		int size = bespList.size() ;
		if(size<=0)
			return ;
		
		for(int i=0;i<size;i++)
		{
			TblBespoke dbBesp = bespList.get(i);
			BespCache tmpBespCache = EpBespokeService.makeBespokeCache(dbBesp);
			
			// 检查是否过期,如果过期.那么结算
			long diff  = EpBespokeService.expireTime(tmpBespCache);
		
			if (diff > 0) {
				//结算
				Date now = new Date();
				tmpBespCache.setRealEndTime(now.getTime()/1000);
				
				if(tmpBespCache.getRealEndTime() > tmpBespCache.getEndTime())
				{
					tmpBespCache.setRealEndTime(tmpBespCache.getEndTime());
				}
				logger.debug("init,initConnect,epCode:{},besp != null, diff > 0,epGunNo:{}",
						tmpBespCache.getEpCode(),tmpBespCache.getEpGunNo());
				BigDecimal realBespAmt = EpBespokeService.statBespoke(tmpBespCache);
				EpBespokeService.endBespoke(tmpBespCache.getEpCode(), realBespAmt, tmpBespCache, now);
			
				tmpBespCache=null;
			} 
			else 
			{
				tmpBespCache.setStatus(EpConstants.BESPOKE_STATUS_LOCK);
				u.addBesp(tmpBespCache);
			}
		}
	}

	public static BespCache makeBespokeCache(TblBespoke besp) {
		BespCache bespokeCache = new BespCache();
		
		String Account = UserService.getUserCache(besp.getUserid()).getAccount();

		bespokeCache.setAccount(Account);
		bespokeCache.setAccountId(besp.getUserid());

		bespokeCache.setBespNo(besp.getBespNo());
		bespokeCache.setBespId(besp.getId());
		
		long st = besp.getBeginTime().getTime() / 1000;
		long et = besp.getEndTime().getTime() / 1000;
		long realet = besp.getRealityTime().getTime() / 1000;
		bespokeCache.setStartTime(st);
		bespokeCache.setEndTime(et);
		bespokeCache.setRealEndTime(realet);
		BigDecimal fronzeAmt= besp.getAmt();
		fronzeAmt = fronzeAmt.setScale(2,BigDecimal.ROUND_HALF_UP);
		bespokeCache.setFronzeAmt(fronzeAmt);
	
		bespokeCache.setRate(besp.getPrice());
		bespokeCache.setPayMode(besp.getPayMode());
		UserOrigin userOrigin = new UserOrigin(besp.getUserOrgNo(),1,besp.getPartnerIdentiy());
		bespokeCache.setUserOrigin(userOrigin);
		return bespokeCache;
	}

	
	public static int apiStopBespoke(long pkBespNo, String bespNo,
			String epCode, int epGunNo,int cmdFromSource,String srcIdentity) {
		
		if (bespNo.length() != 12) {
			return ErrorCodeConstants.INVALID_BESP_NO_LEN;//
		}
		
		java.util.Date dt = new Date();
		long now = dt.getTime() / 1000;
		
		EpGunCache gunCache = EpGunService.getEpGunCache(epCode, epGunNo);
		
		if(gunCache == null)
			return ErrorCodeConstants.EP_UNCONNECTED;

		ITcpClient commClient = gunCache.getEpNetObject();
		if(commClient==null || !commClient.isComm())
			return ErrorCodeConstants.EP_UNCONNECTED;
		
		BespCache besp = gunCache.getBespCache();
		
		if(besp==null)//没有预约
		{
			return ErrorCodeConstants.CAN_NOT_STOP_BESPOKE;// 
		}
		if(bespNo.compareTo(besp.getBespNo())!=0)//没有同编号的预约
		{
			return ErrorCodeConstants.NOT_SELF_REDO_BESP;//
		}
		
		besp.getUserOrigin().setCmdFromSource(cmdFromSource);
		
		gunCache.stopBespokeAction(cmdFromSource,srcIdentity,bespNo,besp.getAccountId());
	
		return 0;

	}

	public static int apiBespoke(String epCode, int epGunNo, int pkEpId,
			String bespNo, Short buyOutTime, long clientBespSt, int redo,
			int userid, String accountNo, long pkEpGunNo,
			int payMode,int orgNo,int cmdFromSource,String cmdIdentily) {
		
		if (redo != 0 && redo != 1) {
			return ErrorCodeConstants.INVALID_REDO_FLAG;//
		}
		// 3.预约参数不对，不能预约
		if (bespNo.length() != 12) {
			return ErrorCodeConstants.INVALID_BESP_NO_LEN;//
		}
		
		
		// 1.预约买断时间不对,不能预约
		int secBuyOutTime = buyOutTime * 60;
		if (secBuyOutTime < GameConfig.minBespTimeUnit
				|| secBuyOutTime > (6 * 3600)) {
			return ErrorCodeConstants.INVALID_BUY_TIMES;//
		}
		//2.用户能不能充预约
		UserCache memUserInfo= UserService.getUserCache(accountNo);
		
		String epBespGun = epCode + epGunNo;
		if(orgNo == UserConstants.ORG_I_CHARGE)
		{
			//自己的用户需要校验用户的状态,因为用户可能被冻结
			UserRealInfo userRealInfo= UserService.findUserRealInfo(accountNo);
			if(null==userRealInfo)
			{
				return ErrorCodeConstants.INVALID_ACCOUNT;
			}
			if(userRealInfo.getStatus() == 3)//用户被删除
			{
				return ErrorCodeConstants.INVALID_ACCOUNT;
			}
			
			if(userRealInfo.getStatus() == 2)//用户被冻结
			{
				return ErrorCodeConstants.INVALID_ACCOUNT_STATUS;
			}
			
			int errorCode = memUserInfo.canBespoke(epBespGun);
			if(errorCode>0)
				return errorCode;

		}
		
		//3.电桩能不能预约
		ElectricPileCache epCache =  EpService.getEpByCode(epCode);
		if(epCache == null)
		{
			logger.info("dont find ElectricPileCache,epCode:{}",epCode);
			return ErrorCodeConstants.EP_UNCONNECTED;
		}
		int rateInfoId = epCache.getRateid();
		
		RateInfoCache rateInfo= RateService.getRateById(rateInfoId);
		if(rateInfo == null)
		{
			return ErrorCodeConstants.INVALID_RATE;
		}
		
		int error = epCache.canBespoke(true);
		if(error > 0)
		{
			return error;
		}
		if(epGunNo<1|| epGunNo> epCache.getGunNum())
		{
			return ErrorCodeConstants.INVALID_EP_GUN_NO;//
		}
		//4.枪能不能预约
		EpGunCache epGunCache = EpGunService.getEpGunCache(epCode, epGunNo);
		if(epGunCache == null)
		{
			logger.info("dont find EpGunCache,epCode:{},epGunNo:{}",epCode, epGunNo);
			return ErrorCodeConstants.INVALID_EP_GUN_NO;//
		}
		error =0;
		if(redo ==0){
			error = epGunCache.canBespoke(userid);
		}
		else{
			error = epGunCache.canRedoBespoke(userid,bespNo);
		}
		if(error > 0)
		{
			return error;
		}
		
		int errorCode = epGunCache.startBespokeAction(memUserInfo,rateInfo,redo,secBuyOutTime,bespNo,
				payMode,orgNo,cmdFromSource,cmdIdentily);
		
		if(errorCode==0 && redo==0)
		{
			epGunCache.setAuthCache(null);
		}
		
		
		return errorCode;
	}

	public static TblBespoke getBespFromDb(long bespId) {
		TblBespoke dbBespoke = null;
		List<TblBespoke> bespList = DB.bespDao.getBesp(bespId);
		if (bespList != null && bespList.size() > 0) {
			if (bespList.size() > 1) {
				logger.error("getBespFromDb bespId:{},have  {}" ,bespId ,bespList.size());
			}

			dbBespoke = bespList.get(0);
		}
		return dbBespoke;
	}

	public static BespCache modifyRealEndTime(BespCache bespCacheObj, long time) {
		if (time < bespCacheObj.getRealEndTime()) {
			bespCacheObj.setRealEndTime(time);
		}
		return bespCacheObj;
	}
	
	public static void endBespoke(String epCode,BigDecimal realBespAmt,BespCache bespCache,Date et)
	{
		int bespStatus = bespCache.getStatus();
		if(bespStatus == EpConstants.BESPOKE_STATUS_END || bespStatus == EpConstants.BESPOKE_STATUS_END)
		{
			logger.info("endbespoke had handle!:{}",bespCache);
			return ;
		}
		int payMode = bespCache.getPayMode();
			
		int accountId= bespCache.getAccountId();
		logger.info("endbespoke accountId:{},payMode:{},realBespAmt:{},fronzeAmt:{}",new Object[]{accountId,payMode,realBespAmt,bespCache.getFronzeAmt()});
		if(payMode == EpConstants.P_M_FIRST)
		{
			bespCache.setStatus(EpConstants.BESPOKE_STATUS_END);
			
			BigDecimal fronzeAmt = bespCache.getFronzeAmt();
			
			BigDecimal addingAmt = fronzeAmt.subtract(realBespAmt);
			addingAmt =  addingAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
			UserService.addAmt(accountId,addingAmt,new BigDecimal(0),bespCache.getBespNo());
		}
		else
		{
			bespCache.setStatus(EpConstants.BESPOKE_STATUS_FIN_UNSTAT);
		}
		bespStatus = bespCache.getStatus();
		
		saveEndBespokeToDb(bespCache.getBespId(),bespCache,realBespAmt,bespStatus);
		
		// 3.增加消费记录,有预冻结金额的才写消费记录
		if(payMode == EpConstants.P_M_FIRST)
		{
			RateService.addPurchaseHistoryToDB(realBespAmt,2,accountId,bespCache.getUserOrigin().getOrgNo(),"预约消费",epCode,"",bespCache.getBespNo(),0);
		}
	}
	
	
	
	public static boolean isCoolEnd(BespCache bespokeCache) {
		java.util.Date dt = new Date();
		long now = dt.getTime() / 1000;
		int ExpireTime = (int) (now - (bespokeCache.getRealEndTime() + EpConstants.BESP_COOLING_TIME));
		if (ExpireTime >= 0)// 冷却时间到之后移除掉
		{
			return true;
		}
		return false;

	}

	public static BespCache cleanRenewBespokeInfo(BespCache bespokeCache) {
		return bespokeCache;
	}

	public static long expireCoolTime(BespCache bespokeCache) {
		java.util.Date dt = new Date();
		long now = dt.getTime() / 1000;

		long expireTime = now - bespokeCache.getRealEndTime();

		return expireTime;
	}

	public static long expireTime(BespCache bespokeCache) {
		java.util.Date dt = new Date();
		long now = dt.getTime() / 1000;
	

		long expireTime = now - bespokeCache.getEndTime();

		return expireTime;
	}
	
	public static void handleEpBespRet(EpCommClient epCommClient, EpBespResp bespResp,byte []time)
	{
		logger.debug("bespoke,handleEpBespRet {}",bespResp);
		
		String epCode = null;
		int epGunNo=0;
		String bespNo = null;
		
		//1.检查预约参数
		int retCode=checkBespRespParams(bespResp);
		if(retCode ==0)
		{
			epCode = bespResp.getEpCode();
		    epGunNo = bespResp.getEpGunNo();
		    bespNo=bespResp.getBespNo();
		    
	        EpGunCache epGunCache = EpGunService.getEpGunCache(epCode, epGunNo);
	        retCode = epGunCache.onEpBespokeResp(bespResp);
		}
		else
		{
            logger.error("bespoke,handleEpBespRet,retCode:{}",retCode);
			
			if(bespResp.getEpCode() !=null && bespResp.getEpCode().length()==16)
			{
				epCode= bespResp.getEpCode();
			}
			else
			{
				epCode = StringUtil.repeat("0", 16);
			}
			if(retCode==5)
			{
				bespNo = StringUtil.repeat("0", 12);
			}
			else
			{
				bespNo = bespResp.getBespNo();
			}
			
		}
	
		 logger.info("[bespoke] confirm,retCode:{},bespNo:{},epCode:{},epGunNo:{}"
	        		,new Object[]{retCode,bespResp.getBespNo(),bespResp.getEpCode(),bespResp.getEpGunNo()});
	        
		byte[] bespConfirmData = EpEncoder.do_bespoke_confirm(epCode, (byte) epGunNo, 
						bespNo,bespResp.getnRedo(),retCode);
		
		logger.info("[bespoke]handleEpBespRet nResult:{},epCode:{},epGunNo:{},redo:{},bespokeNo:{},SuccessFlag:{}",new Object[]{
				retCode,epCode,epGunNo ,bespResp.getnRedo(),bespResp.getBespNo(),bespResp.getSuccessFlag()});
					
		EpMessageSender.sendMessage(epCommClient,0, 0, Iec104Constant.C_BESPOKE_CONFIRM,bespConfirmData,time,epCommClient.getVersion());
	}
	public static void onEpCancelBespRet(EpCommClient epCommClient,EpCancelBespResp cancelBespResp,byte []cmdTimes )  {
		
		logger.debug("cancelbespoke,cancelBespResp:{}",cancelBespResp);
		String epCode = cancelBespResp.getEpCode();
		int epGunNo =cancelBespResp.getEpGunNo();
					
			
		EpGunCache epGunCache = EpGunService.getEpGunCache(epCode, epGunNo);
		
		if(epGunCache == null)
		{
			logger.error("cancelbespokeRet not find gun,epCode:{},epGunNo:{}",epCode,epGunNo);
			return ;
		}
	
		int errorCode = epGunCache.onEpCancelBespRet(epCommClient,cancelBespResp);
		
		logger.info("cancelbespoke,onEpCancelBespRet,epGunNo:{} errorCode:{}",epCode+epGunNo ,errorCode );
	}
	/**
	 * 插入预约到数据库
	 * @param pkEpCode
	 * @param epCode
	 * @param epGunNo
	 * @param bespCache
	 * @return
	 */
	
	public static long insertBespokeToDb(int pkEpId,int pkEpGunId,BespCache bespCache)
	{
		
		TblBespoke dbBespoke = new TblBespoke();
		
		dbBespoke.setPkEpId(pkEpId);
		dbBespoke.setPkEpGunNo(pkEpGunId);
		
		dbBespoke.setBespNo(bespCache.getBespNo());
		
		java.util.Date dtStartTime = new Date(bespCache.getStartTime() * 1000);
		dbBespoke.setBeginTime(dtStartTime);
	
		dbBespoke.setPrice(bespCache.getRate());
		
		dbBespoke.setAmt(bespCache.getFronzeAmt());
		
		dbBespoke.setUserid(bespCache.getAccountId());
		dbBespoke.setOrdertype(0);
		dbBespoke.setBespokeMark("预约");
		
		int bespTotalTimes = (int)(bespCache.getEndTime()- bespCache.getStartTime())/60;
		dbBespoke.setBespokeTime(bespTotalTimes + "");
		java.util.Date dtEndTime = new Date(bespCache.getEndTime() * 1000);
		dbBespoke.setEndTime(dtEndTime);
		
		dbBespoke.setRealityTime(dtEndTime);
		dbBespoke.setUpdateTime(new Date());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		String sStartTime =  dateFormat.format(dtStartTime);
		String sEndTime =  dateFormat.format(dtEndTime);
		String bespokeTimes = String.format("%s至%s", sStartTime, sEndTime);
		
		dbBespoke.setBespokeTimes(bespokeTimes);
		dbBespoke.setStatus(4);
		dbBespoke.setPayMode(bespCache.getPayMode());
		if(bespCache.getUserOrigin()!=null)
		{
			dbBespoke.setUserOrgNo(bespCache.getUserOrigin().getOrgNo());
			dbBespoke.setUserOrigin(0);
			dbBespoke.setPartnerIdentiy(bespCache.getUserOrigin().getCmdChIdentity());
		}
		
		
		DB.bespDao.insert(dbBespoke);
		
		return dbBespoke.getId();
		
	}
	/**
	 * 更新续预约到数据库
	 * @param pkEpCode
	 * @param epCode
	 * @param epGunNo
	 * @param bespCache
	 * @return
	 */
	public static void updateRedoBespokeToDb(long pkBespId,BespCache bespCache)
	{
		logger.info("[bespoke]updateRedoBespokeToDb,pkBespId:{}",pkBespId);
		TblBespoke dbBespoke = new TblBespoke();
		dbBespoke.setId(pkBespId);
		
	
		java.util.Date dtStartTime = new Date(bespCache.getStartTime() * 1000);
		java.util.Date dtEndTime = new Date(bespCache.getEndTime() * 1000);
		
		dbBespoke.setEndTime(dtEndTime);
		dbBespoke.setAmt(bespCache.getFronzeAmt());
		
		dbBespoke.setBespokeMark("续预约");
		
		int bespTotalTimes = (int)(bespCache.getEndTime()- bespCache.getStartTime())/60;
		dbBespoke.setBespokeTime(bespTotalTimes + "");
		dbBespoke.setEndTime(dtEndTime);
		dbBespoke.setRealityTime(dtEndTime);
		
		dbBespoke.setUpdateTime(new Date());
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		String sStartTime =  dateFormat.format(dtStartTime);
		String sEndTime =  dateFormat.format(dtEndTime);
		String bespokeTimes = String.format("%s至%s", sStartTime, sEndTime);
		
		dbBespoke.setBespokeTimes(bespokeTimes);
		
		DB.bespDao.updateRedo(dbBespoke);		
	}
	/**
	 * 结算信息到数据库
	 * @param pkEpCode
	 * @param epCode
	 * @param epGunNo
	 * @param bespCache
	 * @return
	 */
	
	public static void saveEndBespokeToDb(long pkBespId,BespCache bespCache,BigDecimal realBespAmt,int bespStatus)
	{
		logger.debug("statBespokeToDb,bespCache:{}",bespCache);
		try
		{
		TblBespoke dbBespoke = new TblBespoke();
		dbBespoke.setId(pkBespId);
		
	
		java.util.Date dtStartTime = new Date(bespCache.getStartTime() * 1000);
		java.util.Date dtEndTime = new Date(bespCache.getEndTime() * 1000);
		java.util.Date dtRealEndTime = new Date(bespCache.getRealEndTime() * 1000);
		
		dbBespoke.setEndTime(dtEndTime);
		dbBespoke.setAmt(realBespAmt);
		
		dbBespoke.setBespokeMark("预约");
		
		dbBespoke.setStatus(bespStatus);
		
		int bespTotalTimes = (int)(bespCache.getRealEndTime()- bespCache.getStartTime())/60;
		dbBespoke.setBespokeTime(bespTotalTimes + "");
		
		dbBespoke.setRealityTime(dtRealEndTime);
		
		dbBespoke.setUpdateTime(new Date());
		
		dbBespoke.setOrdertype(1);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		String sStartTime =  dateFormat.format(dtStartTime);
		String sRealEndTime =  dateFormat.format(dtRealEndTime);
		String bespokeTimes = String.format("%s至%s", sStartTime, sRealEndTime);
		
		dbBespoke.setBespokeTimes(bespokeTimes);
		
		DB.bespDao.update(dbBespoke);
		}
		catch(Exception e)
		{
			logger.error("[endbespoke]saveEndBespokeToDb exception,bespCache:{}",bespCache);
		}
	}
	
	public static BigDecimal statBespoke(BespCache bespCacheObj)
	{
		//.结算并修改用户金额
		
		long realBespTime = RateService.calcBespTime(
				bespCacheObj.getStartTime(), bespCacheObj.getRealEndTime(),
				bespCacheObj.getRealEndTime());
		
		BigDecimal realBespAmt= new BigDecimal(0.0);
		if(realBespTime<=(6*60))
		{
			realBespAmt = RateService.calcBespAmt(
					bespCacheObj.getRate(), realBespTime);
		}
		else
		{
			logger.error("bespoke over 360 min,bespokeno:{},realBespTime:{}" ,bespCacheObj.getBespNo(),realBespTime);
		}
		
		logger.info("statbespoke,bespokeno:{},realBespAmt:{}" ,bespCacheObj.getBespNo(),realBespAmt.doubleValue());

		return realBespAmt;
	}
	/**
	* 0:预约命令，没收到应答，
	* 1.预约锁定,
	* 2：结束预约(算时间和钱).
	* 3.存入数据库
	* 4.冷却时间
	 * @param states
	 * @return
	 */
	
	
		public  static String getBespokeMemDesc(int states)
		{
			String desc="";
			switch(states)
			{
			case 0:
				desc="取消接受预约";
				break;
			case 2:
				desc="预约结束";
				break;
			case 3:
				desc="预约结束";
				break;
				
			case 4:
				desc="预约成功,锁定状态";
				break;
			case 5:
				desc="预约确认中";
				break;
			case 6:
				desc="预约失败";
				break;
			case 7:
				desc="冷却中";
				break;
			default:
				desc="未知状态";
				break;
			}
			return desc;
		}
		
		
	
		/**
		 * 
		 * @param EpBespResp
		 * @return 4:参数错误
		 * @return 5:
		 * @return 6:
		 * @return 7:
		 */
		private static int checkBespRespParams(EpBespResp bespResp)
		{
			if(bespResp==null)
			{
				logger.info("bespoke,checkBespRespParams bespResp==null");
				return 4;
			}
			String epCode = bespResp.getEpCode();
			if(epCode==null || epCode.length()!=16)
			{
				logger.info("bespoke,checkBespRespParams invalid epCode:{}",epCode);
				return 4;
			}
			int epGunNo = bespResp.getEpGunNo();
			
			if(bespResp.getSuccessFlag()!=0&&  bespResp.getSuccessFlag()!=1)
			{
				logger.info("bespoke,checkBespRespParams invalid epCode:{},epGunNo:{},successFlag:{}",
						new Object[]{epCode,epGunNo,bespResp.getSuccessFlag()});
				return 4;
			}
			
	        String bespNo = bespResp.getBespNo();		
			if(bespNo==null || bespNo.length()!=12)
			{
				logger.info("bespoke,checkBespRespParams not find epCode:{},epGunNo:{},bespNo:{}",
						new Object[]{epCode,epGunNo,bespNo});
				return 5;
			}
			String zeroBespNo= StringUtil.repeat("0", 12);
			if( bespNo.compareTo(zeroBespNo)==0)
			{ 
				 logger.error("bespoke,checkBespRespParams invalid epCode:{},epGunNo:{},bespNo:{}",
						new Object[]{epCode,epGunNo,bespNo});
				 return 6;
			}
			EpGunCache epGunCache = EpGunService.getEpGunCache(epCode,epGunNo);
			if(epGunCache == null)
			{
				logger.info("bespoke,checkRespParams not find epGunCache,epCode:{},epGunNo:{},bespNo:{}",
						new Object[]{epCode,epGunNo,bespNo});
				return 7;
			}
			return 0;
			
		}
}
