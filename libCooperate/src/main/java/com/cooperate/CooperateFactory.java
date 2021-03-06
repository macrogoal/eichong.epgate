package com.cooperate;

import com.cooperate.TCEC.Changan.TCECChangAnPush;
import com.cooperate.TCEC.Changan.TCECChangAnService;
import com.cooperate.TCEC.EAST.TCECEASTPush;
import com.cooperate.TCEC.EAST.TCECEASTService;
import com.cooperate.TCEC.EChong.TCECEChongPush;
import com.cooperate.TCEC.EChong.TCECEChongService;
import com.cooperate.TCEC.EVC.TCECEVCPush;
import com.cooperate.TCEC.EVC.TCECEVCService;
import com.cooperate.TCEC.RTA.TCECCRTAService;
import com.cooperate.TCEC.RTA.TCECRTAPush;
import com.cooperate.TCEC.SGCC.TCECSGCCPush;
import com.cooperate.TCEC.SGCC.TCECSGCCService;
import com.cooperate.TCEC.alipay.TCECAliPayPush;
import com.cooperate.TCEC.alipay.TCECAliPayService;
import com.cooperate.TCEC.amap.TCECAmapPush;
import com.cooperate.TCEC.amap.TCECAmapService;
import com.cooperate.TCEC.chuanhua.TCECChuanHuaPush;
import com.cooperate.TCEC.chuanhua.TCECChuanHuaService;
import com.cooperate.TCEC.hainan.TCECHaiNanPush;
import com.cooperate.TCEC.hainan.TCECHaiNanService;
import com.cooperate.TCEC.heshun.TCECHeShunPush;
import com.cooperate.TCEC.heshun.TCECHeShunService;
import com.cooperate.TCEC.nanchong.TCECNanChongPush;
import com.cooperate.TCEC.nanchong.TCECNanChongService;
import com.cooperate.TCEC.nanrui.TCECENanRuiService;
import com.cooperate.TCEC.nanrui.TCECNanRuiPush;
import com.cooperate.TCEC.ponycar.TCECPonyCarPush;
import com.cooperate.TCEC.ponycar.TCECPonyCarService;
import com.cooperate.TCEC.putian.TCECPuTianPush;
import com.cooperate.TCEC.putian.TCECPuTianService;
import com.cooperate.TCEC.qidian.TCECQIDianService;
import com.cooperate.TCEC.qidian.TCECQiDianPush;
import com.cooperate.TCEC.qingxiang.TCECEQingXiangPush;
import com.cooperate.TCEC.qingxiang.TCECEQingXiangService;
import com.cooperate.TCEC.shenzhen.TCECEShenZhenPush;
import com.cooperate.TCEC.shenzhen.TCECEShenZhenService;
import com.cooperate.TCEC.shouqi.TCECShouQiPush;
import com.cooperate.TCEC.shouqi.TCECShouQiService;
import com.cooperate.TCEC.xiaojukeji.TCECEXiaoJuPush;
import com.cooperate.TCEC.xiaojukeji.TCECEXiaoJuService;
import com.cooperate.cczc.CCZCPush;
import com.cooperate.cczc.CCZCService;
import com.cooperate.constant.KeyConsts;
import com.cooperate.elease.EleasePush;
import com.cooperate.elease.EleaseService;
import com.cooperate.shstop.SHStopPush;
import com.cooperate.shstop.SHStopService;
import com.ec.constants.UserConstants;
import com.ec.utils.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class CooperateFactory {
    private static final Logger logger = LoggerFactory.getLogger(LogUtil.getLogName(CooperateFactory.class.getName()));

    private static String path = System.getProperty("file.separator")
            + System.getProperty("user.dir")
            + System.getProperty("file.separator") + "conf"
            + System.getProperty("file.separator") + "cooperate"
            + System.getProperty("file.separator");

    private static ConcurrentHashMap<Integer, Push> cooperateMaps = new ConcurrentHashMap<>();

    public static void init() {
        try {
            if (isCooperate(UserConstants.ORG_EC)) {
                EleasePush eleasePush = new EleasePush();
                if (eleasePush.init(path + KeyConsts.ELEASE_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_EC);
                    cooperateMaps.put(UserConstants.ORG_EC, eleasePush);

                    EleaseService.startEleasePushTimeout(10);
                }
            }

            if (isCooperate(UserConstants.ORG_CCZC)) {
                CCZCPush cczcPush = new CCZCPush();
                if (cczcPush.init(path + KeyConsts.CCZC_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_CCZC);
                    cooperateMaps.put(UserConstants.ORG_CCZC, cczcPush);

                    CCZCService.startCCZCPushTimeout(10);
                }
            }

            if (isCooperate(UserConstants.ORG_SHSTOP)) {
                SHStopPush shPush = new SHStopPush();
                if (shPush.init(path + KeyConsts.SHSTOP_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_SHSTOP);
                    cooperateMaps.put(UserConstants.ORG_SHSTOP, shPush);

                    SHStopService.startSHSTOPPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_ECHONG)) {
                TCECEChongPush tceceChongPush = new TCECEChongPush();
                if (tceceChongPush.init(path + KeyConsts.TCEC_CHONG_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_ECHONG);
                    cooperateMaps.put(UserConstants.ORG_TCEC_ECHONG, tceceChongPush);

                    TCECEChongService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_SHENZHEN)) {
                TCECEShenZhenPush tceceShenZhenPush = new TCECEShenZhenPush();
                if (tceceShenZhenPush.init(path + KeyConsts.TCEC_SHENZHEN_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_SHENZHEN);
                    cooperateMaps.put(UserConstants.ORG_TCEC_SHENZHEN, tceceShenZhenPush);

                    TCECEShenZhenService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_BEIQI)) {
                TCECEQingXiangPush tceceQingXiangPush = new TCECEQingXiangPush();
                if (tceceQingXiangPush.init(path + KeyConsts.TCEC_QINGXIANG_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_BEIQI);
                    cooperateMaps.put(UserConstants.ORG_TCEC_BEIQI, tceceQingXiangPush);

                    TCECEQingXiangService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_NANRUI)) {
                TCECNanRuiPush tcecNanRuiPush = new TCECNanRuiPush();
                if (tcecNanRuiPush.init(path + KeyConsts.TCEC_NANRUI_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_NANRUI);
                    cooperateMaps.put(UserConstants.ORG_TCEC_NANRUI, tcecNanRuiPush);

                    TCECENanRuiService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_HESHUN)) {
                TCECHeShunPush tcecHeShunPush = new TCECHeShunPush();
                if (tcecHeShunPush.init(path + KeyConsts.TCEC_HESHUN_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_HESHUN);
                    cooperateMaps.put(UserConstants.ORG_TCEC_HESHUN, tcecHeShunPush);

                    TCECHeShunService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_EVC)) {
                TCECEVCPush tcecevcPush = new TCECEVCPush();
                if (tcecevcPush.init(path + KeyConsts.TCEC_EVC_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_EVC);
                    cooperateMaps.put(UserConstants.ORG_EVC, tcecevcPush);

                    TCECEVCService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_GUOWANG)) {
                TCECSGCCPush tcecsgccPush = new TCECSGCCPush();
                if (tcecsgccPush.init(path + KeyConsts.TCEC_SGCC_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_GUOWANG);
                    cooperateMaps.put(UserConstants.ORG_TCEC_GUOWANG, tcecsgccPush);

                    TCECSGCCService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_NANCHONG)) {
                TCECNanChongPush tcecNanChongPush = new TCECNanChongPush();
                if (tcecNanChongPush.init(path + KeyConsts.TCEC_NANCHONG_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_NANCHONG);
                    cooperateMaps.put(UserConstants.ORG_TCEC_NANCHONG, tcecNanChongPush);

                    TCECNanChongService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_ALIPAY)) {
                TCECAliPayPush tcecAliPayPush = new TCECAliPayPush();
                if (tcecAliPayPush.init(path + KeyConsts.TCEC_ALIPAY_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_ALIPAY);
                    cooperateMaps.put(UserConstants.ORG_TCEC_ALIPAY, tcecAliPayPush);

                    TCECAliPayService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_HAINAN)) {
                TCECHaiNanPush tcecHaiNanPush = new TCECHaiNanPush();
                if (tcecHaiNanPush.init(path + KeyConsts.TCEC_HAINAN_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_HAINAN);
                    cooperateMaps.put(UserConstants.ORG_TCEC_HAINAN, tcecHaiNanPush);

                    TCECHaiNanService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_CHUANHUA)) {
                TCECChuanHuaPush tcecChuanHuaPush = new TCECChuanHuaPush();
                if (tcecChuanHuaPush.init(path + KeyConsts.TCEC_CHUANHUA_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_CHUANHUA);
                    cooperateMaps.put(UserConstants.ORG_TCEC_CHUANHUA, tcecChuanHuaPush);

                    TCECChuanHuaService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_XIAOJU)) {
                TCECEXiaoJuPush tceceXiaoJuPush = new TCECEXiaoJuPush();
                if (tceceXiaoJuPush.init(path + KeyConsts.TCEC_XIAOJU_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_XIAOJU);
                    cooperateMaps.put(UserConstants.ORG_TCEC_XIAOJU, tceceXiaoJuPush);
                    TCECEXiaoJuService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_AMAP)) {
                TCECAmapPush tcecAmapPush = new TCECAmapPush();
                if (tcecAmapPush.init(path + KeyConsts.TCEC_AMAP_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_AMAP);
                    cooperateMaps.put(UserConstants.ORG_AMAP, tcecAmapPush);

                    TCECAmapService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_ChangAn)) {
                TCECChangAnPush tcecChangAnPush = new TCECChangAnPush();
                if (tcecChangAnPush.init(path + KeyConsts.TCEC_CHANGAN_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_ChangAn);
                    cooperateMaps.put(UserConstants.ORG_TCEC_ChangAn, tcecChangAnPush);
                    TCECChangAnService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_DongFeng)) {
                TCECEASTPush tceceastPush = new TCECEASTPush();
                if (tceceastPush.init(path + KeyConsts.TCEC_EAST_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_DongFeng);
                    cooperateMaps.put(UserConstants.ORG_TCEC_DongFeng, tceceastPush);
                    TCECEASTService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_ShenZhenTraffic)) {
                TCECRTAPush tcecrtaPush = new TCECRTAPush();
                if (tcecrtaPush.init(path + KeyConsts.TCEC_RTA_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_ShenZhenTraffic);
                    cooperateMaps.put(UserConstants.ORG_TCEC_ShenZhenTraffic, tcecrtaPush);
                    TCECCRTAService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_QiDian)) {
                TCECQiDianPush tcecQiDianPush = new TCECQiDianPush();
                if (tcecQiDianPush.init(path + KeyConsts.TCEC_QIDIAN_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_QiDian);
                    cooperateMaps.put(UserConstants.ORG_TCEC_QiDian, tcecQiDianPush);
                    TCECQIDianService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_PonyCar)) {
                TCECPonyCarPush tcecPonyCarPush = new TCECPonyCarPush();
                if (tcecPonyCarPush.init(path + KeyConsts.TCEC_PONYCAR_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_PonyCar);
                    cooperateMaps.put(UserConstants.ORG_TCEC_PonyCar, tcecPonyCarPush);
                    TCECPonyCarService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_ShouQi)) {
                TCECShouQiPush tcecShouQiPush = new TCECShouQiPush();
                if (tcecShouQiPush.init(path + KeyConsts.TCEC_shouqi_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_ShouQi);
                    cooperateMaps.put(UserConstants.ORG_TCEC_ShouQi, tcecShouQiPush);
                    TCECShouQiService.startPushTimeout(10);
                }
            }
            if (isCooperate(UserConstants.ORG_TCEC_PuTian)) {
                TCECPuTianPush tcecPuTianPush = new TCECPuTianPush();
                if (tcecPuTianPush.init(path + KeyConsts.TCEC_putian_SETTING)) {
                    logger.info(LogUtil.addExtLog("orgNo"), UserConstants.ORG_TCEC_PuTian);
                    cooperateMaps.put(UserConstants.ORG_TCEC_PuTian, tcecPuTianPush);
                    TCECPuTianService.startPushTimeout(10);
                }
            }


        } catch (Exception e) {
            logger.error(LogUtil.addExtLog("exception"), e.getMessage());
        }
    }

    public static boolean isCooperate(int orgNo) {
        File file;

        if (orgNo == UserConstants.ORG_EC) {
            file = new File(path + KeyConsts.ELEASE_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.ELEASE_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_CCZC) {
            file = new File(path + KeyConsts.CCZC_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.CCZC_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_SHSTOP) {
            file = new File(path + KeyConsts.SHSTOP_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.SHSTOP_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_ECHONG) {
            file = new File(path + KeyConsts.TCEC_CHONG_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_CHONG_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_SHENZHEN) {
            file = new File(path + KeyConsts.TCEC_SHENZHEN_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_SHENZHEN_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_BEIQI) {
            file = new File(path + KeyConsts.TCEC_QINGXIANG_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_QINGXIANG_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_NANRUI) {
            file = new File(path + KeyConsts.TCEC_NANRUI_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_NANRUI_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_HESHUN) {
            file = new File(path + KeyConsts.TCEC_HESHUN_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_HESHUN_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_EVC) {
            file = new File(path + KeyConsts.TCEC_EVC_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_EVC_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_GUOWANG) {
            file = new File(path + KeyConsts.TCEC_SGCC_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_SGCC_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_NANCHONG) {
            file = new File(path + KeyConsts.TCEC_NANCHONG_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_NANCHONG_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_ALIPAY) {
            file = new File(path + KeyConsts.TCEC_ALIPAY_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_ALIPAY_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_HAINAN) {
            file = new File(path + KeyConsts.TCEC_HAINAN_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_HAINAN_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_CHUANHUA) {
            file = new File(path + KeyConsts.TCEC_CHUANHUA_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_CHUANHUA_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_XIAOJU) {
            file = new File(path + KeyConsts.TCEC_XIAOJU_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_XIAOJU_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_AMAP) {
            file = new File(path + KeyConsts.TCEC_AMAP_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_AMAP_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_ChangAn) {
            file = new File(path + KeyConsts.TCEC_CHANGAN_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_CHANGAN_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_DongFeng) {
            file = new File(path + KeyConsts.TCEC_EAST_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_EAST_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_ShenZhenTraffic) {
            file = new File(path + KeyConsts.TCEC_RTA_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_RTA_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_QiDian) {
            file = new File(path + KeyConsts.TCEC_QIDIAN_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_QIDIAN_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_PonyCar) {
            file = new File(path + KeyConsts.TCEC_PONYCAR_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_PONYCAR_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_ShouQi) {
            file = new File(path + KeyConsts.TCEC_shouqi_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_shouqi_SETTING);
            if (file.exists()) return true;
        } else if (orgNo == UserConstants.ORG_TCEC_PuTian) {
            file = new File(path + KeyConsts.TCEC_putian_SETTING);
            logger.debug(LogUtil.addExtLog("fileName"), path + KeyConsts.TCEC_putian_SETTING);
            if (file.exists()) return true;
        }


        return false;
    }

    public static IPush getPush(int orgNo) {
        return cooperateMaps.get(orgNo);
    }

    public static Push getCoPush(int orgNo) {
        return cooperateMaps.get(orgNo);
    }
}
