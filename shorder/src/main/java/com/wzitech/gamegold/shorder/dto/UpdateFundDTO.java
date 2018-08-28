package com.wzitech.gamegold.shorder.dto;

import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/8/23.
 */
public class UpdateFundDTO {
    /**
     * 5173登录账号
     * */
    private String loginAccount;

    /**
     * uid
     * */
    private String uid;

    /**
     * 冻结/解冻金额
     * */
    private BigDecimal freezeFund;

    /**
     * 1.冻结 2.解冻
     * */
    private Integer yesOrNo;

    /**
     * 签名
     * */
    private String sign;

    /**
     * 订单号
     * */
    private String orderId;
    /**
     * 总金额
     * */
    private BigDecimal totalAmount;
    /**
     * 可用金额
     * */
    private BigDecimal availableAmount;
    /**
     * 冻结金额
     * */
    private BigDecimal freezeAmount;

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(Integer yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getFreezeFund() {
        return freezeFund;
    }

    public void setFreezeFund(BigDecimal freezeFund) {
        this.freezeFund = freezeFund;
    }
}
