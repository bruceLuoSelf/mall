/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TransferResponse
 *	包	名：		com.wzitech.gamegold.common.paymgmt.dto
 *	项目名称：	    gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	    2014年3月1日
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014年3月1日 下午6:48:33
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.paymgmt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wzitech.gamegold.common.paymgmt.dto.jsondeserial.ResultJsonDeserializer;

/**
 * 转账返回结果
 * @author SunChegnfei
 *
 */
public class BatchTransferResponse {
	/**
	 * 返回结果
	 */
    @JsonDeserialize(using = ResultJsonDeserializer.class)
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
	 * 转账成功的单号列表
	 */
	private String transferNumList;

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
	 * @return the transferNumList
	 */
	public String getTransferNumList() {
		return transferNumList;
	}

	/**
	 * @param transferNumList the transferNumList to set
	 */
	@JsonProperty("TransferNumList") 
	public void setTransferNumList(String transferNumList) {
		this.transferNumList = transferNumList;
	}
	
}
