/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IOrderService
 *	包	名：		com.wzitech.gamegold.facade.service.order
 *	项目名称：	gamegold-facade
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-14 下午4:52:59
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order;

import javax.servlet.http.HttpServletRequest;

import com.wzitech.gamegold.facade.service.order.dto.CancelOrderRequest;
import com.wzitech.gamegold.facade.service.order.dto.CancelOrderResponse;
import com.wzitech.gamegold.facade.service.order.dto.QueryOrderInfoRequest;
import com.wzitech.gamegold.facade.service.order.dto.QueryOrderListRequest;
import com.wzitech.gamegold.facade.service.order.dto.QueryOrderListResponse;
import com.wzitech.gamegold.facade.service.order.dto.QueryOrderStateRequest;
import com.wzitech.gamegold.facade.service.order.dto.QueryOrderStateResponse;
import com.wzitech.gamegold.facade.service.order.dto.TransferOrderRequest;
import com.wzitech.gamegold.facade.service.order.dto.TransferOrderResponse;

/**
 * 订单管理服务端接口
 * @author LuChangkai
 *
 */
public interface IOrderService {
	/**
	 * 查询订单列表
	 * @param queryOrderListRequest
	 * @param request
	 * @return
	 */
	QueryOrderListResponse queryOrderList(QueryOrderListRequest queryOrderListRequest, HttpServletRequest request);
	
	/**
	 * 订单移交
	 * @param transferOrderRequest
	 * @param request
	 * @return
	 */
	TransferOrderResponse transferOrder(TransferOrderRequest transferOrderRequest, HttpServletRequest request);
	
	/**
	 * 取消订单
	 * @param cancelOrderRequest
	 * @param request
	 * @return
	 */
	CancelOrderResponse cancelOrder(CancelOrderRequest cancelOrderRequest, HttpServletRequest request);

    /**
     * 取消订单原因
     * @return
     */
    String cancelOrderReason(HttpServletRequest request);
	
	/**
	 * 查询订单状态
	 * @param queryOrderStateRequest
	 * @param request
	 * @return
	 */
	QueryOrderStateResponse queryOrderState(QueryOrderStateRequest queryOrderStateRequest, HttpServletRequest request);
	
	
	/**
	 * 查询订单信息
	 * @param queryOrderInfoRequest
	 * @param request
	 * @return
	 */
	String queryOrderInfo(QueryOrderInfoRequest queryOrderInfoRequest, HttpServletRequest request);
	
}
