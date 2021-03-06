package com.ormcore.dao;

import com.ormcore.cache.GameContext;
import com.ormcore.model.FinAccountConfigRela;

public class DB {
	public static RateInfoDao rateInfoDao = (RateInfoDao) GameContext.getBean("rateInfoDao");
		
	public static TblUserInfoDao userInfoDao = (TblUserInfoDao) GameContext.getBean("tblUserInfoDao");

    public static FinAccountDao finAccountDao = (FinAccountDao) GameContext.getBean("finAccountDao");

    public static TblUserDao tblUserDao = (TblUserDao) GameContext.getBean("tblUserDao");

    public static TblUserCompanyDao tblUserCompanyDao = (TblUserCompanyDao) GameContext.getBean("tblUserCompanyDao");

    public static FinAccountConfigRelaDao finAccountConfigRelaDao = (FinAccountConfigRelaDao) GameContext.getBean("finAccountConfigRelaDao");

    public static TblCompanyDataDao tblCompanyDataDao = (TblCompanyDataDao) GameContext.getBean("tblCompanyDataDao");

	public static TblUserNormalDao userNormalDao = (TblUserNormalDao) GameContext.getBean("tblUserNormalDao");
	
	public static TblUserBusinessDao userBusinessDao = (TblUserBusinessDao) GameContext.getBean("tblUserBusinessDao");


	public static TblElectricPileDao epClientDao = (TblElectricPileDao) GameContext.getBean("tblElectricPileDao");

	public static BespokeDao bespDao = (BespokeDao) GameContext.getBean("bespokeDao");
	
	public static ChargingOrderDao chargeOrderDao = (ChargingOrderDao) GameContext.getBean("chargingOrderDao");
	
	public static PurchaseHistoryDao phDao = (PurchaseHistoryDao) GameContext.getBean("purchaseHistoryDao");
	
	public static EpGunDao epGunDao = (EpGunDao) GameContext.getBean("epGunDao");
	
	public static ChargingrecordDao chargingrecordDao = (ChargingrecordDao) GameContext.getBean("chargingrecordDao");
	
	public static ChargingfaultrecordDao chargingfaultrecordDao = (ChargingfaultrecordDao) GameContext.getBean("chargingfaultrecordDao");
	
    public static TblConcentratorDao concentratorDao = (TblConcentratorDao) GameContext.getBean("tblConcentratorDao");
    
    //public static TblPowerStationDao powerStationDao = (TblPowerStationDao) GameContext.getBean("tblPowerStationDao");

    public static TblPartnerTimeDao partnerTimeDao = (TblPartnerTimeDao) GameContext.getBean("tblPartnerTimeDao");

    public static TblEquipmentVersionDao equipmentVersionDao = (TblEquipmentVersionDao) GameContext.getBean("tblEquipmentVersionDao");

    public static ChargeCardDao chargeCardDao = (ChargeCardDao) GameContext.getBean("chargeCardDao");
   
    public static TblChargeACInfoDao chargeACInfoDao = (TblChargeACInfoDao) GameContext.getBean("tblChargeACInfoDao");
	
	public static TblChargeDCInfoDao chargeDCInfoDao = (TblChargeDCInfoDao) GameContext.getBean("tblChargeDCInfoDao");
	
    public static BomListDao bomListDao = (BomListDao) GameContext.getBean("bomListDao");
    
    public static TypeSpanDao typeSpanDao = (TypeSpanDao) GameContext.getBean("typeSpanDao");
    
    public static EquipmentRepairDao equipmentRepairDao = (EquipmentRepairDao) GameContext.getBean("equipmentRepairDao");
    
    public static TblVehicleBatteryDao vehicleBatteryDao = (TblVehicleBatteryDao) GameContext.getBean("tblVehicleBatteryDao");

    public static TblPowerModuleDao powerModuleDao = (TblPowerModuleDao) GameContext.getBean("tblPowerModuleDao");

    public static TblUserNewcouponDao userNewcouponDao = (TblUserNewcouponDao)GameContext.getBean("tblUserNewcouponDao");
    public static TblEpGateCfgDao epGateCfgDao = (TblEpGateCfgDao) GameContext.getBean("tblEpGateCfgDao");

    public static TblCouponDao couponDao = (TblCouponDao) GameContext.getBean("tblCouponDao");
    public static TblCarVinDao carVinDao = (TblCarVinDao) GameContext.getBean("tblCarVinDao");
    
    public static TblJpushDao jpushDao = (TblJpushDao) GameContext.getBean("tblJpushDao");
    
    public static TblUserThreshodDao userThreshodDao = (TblUserThreshodDao) GameContext.getBean("tblUserThreshodDao");
    
    public static TblCompanyDao companyDao = (TblCompanyDao) GameContext.getBean("tblCompanyDao");

    public static TblTimingChargeDao timingChargeDao = (TblTimingChargeDao) GameContext.getBean("tblTimingChargeDao");
    
    public static CompanyRelaDao companyRelaDao = (CompanyRelaDao) GameContext.getBean("companyRelaDao");
    
    public static TblConsumeRecordDao consumeRecordDao = (TblConsumeRecordDao) GameContext.getBean("tblConsumeRecordDao");
    
    public static ElectricpileConfigDao electricpileConfigDao = (ElectricpileConfigDao) GameContext.getBean("electricpileConfigDao");
    
    public static ElectricpileMeternumDao electricpileMeternumDao = (ElectricpileMeternumDao) GameContext.getBean("electricpileMeternumDao");
    
    public static ElectricpileWorkargDao electricpileWorkargDao = (ElectricpileWorkargDao) GameContext.getBean("electricpileWorkargDao");

    public static TblOffLineInfoDao tblOffLineInfoDao = (TblOffLineInfoDao) GameContext.getBean("tblOffLineInfoDao");

    public static FavRecordDao favRecordDao = (FavRecordDao) GameContext.getBean("favRecordDao");
}
