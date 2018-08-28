package com.wzitech.gamegold.common.paymgmt.dto;

import java.math.BigDecimal;

/**
 * 模    块：TransferUserInfo
 * 包    名：com.wzitech.gamegold.common.paymgmt.dto
 * 项目名称：dada
 * 作    者：Shawn
 * 创建时间：3/2/14 9:20 PM
 * 描    述：批量转账用户信息
 * 更新纪录：1. Shawn 创建于 3/2/14 9:20 PM
 */
public class TransferUserInfo {
    private String getUserId;

    private String getUserName;

    private BigDecimal transferFee;

    /**
     * 售得用户Id
     */
    public String getGetUserId() {
        return getUserId;
    }

    public void setGetUserId(String getUserId) {
        this.getUserId = getUserId;
    }

    /**
     * 售得用户名称
     */
    public String getGetUserName() {
        return getUserName;
    }

    public void setGetUserName(String getUserName) {
        this.getUserName = getUserName;
    }

    /**
     * 转账金额
     */
    public BigDecimal getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(BigDecimal transferFee) {
        this.transferFee = transferFee;
    }
}
