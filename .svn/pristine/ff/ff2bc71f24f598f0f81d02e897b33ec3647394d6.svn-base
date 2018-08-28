package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.paymgmt.dto.RefundResponse;
import com.wzitech.gamegold.shorder.business.IFundDetailManager;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.shorder.business.IFundPrivateManager;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.dao.IPayOrderDao;
import com.wzitech.gamegold.shorder.dao.IPurchaserDataDao;
import com.wzitech.gamegold.shorder.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by 汪俊杰 on 2018/3/8.
 */
@Component
public class FundManagerPrivateImpl implements IFundPrivateManager {
    @Autowired
    protected IPayOrderManager payOrderManager;
    @Autowired
    protected IPayOrderDao payOrderDao;
    @Autowired
    protected IPurchaserDataDao purchaserDataDao;
    @Autowired
    protected IFundDetailManager fundDetailManager;

    @Override
    public void refund(RefundOrder refundOrder) {
        // 修改当前支付单数据
        PayOrder payOrder = payOrderManager.queryByOrderId(refundOrder.getPayOrderId(), true);

        // 支付单余额
        BigDecimal balance = payOrder.getBalance();
        balance = balance.subtract(refundOrder.getRefundAmount()).setScale(2, RoundingMode.DOWN);

        if (BigDecimal.ZERO.compareTo(balance) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        payOrder.setBalance(balance);
        payOrder.setStatus(PayOrder.REFUNDED);
        payOrder.setLastUpdateTime(new Date());
        payOrderDao.update(payOrder);

        // 获取采购商数据
        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate(payOrder.getAccount());
        if (purchaserData == null)
            throw new SystemException(ResponseCodes.NotFindPurchaser.getCode(),
                    ResponseCodes.NotFindPurchaser.getMessage());

        // 总金额
        BigDecimal totalAmount = purchaserData.getTotalAmount();
        totalAmount = totalAmount.subtract(refundOrder.getRefundAmount()).setScale(2, RoundingMode.DOWN);
        purchaserData.setTotalAmount(totalAmount);

        if (BigDecimal.ZERO.compareTo(totalAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 冻结金额
        BigDecimal freezeAmount = purchaserData.getFreezeAmount();
        freezeAmount = freezeAmount.subtract(refundOrder.getRefundAmount()).setScale(2, RoundingMode.DOWN);
        purchaserData.setFreezeAmount(freezeAmount);

        if (BigDecimal.ZERO.compareTo(freezeAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 可用金额
        BigDecimal availableAmount = purchaserData.getTotalAmount().subtract(freezeAmount);
        availableAmount = availableAmount.setScale(2, RoundingMode.DOWN);
        purchaserData.setAvailableAmount(availableAmount);

        if (BigDecimal.ZERO.compareTo(availableAmount) == 1) {
            throw new SystemException(ResponseCodes.FundIsNotEnough.getCode(),
                    ResponseCodes.FundIsNotEnough.getMessage());
        }

        // 更新采购商数据
        purchaserDataDao.update(purchaserData);

        // 写资金明细记录
        String log = String.format("【退款】退款订单号:%s,退款金额:%s,当前总金额:%s,冻结金额:%s,可用金额:%s",
                refundOrder.getOrderId(), refundOrder.getRefundAmount(), purchaserData.getTotalAmount(),
                purchaserData.getFreezeAmount(), purchaserData.getAvailableAmount());

        FundDetail fundDetail = new FundDetail();
        fundDetail.setBuyerAccount(payOrder.getAccount());
        fundDetail.setType(FundType.RELEASE_FREEZE.getCode());
        fundDetail.setRefundOrderId(refundOrder.getOrderId());
        fundDetail.setAmount(refundOrder.getRefundAmount());
        fundDetail.setLog(log);
        fundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(fundDetail);

        // 创建资金明细
        String logRefund = String.format("【退款】已退款给收货方，退款单号：%s，退款金额：%s", refundOrder.getOrderId(), refundOrder.getRefundAmount());
        FundDetail refundDetail = new FundDetail();
        refundDetail.setBuyerAccount(payOrder.getAccount());
        refundDetail.setType(FundType.REFUND.getCode());
        refundDetail.setPayOrderId(refundOrder.getPayOrderId());
        refundDetail.setRefundOrderId(refundOrder.getOrderId());
        refundDetail.setAmount(refundOrder.getRefundAmount());
        refundDetail.setLog(logRefund);
        refundDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
        fundDetailManager.save(refundDetail);
    }
}
