package com.wzitech.gamegold.facade.backend.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.common.enums.ShOpenState;
import com.wzitech.gamegold.common.main.IMainStationManager;
import com.wzitech.gamegold.common.main.MainStationConstant;
import com.wzitech.gamegold.facade.backend.business.ISellerShManager;
import com.wzitech.gamegold.repository.business.IHuanXinManager;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.repository.entity.SendDataDTO;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.dao.IPurchaserDataDao;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.utils.ISendHttpToSevenBao;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 卖家
 */
@Component
public class SellerShManagerImpl extends AbstractBusinessObject implements ISellerShManager {
    @Autowired
    ISellerDBDAO sellerDBDAO;
    @Autowired
    ISendHttpToSevenBao sendHttpToSevenBao;
    @Autowired
    IPurchaserDataDao purchaserDataDao;
    @Autowired
    IHuanXinManager huanXinManager;
    @Autowired
    CreateZBaoUtil createZBaoUtil;
    @Autowired
    IMainStationManager mainStationManager;
    @Autowired
    IPurchaserDataManager purchaserDataManager;
    @Autowired
    ISellerManager sellerManager;

    @Value("${7bao.ser.key}")
    private String serKey;

    @Override
    @Transactional
    public void checkSh(String account, String uid, boolean isOpenSh, Long id) {
        //340096 ZW_C_JB_00021
        SellerInfo seller = sellerDBDAO.findByAccountAndUid(account, uid);
        if (seller == null) {
            throw new SystemException(ResponseCodes.EmptyPurchaseAccount.getCode(), ResponseCodes.EmptyPurchaseAccount.getMessage());
        }
        SendDataDTO sendDataDTO = new SendDataDTO();
        //添加数据  loginAccount   qq   name   isUserBind  uid  phoneNumber applyTime
        sendDataDTO.setLoginAccount(seller.getLoginAccount());
        int openShState = isOpenSh ? ShOpenState.OPEN.getCode() : ShOpenState.CLOSE.getCode();

        PurchaserData purchaserData = purchaserDataManager.selectByIdForUpdate(seller.getId());
        if (purchaserData == null) {
            sendDataDTO.setCheckTotalAmount(BigDecimal.ZERO);
            sendDataDTO.setCheckAvailableAmount(BigDecimal.ZERO);
            sendDataDTO.setCheckFreezeAmount(BigDecimal.ZERO);
        } else {
            sendDataDTO.setCheckTotalAmount(purchaserData.getTotalAmountZBao());
            sendDataDTO.setCheckAvailableAmount(purchaserData.getAvailableAmountZBao());
            sendDataDTO.setCheckFreezeAmount(purchaserData.getFreezeAmountZBao());
        }

        if (isOpenSh) {
//            huanXinManager.registerHuanXin(id);
            //开通收货
            sellerDBDAO.checkSh(account, uid, openShState);

            //开通收货的时候，判断是否存在当前卖家的收货信息，不存在则添加，存在则不操作
            SellerInfo sellerInfo = sellerDBDAO.findByAccountAndUid(account, uid);
            if (purchaserData == null && sellerInfo != null) {
                purchaserData = new PurchaserData();
                purchaserData.setId(sellerInfo.getId());
                purchaserData.setStartTime(0);
                purchaserData.setEndTime(24);
                purchaserData.setFreezeAmount(BigDecimal.ZERO);
                purchaserData.setAvailableAmount(BigDecimal.ZERO);
                purchaserData.setTotalAmount(BigDecimal.ZERO);
                purchaserData.setCjl(BigDecimal.ZERO);
                purchaserData.setTradingVolume(0L);
                purchaserData.setPjys(BigDecimal.ZERO);
                purchaserData.setCredit(0);
                if (purchaserData.getDeliveryType() == null) {
                    purchaserData.setDeliveryType(ShDeliveryTypeEnum.Close.getCode());
                }

                purchaserDataDao.insert(purchaserData);
            }

            if (seller.getisBind() != null && seller.getisBind() && seller.getisAgree() != null && seller.getisAgree()) {
                sendDataDTO.setUserBind(seller.getisBind());
                sendDataDTO.setIsShBind(true);
                createZBaoUtil.createZBaoAccount(sendDataDTO);
            }
            //同时把7bao账号放入表内
            //sellerDBDAO.updateSevenBaoAccount(account, uid,true);
        }
        else {
            //关闭收货
            sellerDBDAO.checkSh(account, uid, openShState);
            if (seller.getisBind() != null && seller.getisBind() && seller.getisAgree() != null && seller.getisAgree()) {
                sendDataDTO.setUserBind(seller.getisBind());
                sendDataDTO.setIsShBind(false);
                createZBaoUtil.createZBaoAccount(sendDataDTO);
            }
        }

//        //开通收货的时候，判断是否存在当前卖家的收货信息，不存在则添加，存在则不操作
//        if (isOpenSh) {
////            PurchaserData purchaserData = purchaserDataDao.selectByAccount(account);
//            SellerInfo sellerInfo = sellerDBDAO.findByAccountAndUid(account, uid);
//
//            if (purchaserData == null && sellerInfo != null) {
//                purchaserData = new PurchaserData();
//                purchaserData.setId(sellerInfo.getId());
//                purchaserData.setStartTime(0);
//                purchaserData.setEndTime(24);
//                purchaserData.setFreezeAmount(BigDecimal.ZERO);
//                purchaserData.setAvailableAmount(BigDecimal.ZERO);
//                purchaserData.setTotalAmount(BigDecimal.ZERO);
//                purchaserData.setCjl(BigDecimal.ZERO);
//                purchaserData.setTradingVolume(0L);
//                purchaserData.setPjys(BigDecimal.ZERO);
//                purchaserData.setCredit(0);
//                if (purchaserData.getDeliveryType() == null) {
//                    purchaserData.setDeliveryType(ShDeliveryTypeEnum.Close.getCode());
//                }
//
//                purchaserDataDao.insert(purchaserData);
//            }
//        }
    }

    /**
     * 开通收货时调用主站设置该用户为商户，关闭收货的时候变更为普通用户
     * code : 1 的时候设置为商户   0 的时候取消商户
     */
    public void setMerChant(String uid, int code) {
        String jsonParam = "{'UserId':'" + uid + "','merchantType':'" + code + "'}";
        try {
            mainStationManager.GetMainRest(MainStationConstant.URL_REST, MainStationConstant.RESULT_AUTHVERS, MainStationConstant.RESULT_TYPE, MainStationConstant.SET_USER_TOBE_MERCHANT,
                    jsonParam, MainStationConstant.RESULT_MD5, MainStationConstant.RESULT_FIELDS, MainStationConstant.RESULT_VERSION);
        } catch (Exception e) {
            logger.error("调用主站设置{}：商户失败", uid);
            throw new SystemException("设置商户失败" + uid);
        }
    }

    /**
     * 请求主站查询该用户是否有过实名制认证
     */
    public String isRealName(String uid) {
        String jsonParam = "{\"UserId\":\"" + uid + "\"}";
        String result = mainStationManager.GetMainRest(MainStationConstant.URL_REST, MainStationConstant.RESULT_AUTHVERS, MainStationConstant.RESULT_TYPE, MainStationConstant.METHOD_GET_USER_REALNAME,
                jsonParam, MainStationConstant.RESULT_MD5, MainStationConstant.RESULT_FIELDS, MainStationConstant.RESULT_VERSION);
        //转化主站返回来的结果
        JSONObject jsonObject = JSONObject.fromObject(result);
        JSONObject bizResult = jsonObject.getJSONObject("BizResult");
        String realName;
        if (!bizResult.isNullObject()) {
            realName = bizResult.getString("RealName");
            if (realName == null || "null".equals(realName)) {
                realName = "-1";
            }
        } else {
            realName = "-1";
        }
        return realName;
    }
}
