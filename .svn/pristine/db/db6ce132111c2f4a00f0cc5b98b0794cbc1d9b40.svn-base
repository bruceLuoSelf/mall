/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryDetailResponse
 *	包	名：		com.wzitech.gamegold.common.paymgmt.dto
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-3-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-3-14 下午3:52:07
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.paymgmt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author SunChengfei
 *
 */
public class QueryDetailResponse {
	/**
	 * 提交状态0：初始，1：已提交，2：取消
	 */
	private int commitStatus;
	
	/**
	 * 转账状态(EarnStatus)：0：初始，1：成功，3：取消
	 */
	private int earnStatus;
	
	/**
	 * 说明
	 */
	private String message;

    /**
     * 明细创建时间
     */
    private String date;

    /**
     * 订单号
     */
    private String tradeNo;

	/**
	 * @return the commitStatus
	 */
	public int getCommitStatus() {
		return commitStatus;
	}

	/**
	 * @param commitStatus the commitStatus to set
	 */
	@JsonProperty("CommitStatus") 
	public void setCommitStatus(int commitStatus) {
		this.commitStatus = commitStatus;
	}

	/**
	 * @return the earnStatus
	 */
	public int getEarnStatus() {
		return earnStatus;
	}

	/**
	 * @param earnStatus the earnStatus to set
	 */
	@JsonProperty("EarnStatus") 
	public void setEarnStatus(int earnStatus) {
		this.earnStatus = earnStatus;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	@JsonProperty("Message")
	public void setMessage(String message) {
		this.message = message;
	}

    public String getDate() {
        return date;
    }

    @JsonProperty("Date")
    public void setDate(String date) {
        this.date = date;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    @JsonProperty("TradeNo")
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
