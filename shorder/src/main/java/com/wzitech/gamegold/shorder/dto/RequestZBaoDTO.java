package com.wzitech.gamegold.shorder.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by chengXY on 2017/8/21.
 */
public class RequestZBaoDTO {
    private BigDecimal balance;

    private Date time;

    private String decStr;

    private String remark;

    private String loginAccount;

    private String phoneNum;

    private String handle;

    private String sign;

    private String outOrderId;

    private String orderId;

    private String userId;

    /**
     * 订单总金额
     */
    private BigDecimal orderMoney;

    /**
     * 实际完单金额
     */
    private BigDecimal realMoney;

    /**
     * 单金额信息（30,SG1712250000115;40,SG1712250000116）
     */
    private String transferList;

    public String getTransferList() {
        return transferList;
    }

    public void setTransferList(String transferList) {
        this.transferList = transferList;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public BigDecimal getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(BigDecimal realMoney) {
        this.realMoney = realMoney;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getDecStr() {
        return decStr;
    }

    public void setDecStr(String decStr) {
        this.decStr = decStr;
    }

    public Date getTime() {

        return time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public BigDecimal getBalance() {

        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
