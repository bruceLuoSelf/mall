/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryOrderStateResponse
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午8:12:37
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 获取订单状态响应
 * 
 * @author HeJian
 * 
 */
@XmlRootElement(name = "Result")
public class QueryOrderStateResponse {

	private String msg;

	private boolean status;

	private String orderStatus;
	
	private String yxbMall;

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	@XmlElement(name = "Msg")
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	@XmlElement(name = "Status")
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus
	 *            the orderStatus to set
	 */
	@XmlElement(name = "OrderStatus")
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	/**
	 * @return the yxbMall
	 */
	@XmlElement(name = "YXBMALL")
	public String getYxbMall() {
		return yxbMall;
	}

	/**
	 * @param yxbMall
	 *            the yxbMall to set
	 */
	public void setYxbMall(String yxbMall) {
		this.yxbMall = yxbMall;
	}

}
