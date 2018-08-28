/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		RefundResponse
 *	包	名：		com.wzitech.gamegold.common.paymgmt.dto
 *	项目名称：	    gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	    2014年3月2日
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014年3月2日 下午8:47:28
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.paymgmt.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 退款返回
 * @author SunChegnfei
 *
 */
public class RefundResponse {
	/**
	 * 返回结果
	 */
	private boolean result;
	
	/**
	 * 返回信息
	 */
	private String message;
	
	/**
	 * 订单id
	 */
	private String orderId;
	
	/**
	 * 操作资金总额
	 */
	private BigDecimal requestSum;

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
	
}
