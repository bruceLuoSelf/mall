package com.wzitech.gamegold.common.paymgmt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 汪俊杰 on 2018/1/23.
 */
public class VaQueryDetailResponse {
    /**
     * 说明code
     */
    private String code;

    /**
     * 说明
     */
    private String message;
    /**
     * 提交状态0：初始，1：已提交，2：取消
     */
    private int commitStatus;

    /**
     * 转账状态(EarnStatus)：0：初始，1：成功，3：取消
     */
    private int earnStatus;

    /**
     * 主站订单号
     */
    private String orderId;

    /**
     * 业务订单号
     */
    private String billId;

    /**
     * 创建时间
     */
    private String createdDate;

    /**
     * 修改时间
     */
    private String lastModified;

    /**
     * 用户uid
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 提现资金
     */
    private BigDecimal moneyRequest;

    public String getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("Message")
    public void setMessage(String message) {
        this.message = message;
    }

    public int getCommitStatus() {
        return commitStatus;
    }

    @JsonProperty("CommitStatus")
    public void setCommitStatus(int commitStatus) {
        this.commitStatus = commitStatus;
    }

    public int getEarnStatus() {
        return earnStatus;
    }

    @JsonProperty("EarnStatus")
    public void setEarnStatus(int earnStatus) {
        this.earnStatus = earnStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    @JsonProperty("OrderId")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBillId() {
        return billId;
    }

    @JsonProperty("BillId")
    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("CreatedDate")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModified() {
        return lastModified;
    }

    @JsonProperty("LastModified")
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getUserId() {
        return userId;
    }

    @JsonProperty("UserId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    @JsonProperty("UserName")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getMoneyRequest() {
        return moneyRequest;
    }

    @JsonProperty("MoneyRequest")
    public void setMoneyRequest(double moneyRequest) {
        this.moneyRequest = BigDecimal.valueOf(Math.abs(moneyRequest));
    }

    /**
     * 重载Entity类的toString()方法
     * 返回Entity类所有属性值
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
