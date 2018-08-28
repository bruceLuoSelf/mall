package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.common.message.IMobileMsgManager;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.business.IShrobotRefundOrderManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IRefundOrderManager;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.RefundOrder;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by 汪俊杰 on 2017/6/21.
 */
@Component
public class ShrobotRefundOrderManagerImpl extends AbstractBusinessObject implements IShrobotRefundOrderManager {
    @Autowired
    ISellerManager sellerManager;

    @Autowired
    IRefundOrderManager refundOrderManager;

    @Autowired
    IMobileMsgManager mobileMsgManager;

    @Autowired
    ISystemConfigManager systemConfigManager;

    /**
     * 自动退款超过三个月的充值订单
     *
     * @param payOrderId
     * @param loginAccount
     */
    @Override
    @Transactional
    public void autoRefundTimeoutPayOrder(String payOrderId, String loginAccount, int day) {
        RefundOrder refundOrder = refundOrderManager.applyRefund(payOrderId, "支付单超过" + day + "天，自动原路退款", loginAccount);
        if (refundOrder != null) {
            refundOrder.setStatus(RefundOrder.Refund);
            refundOrder.setAuditor("自动审核");
            refundOrder.setAuditTime(new Date());
            refundOrderManager.updateRefundOrder(refundOrder);

            try {
                SellerInfo sellerInfo = sellerManager.querySellerInfo(loginAccount);
                if (sellerInfo != null && StringUtils.isNotBlank(sellerInfo.getPhoneNumber())) {
                    //是否发短信
                    SystemConfig enableSendMsg = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.SEND_MSG.getKey());
                    if (enableSendMsg != null && enableSendMsg.getEnabled()) {
                        //发短信
                        sendMessage(sellerInfo.getPhoneNumber(), String.format("尊敬的5173收货商:您好，由于您创建的支付单%s金额%s元已超过3个月（支付单最长使用时效3个月），现需要对您该笔支付单进行原路退款操作，请您注意查收，给您带来不便深表歉意！", payOrderId, refundOrder.getRefundAmount()));
                    }
                }
            } catch (Exception ex) {
                logger.info("自动退款超过三个月的充值订单发生错误，{},{}", payOrderId, ex);
            }
        }
    }

    /**
     * 自动退款超过三个月的充值订单(禁止调用)
     *
     * @param payOrderId
     * @param loginAccount
     */
    @Override
    @Transactional
    public void autoRefundTimeoutPayOrderPrivate(String payOrderId, String loginAccount, int day) {
        RefundOrder refundOrder = refundOrderManager.applyRefund(payOrderId, "支付单超过" + day + "天，自动原路退款", loginAccount);
        if (refundOrder != null) {
            refundOrder.setStatus(RefundOrder.Refund);
            refundOrder.setAuditor("自动审核");
            refundOrder.setAuditTime(new Date());
            refundOrderManager.updateRefundOrderPrivate(refundOrder);
        }
    }

    /**
     * 送短信
     *
     * @param mobilePhone
     * @return
     * @ 发param content
     */
    private boolean sendMessage(String mobilePhone, String content) {
        try {
            mobileMsgManager.sendMessage(mobilePhone, content);
        } catch (Exception e) {
            logger.error("发送短信出错了", e);
            return false;
        }
        return true;
    }
}
