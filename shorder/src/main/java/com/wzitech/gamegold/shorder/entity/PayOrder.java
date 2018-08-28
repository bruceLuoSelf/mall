package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收货商支付单
 * @author yemq
 */
public class PayOrder extends BaseEntity {
    /**
     * 待支付
     */
    public static final int WAIT_PAYMENT = 0;
    /**
     * 已支付
     */
    public static final int PAID = 1;
    /**
     * 申请退款中
     */
    public static final int APPLY_REFUND = 2;
    /**
     * 已退款
     */
    public static final int REFUNDED = 3;

    /**
     * 2017/08/16 wangmin	ZW_C_JB_00021 商城资金改造
     * 自动充值单
     * 7bao 充值
     */
    public static final int Z7BAOPAID = 4;
    /**
     * 充值单号
     */
    private String orderId;
    /**
     * 收货方5173UID
     */
    private String uid;
    /**
     * 收货方5173账号
     */
    private String account;
    /**
     * 充值金额
     */
    private BigDecimal amount;
    /**
     * 已用金额
     */
    private BigDecimal usedAmount;
    /**
     * 剩余金额
     */
    private BigDecimal balance;
    /**
     * 类型
     * <li>0：待支付</li>
     * <li>1：已支付</li>
     * <li>2：申请退款中</li>
     * <li>3：已退款</li>
     * <li>4：自动充值单</li>
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 付款时间
     */
    private Date payTime;
    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 对应7bao的订单号
     */
    private String zbaoRelatedOrderId;

    public String getZbaoRelatedOrderId() {
        return zbaoRelatedOrderId;
    }

    public void setZbaoRelatedOrderId(String zbaoRelatedOrderId) {
        this.zbaoRelatedOrderId = zbaoRelatedOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(BigDecimal usedAmount) {
        this.usedAmount = usedAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
