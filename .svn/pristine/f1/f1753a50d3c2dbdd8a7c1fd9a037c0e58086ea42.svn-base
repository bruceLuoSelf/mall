package com.wzitech.gamegold.common.paymgmt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wzitech.gamegold.common.paymgmt.dto.jsondeserial.ResultJsonDeserializer;

import java.math.BigDecimal;

/**
 * 付款返回结果
 * @author SunChegnfei
 *
 */
public class DirectPayTransferResponse {
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
	 * 向资金服务请求的资金操作总金额
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

	public BigDecimal getRequestSum() {
		return requestSum;
	}

	@JsonProperty("RequestSum")
	public void setRequestSum(BigDecimal requestSum) {
		this.requestSum = requestSum;
	}
}
