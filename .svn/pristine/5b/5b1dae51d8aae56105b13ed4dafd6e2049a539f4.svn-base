package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.*;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 资金管理
 */
public interface IFundManager {
    /**
     * 冻结/解冻资金类型-出货单
     */
    int FREEZE_BY_DELIVERY_ORDER = 1;
    /**
     * 冻结/解冻资金类型-退款申请
     */
    int FREEZE_BY_REFUND_ORDER = 2;

    /**
     * 处理充值成功通知
     *
     * @param orderId 订单号
     * @param amount  支付金额
     */
    void processRechargeSuccessNotify(String orderId, BigDecimal amount);

    /**
     * 处理充值成功通知 7bao
     *
     * @param orderId 订单号
     * @param amount  支付金额
     */
    // void processRechargeSuccess7Bao(String orderId, BigDecimal amount);

    /**
     * 补单
     *
     * @param autoDeleteTime
     */
    void autoConfirmationPayTimeoutOrder(Integer autoDeleteTime);

    /**
     * 人工补单
     *
     * @param orderId
     */
    void manualPayShOrder(String orderId);

    /**
     * 冻结采购商资金
     *
     * @param freezeType 资金冻结类型
     * @param account    采购商账号
     * @param orderId    订单号
     * @param amount     冻结金额
     * @return
     */
    PurchaserData freezeFund(int freezeType, String account, String orderId, BigDecimal amount);

    /**
     * 冻结资金新资金
     */
    PurchaserData freezeFundZBao(int freezeType, String account, String uid, String orderId, BigDecimal amount);

    /**
     * 解冻采购商资金
     *
     * @param freezeType 资金解冻类型
     * @param account    采购商账号
     * @param orderId    订单号
     * @param amount     解冻金额
     * @return
     */
    PurchaserData releaseFreezeFund(int freezeType, String account, String orderId, BigDecimal amount);

    /**
     * 新资金：解冻采购商资金
     */
    PurchaserData releaseFreezeFundZBao(int freezeType, DeliveryOrder deliveryOrder, BigDecimal balance);

    /**
     * 新资金：解冻采购商资金
     */
    PurchaserData releaseFreezeFund(int freezeType, DeliveryOrder deliveryOrder, BigDecimal balance);

    /**
     * 资金结算
     *
     * @param orderId      订单号
     * @param amount       订单金额
     * @param realAmount   实际结算金额
     * @param buyerAccount 采购商账号
     */
    boolean settlement(String orderId, BigDecimal amount, BigDecimal realAmount, String buyerAccount);

    /**
     * 主站回调回来，给此出货单的卖家加钱
     */
    boolean plusAmount(DeliveryOrder deliveryOrder);

    /**
     * 新资金结算
     *
     * @param deliveryOrder 出货单
     */
    boolean newSettlement(DeliveryOrder deliveryOrder) throws IOException;

    /**
     * 调用资金接口转账给出货方
     *
     * @param deliveryOrderId 出货单号
     * @param payOrderId      付款单号
     * @param buyerId         采购商ID
     * @param buyerAccount    采购商账号
     * @param sellerId        获得转账的用户ID
     * @param sellerName      获得转账的用户名
     * @param totalFee        转账总金额
     * @param payDetail       付款明细
     */
    boolean transfer(String deliveryOrderId, String payOrderId, String buyerId, String buyerAccount,
                     String sellerId, String sellerName,
                     BigDecimal totalFee, PayDetail payDetail);

    boolean transfer(String deliveryOrderId, String payOrderId, String buyerId, String buyerAccount,
                     String sellerId, String sellerName,
                     BigDecimal totalFee, PayOrder payOrder);

    /**
     * 冻结申请退款资金
     *
     * @param refundOrder 退款订单
     */
    void freezeRefundFund(RefundOrder refundOrder);

    /**
     * 解冻申请退款资金
     *
     * @param refundOrder
     */
    void releaseFreezeRefundFund(RefundOrder refundOrder);

    /**
     * 退款给收货方
     *
     * @param refundOrder
     */
    void refund(RefundOrder refundOrder);

    boolean transferFor7Bao(DeliveryOrder deliveryOrder, String orderId, String payOrderId, String buyerUid, String buyerAccount,
                            String sellerUid, String sellerAccount, BigDecimal amount, PayDetail payDetail, int orderSubfix);

    void freezeAmountZBao(String account, String uid, BigDecimal amount, Integer yesOrNo, String url, String orderId);

    /**
     * 释放7宝资金，但不涉及商城收货资金
     *
     * @param deliveryOrder
     */
    void freezeZBao(DeliveryOrder deliveryOrder);
}
