package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款明细
 */
public class PayDetail extends BaseEntity {
    /**
     * 待付款
     */
    public static final Integer WAIT_PAID = 1;
    /**
     * 已付款
     */
    public static final Integer PAID = 2;
    /**
     * 付款明细订单号
     */
    private String orderId;

    /**
     * 关联的支付订单号
     */
    private String payOrderId;
    /**
     * 关联的出货订单号
     */
    private String chOrderId;
    /**
     * 付款金额
     */
    private BigDecimal amount;
    /**
     * 状态
     * <li>1：待付款</li>
     * <li>2：已付款</li>
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


    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getChOrderId() {
        return chOrderId;
    }

    public void setChOrderId(String chOrderId) {
        this.chOrderId = chOrderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
