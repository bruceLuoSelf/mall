/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DiscountInfoServiceImpl
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.goods.impl
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 上午10:47:39
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.goods.impl;

import java.util.List;

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
import com.wzitech.gamegold.facade.frontend.service.goods.IDiscountInfoService;
import com.wzitech.gamegold.facade.frontend.service.goods.dto.QueryDiscountRequest;
import com.wzitech.gamegold.facade.frontend.service.goods.dto.QueryDiscountResponse;
import com.wzitech.gamegold.goods.business.IDiscountInfoManager;
import com.wzitech.gamegold.goods.entity.DiscountInfo;

/**
 * 商品对应折扣服务实现
 * @author HeJian
 *
 */
@Service("DiscountInfoService")
@Path("discount")
@Produces("application/json; charset=UTF-8")
@Consumes("application/json; charset=UTF-8")
public class DiscountInfoServiceImpl extends AbstractBaseService implements IDiscountInfoService{
	@Autowired
	IDiscountInfoManager discountInfoManager;
	
	@Path("querydiscount")
	@POST
	@Consumes("multipart/form-data")
	@Override
	public IServiceResponse queryDiscount(QueryDiscountRequest queryDiscountRequest,
			@Context HttpServletRequest request) {
		logger.debug("当前查询折扣:{}", queryDiscountRequest);
		// 初始化返回数据
		QueryDiscountResponse response = new QueryDiscountResponse();
		ResponseStatus responseStatus = new ResponseStatus();
		response.setResponseStatus(responseStatus);
		try {
			List<DiscountInfo> discountInfos = discountInfoManager.queryDiscountInfos(queryDiscountRequest.getGoodsId());
			response.setDiscountInfos(discountInfos);
			responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());	
		} catch (SystemException ex) {
			// 捕获系统异常
			responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
			logger.error("当前查询折扣发生异常:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			responseStatus.setStatus(
					ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
			logger.error("当前查询折扣发生未知异常:{}", ex);
		}
		logger.debug("当前查询折扣响应信息:{}", response);
		return response;
	}

}
