/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TradePlaceServiceImpl
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.impl
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		Wengwei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. Wengwei 创建于 2014-2-21 下午4:56:37
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.order.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.order.ITradePlaceService;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QueryTradePlaceRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QueryTradePlaceResponse;
import com.wzitech.gamegold.order.business.IGameConfigManager;
import com.wzitech.gamegold.order.entity.GameConfigEO;

/**
 * @author Wengwei
 * 
 */
@Service("TradePlaceService")
@Path("querytradeplace")
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public class TradePlaceServiceImpl extends AbstractBaseService implements ITradePlaceService {
	@Autowired
	IGameConfigManager tradePlaceManager;
	
	@Path("queryplace")
	@POST
	@Override
	public IServiceResponse queryTradePlaceByGameName(
			QueryTradePlaceRequest queryTradePlacerequest,
			@Context HttpServletRequest request) {
		logger.debug("当前查询游戏交易地点:{}", queryTradePlacerequest);
		// 初始化返回数据
		QueryTradePlaceResponse response = new QueryTradePlaceResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		response.setResponseStatus(responseStatus);
		try {
			// 获取当前用户
//			GameUserInfo userInfo = CurrentUserSession.getUser();
//			if (userInfo == null) {
//				responseStatus.setStatus(
//						ResponseCodes.InvalidAuthkey.getCode(),
//						ResponseCodes.InvalidAuthkey.getMessage());
//				return response;
//			}
			GameConfigEO place = tradePlaceManager.selectGameConfig(queryTradePlacerequest.getGameName(),queryTradePlacerequest.getGoodsTypeName());
			response.setPlace(place);		
			responseStatus.setStatus(ResponseCodes.Success.getCode(),
					ResponseCodes.Success.getMessage());
		} catch (SystemException ex) {
			// 捕获系统异常
			responseStatus.setStatus(ex.getErrorCode(),
					ex.getArgs()[0].toString());
			logger.error("当前查询游戏交易地点:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(),
					ResponseCodes.UnKnownError.getMessage());
			logger.error("当前查询游戏交易地点发生未知异常:{}", ex);
		}
		logger.debug("当前查询游戏交易地点响应信息:{}", response);
		return response;
	}

}
