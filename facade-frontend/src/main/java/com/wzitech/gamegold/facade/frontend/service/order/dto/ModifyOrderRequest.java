/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ModifyOrderRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		LuChangkai
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. LuChangkai 创建于 2014-1-14 下午5:49:41
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.order.dto;

import java.math.BigDecimal;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 修改订单请求DTO
 * @author LuChangkai
 *
 */
public class ModifyOrderRequest extends AbstractServiceRequest{
	
	/**
	 * 订单Id
	 */
	private Long id;
	
	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 联系方式
	 */
	private String mobileNumber;
	
	/**
	 * QQ
	 */
	private String qq;
	
	/**
	 * 收货人姓名
	 */
	private String receiver;
	
	/**
	 * 总费用
	 */
	private BigDecimal totalPrice;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 是否延迟
	 */
	private Boolean isDelay;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the qq
	 */
	public String getQq() {
		return qq;
	}

	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the totalPrice
	 */
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * @return the orderState
	 */
	public Integer getOrderState() {
		return orderState;
	}

	/**
	 * @param orderState the orderState to set
	 */
	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	/**
	 * @return the isDelay
	 */
	public Boolean getIsDelay() {
		return isDelay;
	}

	/**
	 * @param isDelay the isDelay to set
	 */
	public void setIsDelay(Boolean isDelay) {
		this.isDelay = isDelay;
	}

}
