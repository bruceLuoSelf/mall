/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryOrderListResponse
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午8:16:40
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 获取订单信息响应
 * 
 * @author HeJian
 * 
 */
@XmlRootElement(name="Orders")
public class QueryOrderListResponse {
	/**
	 * 返回信息
	 */
	private List<OrderListItemDTO> list;
	
	/**
	 * 状态信息
	 */
	private String msg;

	/**
	 * 状态
	 */
	private boolean status;

	@XmlElement(name="Order")
	public List<OrderListItemDTO> getList() {
		return list;
	}

	public void setList(List<OrderListItemDTO> list) {
		this.list = list;
	} 
	
	/**
	 * @return the msg
	 */
	@XmlElement(name = "Msg")
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the status
	 */
	@XmlElement(name = "Status")
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

}
