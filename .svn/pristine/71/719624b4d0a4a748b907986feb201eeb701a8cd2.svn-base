/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryServicesServiceImpl
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.repository.impl
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 下午4:29:20
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.facade.frontend.service.repository.IQueryServicerService;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryServicerRequest;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryServicerResponse;
import com.wzitech.gamegold.order.business.IServiceSortManager;
import com.wzitech.gamegold.order.entity.ServiceSort;
import com.wzitech.gamegold.repository.business.IQueryServiceManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 订单页查询客服服务实现
 * 
 * @author HeJian
 * 
 */
@Service("QueryServicer")
@Path("queryservicer")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class QueryServicerServiceImpl extends AbstractBaseService implements
		IQueryServicerService {
	@Autowired
	IQueryServiceManager queryServiceManager;

	@Autowired
	IServicerOrderManager servicerOrderManager;

	@Autowired
	IUserInfoManager userInfoManager;

	@Autowired
	IServiceSortManager serviceSortManager;

	@Path("orderservice")
	@POST
	@Override
	public IServiceResponse queryOrderServicer(
			QueryServicerRequest queryServicerRequest,
			@Context HttpServletRequest request) {
		logger.debug("当前获取客服列表请求信息:{}", queryServicerRequest);
		// 初始化返回数据
		QueryServicerResponse response = new QueryServicerResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		response.setResponseStatus(responseStatus);

		try {
			// List<UserInfoEO> servicer =
//			 queryServiceManager.randomServicer(queryServicerRequest.getGameName(),
			// queryServicerRequest.getRegion(),
			// queryServicerRequest.getServer(),
			// queryServicerRequest.getGameRace(),
			// queryServicerRequest.getGoldCount(),
			// queryServicerRequest.getSize());
			// response.setUserInfoEOs(servicer);
			response.setUserInfoEOs(queryServices(queryServicerRequest, false));
			responseStatus.setStatus(ResponseCodes.Success.getCode(),
					ResponseCodes.Success.getMessage());
		} catch (SystemException ex) {
			// 捕获系统异常
			responseStatus.setStatus(ex.getErrorCode(),
					ex.getArgs()[0].toString());
			logger.error("获取客服列表发生异常:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
					ResponseCodes.UnKnownError.getMessage());
			logger.error("当前客服发生异常:{}", ex);
		}
		logger.debug("当前客服响应信息:{}", response);
		return response;
	}

	/**
	 * 查询指定游戏担保客服列表，待发货订单数量少的客服靠前排序
	 * @return
	 */
	@Path("queryServices")
	@GET
	@Override
	public IServiceResponse queryServices(@QueryParam("") String gameName) {
		logger.debug("当前获取客服列表请求信息:{}", gameName);
		// 初始化返回数据
		QueryServicerResponse response = new QueryServicerResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		response.setResponseStatus(responseStatus);

		try {
			/*List<ServiceSort> list = serviceSortManager.getSortedServiceList(UserType.AssureService.getCode(), gameName);
			if (!CollectionUtils.isEmpty(list)) {
				List<UserInfoEO> users = new ArrayList<UserInfoEO>();
				for (ServiceSort serviceSort : list) {
					users.add(serviceSort.getService());
				}
				response.setUserInfoEOs(users);
			}*/
			List<UserInfoEO> users = userInfoManager.queryAssureServiceByGameNoCache(gameName);
			response.setUserInfoEOs(users);
			responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
		} catch (SystemException ex) {
			// 捕获系统异常
			responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
			logger.error("获取客服列表发生异常:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
			logger.error("当前客服发生异常:{}", ex);
		}
		logger.debug("当前客服响应信息:{}", response);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.wzitech.gamegold.facade.frontend.service.repository.IQueryServicerService
	 * #
	 * queryApplyServicer(com.wzitech.gamegold.facade.frontend.service.repository
	 * .dto.QueryServicerRequest, javax.servlet.http.HttpServletRequest)
	 */
	@Path("applyservice")
	@POST
	@Override
	public IServiceResponse queryApplyServicer(
			QueryServicerRequest queryServicerRequest,
			@Context HttpServletRequest request) {
		logger.debug("当前获取客服列表请求信息:{}", queryServicerRequest);
		// 初始化返回数据
		QueryServicerResponse response = new QueryServicerResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		response.setResponseStatus(responseStatus);

		try {
			response.setUserInfoEOs(queryServices(queryServicerRequest, false));
			responseStatus.setStatus(ResponseCodes.Success.getCode(),
					ResponseCodes.Success.getMessage());
		} catch (SystemException ex) {
			// 捕获系统异常
			responseStatus.setStatus(ex.getErrorCode(),
					ex.getArgs()[0].toString());
			logger.error("获取客服列表发生异常:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
					ResponseCodes.UnKnownError.getMessage());
			logger.error("当前客服发生异常:{}", ex);
		}
		logger.debug("当前客服响应信息:{}", response);
		return response;
	}

	private List<UserInfoEO> queryServices(
			QueryServicerRequest queryServicerRequest, boolean random) {
		List<String> servicers = null;
		if (queryServicerRequest.getServicerType() == UserType.AssureService.getCode()) {
			servicers = servicerOrderManager.sortServicer();
		} else if (queryServicerRequest.getServicerType() == UserType.EnterService.getCode()){
			servicers = servicerOrderManager.sortEnterServicer();
		}

		if (servicers == null || servicers.size() == 0) {
			return null;
		}

//		List<Long> servicerIds = new ArrayList<Long>();
//		for (int i = 0; i < servicers.size(); i++) {
//			servicerIds.add(Long.valueOf(servicers.get(i)));
//		}

		List<UserInfoEO> userList = new ArrayList<UserInfoEO>();
		for (int i=0; i<servicers.size(); i++) {
			UserInfoEO userInfoEO = userInfoManager.findDBUserById(servicers.get(i));
			if(userInfoEO == null || (userInfoEO.getIsDeleted() != null && userInfoEO.getIsDeleted() == true)
					|| (userInfoEO.getUserType() != queryServicerRequest.getServicerType())){
				continue;
			}
			userList.add(userInfoEO);
		}

		if (random) {
			// list随机排序
			Collections.shuffle(userList);
		
			// 返回客服数默认为4
			int size = queryServicerRequest.getSize();
			if (size == 0) {
				size = 4;
			}
			if (size >= userList.size()) {
				size = userList.size();
			}
			return userList = userList.subList(0, size);
		}
		else{
			return userList;
		}
	}
}
