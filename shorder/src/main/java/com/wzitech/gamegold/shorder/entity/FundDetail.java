package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 资金明细
 */
public class FundDetail extends BaseEntity{
    /**
     * 采购商5173账号
     */
    private String buyerAccount;
    /**
     * 类型
     * <li>1：充值</li>
     * <li>2：付款</li>
     * <li>3：退款</li>
     * <li>4：冻结资金</li>
     * <li>5：解冻资金</li>
     * <li>6：转账</li>
     */
    private Integer type;
    /**
     * 关联的充值单号
     */
    private String payOrderId;
    /**
     * 关联的付款明细单号
     */
    private String payDetailOrderId;
    /**
     * 关联的退款订单号
     */
    private String refundOrderId;
    /**
     * 关联的出货订单号
     */
    private String deliveryOrderId;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 日志详情
     */
    private String log;
    /**
     * 操作时间
     */
    private Date createTime;

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId;
    }

    public String getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(String deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPayDetailOrderId() {
        return payDetailOrderId;
    }

    public void setPayDetailOrderId(String payDetailOrderId) {
        this.payDetailOrderId = payDetailOrderId;
    }
}
