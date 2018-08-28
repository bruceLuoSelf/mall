package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算记录
 */
public class Settlement extends BaseEntity{
    /**
     * 等待付款
     */
    public static final int WAIT_PAYMENT = 1;
    /**
     * 已付款
     */
    public static final int PAID = 2;
    /**
     * 关联的出货单号
     */
    private String chOrderId;
    /**
     * 出货方5173账号
     */
    private String sellerAccount;
    /**
     * 出货方5173UID
     */
    private String sellerUid;
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

    public String getChOrderId() {
        return chOrderId;
    }

    public void setChOrderId(String chOrderId) {
        this.chOrderId = chOrderId;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getSellerUid() {
        return sellerUid;
    }

    public void setSellerUid(String sellerUid) {
        this.sellerUid = sellerUid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
