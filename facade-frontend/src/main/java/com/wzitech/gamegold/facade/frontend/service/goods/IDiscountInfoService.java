/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IDiscountInfoService
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.goods
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 上午10:40:29
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.goods;

import javax.servlet.http.HttpServletRequest;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.goods.dto.QueryDiscountRequest;

/**
 * 商品对应折扣服务接口
 * @author HeJian
 *
 */
public interface IDiscountInfoService {
	/**
	 * 查询折扣
	 * @param queryDiscountRequest
	 * @param request
	 * @return
	 */
	IServiceResponse queryDiscount(QueryDiscountRequest queryDiscountRequest, HttpServletRequest request);
}
