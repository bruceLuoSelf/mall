/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IComplaintService
 *	包	名：		com.wzitech.gamegold.facade.service.complaint
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-25
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-25 下午4:18:23
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.complaint;

import javax.servlet.http.HttpServletRequest;

import com.wzitech.gamegold.facade.service.complaint.dto.QueryBuyerListRequest;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryBuyerListResponse;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryInfoByOrderIdRequest;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryInfoByOrderIdResponse;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryOrdersByUidRequest;
import com.wzitech.gamegold.facade.service.complaint.dto.QueryOrdersByUidResponse;

/**
 * 投诉服务接口
 * @author HeJian
 *
 */
public interface IComplaintService {
	/**
	 * 获取买家订单列表
	 * @param queryBuyerListRequest
	 * @param request
	 * @return
	 */
	QueryBuyerListResponse queryBuyerList(QueryBuyerListRequest queryBuyerListRequest, HttpServletRequest request);
	
	/**
	 * 根据订单号查询代练组信息
	 * @param queryInfoByOrderId
	 * @param request
	 * @return
	 */
	QueryInfoByOrderIdResponse queryInfoByOrderId(QueryInfoByOrderIdRequest queryInfoByOrderIdRequest, HttpServletRequest request);
	
	/**
	 * 根据5173账号获取订单集合
	 * @param queryOrderByUidRequest
	 * @param request
	 * @return
	 */
	QueryOrdersByUidResponse queryOrdersByUid(QueryOrdersByUidRequest queryOrdersByUidRequest, HttpServletRequest request);
	
	/**
	 * 根据订单Id，返回订单状态，0无效，1有效
	 * @param orderId
	 * @return
	 */
	String queryOrderState(String orderId);
}
