package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.main.IMainStationManager;
import com.wzitech.gamegold.common.main.MainStationConstant;
import com.wzitech.gamegold.repository.business.IAgreeInitAccountManager;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.AccountIntDto;
import com.wzitech.gamegold.repository.entity.JBPayOrderTo7BaoEO;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.repository.util.AccountZBaoUtil;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 340032 on 2017/8/26.
 */
@Component
public class AgreeInitAccountImpl extends AbstractBusinessObject implements IAgreeInitAccountManager {

    @Autowired
    ISellerManager sellerManager;
    @Autowired
    AccountIntDto accountIntDto;
    @Autowired
    AccountZBaoUtil accountZBaoUtil;
    @Autowired
    IPurchaserDataManager purchaserDataManager;
    @Autowired
    DeliveryOrder deliveryOrder;
    @Autowired
    IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    IMainStationManager mainStationManager;


    @Override
    @Transactional
    public SellerInfo initAccount(String realName) {
        //查询该用户所有信息
        String userLoginAccount = CurrentUserContext.getUserLoginAccount();
        SellerInfo sellerInfo = sellerManager.queryloginAccountNotLike(userLoginAccount);
        if (sellerInfo == null) {
            throw new SystemException(ResponseCodes.EmptySellerInfo.getCode(), ResponseCodes.EmptySellerInfo.getMessage());
        }
        logger.info("initAccount,获取卖家信息{}：", sellerInfo);
        List<JBPayOrderTo7BaoEO> jbPayOrderTo7BaoEOs = null;
        int count = deliveryOrderManager.queryFund(userLoginAccount);
        if (count > 0) {
            //抛异常，有未结完订单
            throw new SystemException(ResponseCodes.OrderEnd.getCode(), ResponseCodes.OrderEnd.getMessage());
        }
//        PurchaserData purchaserData =null;
//        if (count == 0) {
        //根据用户ID查找给用户的资金,并添加到7bao账户
//             purchaserData = purchaserDataManager.selectByIdForUpdate(sellerInfo.getId());
//            if (purchaserData != null) {
//               jbPayOrderTo7BaoEOs = sellerManager.updatePurchaserData(userLoginAccount, purchaserData);
//            }
//        } else {
//            //抛异常，有未结完订单
//            throw new SystemException(ResponseCodes.OrderEnd.getCode(), ResponseCodes.OrderEnd.getMessage());
//        }

        PurchaserData purchaserData = purchaserDataManager.selectByIdForUpdate(sellerInfo.getId());
        JSONArray jsonArray = JSONArray.fromObject(jbPayOrderTo7BaoEOs);
        List<SellerInfo> list = new ArrayList<SellerInfo>();
        list.add(sellerInfo);
        //买家是否开通收货 1为开通  2为没有开通
        if (sellerInfo.getOpenShState() == 1) {
            if (sellerInfo.getisBind() == null || !sellerInfo.getisBind() || null == sellerInfo.getisAgree() || !sellerInfo.getisAgree()) {
                AccountIntDto accountIntDto = new AccountIntDto();
                accountIntDto.setLoginAccount(sellerInfo.getLoginAccount());
                accountIntDto.setPhoneNumber(sellerInfo.getPhoneNumber());
                accountIntDto.setName(realName);
                accountIntDto.setQq(Long.valueOf(sellerInfo.getQq()));
                accountIntDto.setUid(sellerInfo.getUid());
                accountIntDto.setUserBind(true);
                accountIntDto.setTotalAmountBao(purchaserData.getTotalAmountZBao() == null ? BigDecimal.ZERO : purchaserData.getTotalAmountZBao());
                accountIntDto.setAvailableAmountBao(purchaserData.getAvailableAmountZBao() == null ? BigDecimal.ZERO : purchaserData.getAvailableAmountZBao());
                accountIntDto.setFreezeAmountBao(purchaserData.getFreezeAmountZBao() == null ? BigDecimal.ZERO : purchaserData.getFreezeAmountZBao());
                accountIntDto.setApplyTime(sellerInfo.getCreateTime().getTime());
                accountIntDto.setIsShBind(true);
                accountIntDto.setDataJson(jsonArray);
                String zBaoAccount = accountZBaoUtil.createZBaoAccount(accountIntDto);
                sellerInfo.setisBind(true);
                //判断该用户是否已阅读
                sellerInfo.setisAgree(true);
                sellerInfo.setSevenBaoAccount(zBaoAccount);
                list.add(sellerInfo);
                sellerManager.updateAgrre(list);
                //第一次同意的时候给收货商调用主站接口设置他为“商户”
                try {
                    int code = 1;
                    String jsonParam = "{'UserId':'" + sellerInfo.getUid() + "','merchantType':'" + code + "'}";
                    mainStationManager.GetMainRest(MainStationConstant.URL_REST, MainStationConstant.RESULT_AUTHVERS, MainStationConstant.RESULT_TYPE, MainStationConstant.SET_USER_TOBE_MERCHANT,
                            jsonParam, MainStationConstant.RESULT_MD5, MainStationConstant.RESULT_FIELDS, MainStationConstant.RESULT_VERSION);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("请求主站设置卖家未商户失败{}：", sellerInfo.getUid());
                    throw new SystemException("请求主站设置卖家未商户失败{}：", sellerInfo.getUid());
                }
            }

            sellerManager.updatePurchaserData(userLoginAccount, purchaserData);
            return sellerInfo;
        }

        throw new SystemException(ResponseCodes.NotAvailable.getCode(), ResponseCodes.NotAvailable.getMessage());
    }

    /**
     * 请求主站查询该用户是否有过实名制认证
     */
    @Override
    public String getRealName(String uid) {
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

