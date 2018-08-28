/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IQueryServicesService
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.repository
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 下午4:29:01
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository;

import javax.servlet.http.HttpServletRequest;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryServicerRequest;

/**
 * 订单页查询客服服务
 * @author HeJian
 *
 */
public interface IQueryServicerService {
	/**
	 * 订单页查询客服
	 * @param queryServicerRequest
	 * @param request
	 * @return
	 */
	IServiceResponse queryOrderServicer(QueryServicerRequest queryServicerRequest, HttpServletRequest request);
	
	/**
	 * 申请卖家页查询客服(按忙碌程度)
	 * @param queryServicerRequest
	 * @param request
	 * @return
	 */
	IServiceResponse queryApplyServicer(QueryServicerRequest queryServicerRequest, HttpServletRequest request);

	/**
	 * 查询指定游戏担保客服列表，待发货订单数量少的客服靠前排序
	 * @return
	 */
	public IServiceResponse queryServices(String gameName);
}
