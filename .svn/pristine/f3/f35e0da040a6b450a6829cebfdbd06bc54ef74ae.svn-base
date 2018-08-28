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
package com.wzitech.gamegold.facade.frontend.service.order.dto;

import java.util.List;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 查询订单请求DTO响应
 * @author LuChangkai
 *
 */
public class QueryOrderResponse extends AbstractServiceResponse{
	
	/**
	 * 返回实体类集合
	 */
	private GenericPage<OrderInfoEO> orders;
	
	/**
	 * 订单信息
	 */
	private OrderInfoEO orderInfoEO;
	
	/**
	 * 服务器当前时间
	 */
	private long currentDate;
	
	/**
	 * 返回最新5笔订单
	 */
	private List<OrderInfoEO> orderInfoEOs;

	/**
	 * @return the currentDate
	 */
	public long getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(long currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * @return the orders
	 */
	public GenericPage<OrderInfoEO> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(GenericPage<OrderInfoEO> orders) {
		this.orders = orders;
	}

	/**
	 * @return the orderInfoEO
	 */
	public OrderInfoEO getOrderInfoEO() {
		return orderInfoEO;
	}

	/**
	 * @param orderInfoEO the orderInfoEO to set
	 */
	public void setOrderInfoEO(OrderInfoEO orderInfoEO) {
		this.orderInfoEO = orderInfoEO;
	}

	/**
	 * @return the orderInfoEOs
	 */
	public List<OrderInfoEO> getOrderInfoEOs() {
		return orderInfoEOs;
	}

	/**
	 * @param orderInfoEOs the orderInfoEOs to set
	 */
	public void setOrderInfoEOs(List<OrderInfoEO> orderInfoEOs) {
		this.orderInfoEOs = orderInfoEOs;
	}
	
}
