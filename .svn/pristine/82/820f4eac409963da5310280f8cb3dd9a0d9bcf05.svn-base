/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		CompensateResponse
 *	包	名：		com.wzitech.gamegold.common.paymgmt.dto
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-3-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-3-20 下午1:44:53
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.paymgmt.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 赔付返回
 * @author SunChengfei
 *
 */
public class CompensateResponse {
	/**
	 * 结果值
	 * true:message为空
	 * false:message错误信息
	 */
	private boolean result;
	
	/**
	 * 请求金额
	 */
	private BigDecimal requestSum;
	
	/**
	 * 返回信息
	 */
	private String message;
	
	/**
	 * 交易号
	 */
	private String orderId;

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	@JsonProperty("Result") 
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return the requestSum
	 */
	public BigDecimal getRequestSum() {
		return requestSum;
	}

	/**
	 * @param requestSum the requestSum to set
	 */
	@JsonProperty("RequestSum") 
	public void setRequestSum(BigDecimal requestSum) {
		this.requestSum = requestSum;
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

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	@JsonProperty("OrderId") 
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
