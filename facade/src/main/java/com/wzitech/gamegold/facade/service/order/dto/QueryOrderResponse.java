/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryOrderResponse
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		LuChangkai
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. LuChangkai 创建于 2014-1-14 下午6:11:39
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 查询订单请求DTO响应
 * @author LuChangkai
 *
 */
@XmlRootElement
public class QueryOrderResponse extends AbstractServiceResponse{
	
	/**
	 * 返回实体类集合
	 */
	private GenericPage<OrderInfoEO> orders;

	/**
	 * @return the orders
	 */
	@XmlElement(name = "orders")
	public GenericPage<OrderInfoEO> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(GenericPage<OrderInfoEO> orders) {
		this.orders = orders;
	}
}
