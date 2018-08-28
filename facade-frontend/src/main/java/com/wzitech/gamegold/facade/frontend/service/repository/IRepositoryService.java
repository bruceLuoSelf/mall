/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IRepositoryService
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.repository
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 下午3:39:00
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.AddRepositoryRequest;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryLowestPriceRequest;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.SellerRepositoryRequest;

/**
 * 库存服务接口
 * @author HeJian
 *
 */
public interface IRepositoryService {
	/**
	 * 添加库存
	 * @param addRepositoryRequest
	 * @param request
	 * @return
	 */
	IServiceResponse addRepository(AddRepositoryRequest addRepositoryRequest, HttpServletRequest request);
	
	/**
	 * 上传文件
	 * @param file
	 * @param request
	 * @return
	 */
	IServiceResponse upLoadGoods(byte[] file, String serviceId, HttpServletRequest request);
	
	
	IServiceResponse queryLowestPrice(QueryLowestPriceRequest queryLowestPriceRequest, HttpServletRequest request);

	/**
	 * 查询店铺最低价列表
	 * @param sellerRepositoryRequest
	 * @param request
	 * @return
	 */
	IServiceResponse querySellerGoodslist(SellerRepositoryRequest sellerRepositoryRequest, HttpServletRequest request);

	/**
	 * 查询店铺最低价列表笔数
	 * @param sellerRepositoryRequest
	 * @param request
	 * @return
	 */
	IServiceResponse countSellerGoodslist(SellerRepositoryRequest sellerRepositoryRequest, @Context HttpServletRequest request);

//	/**
//	 * 店铺卖家平均价查询
//	 * @param sellerRepositoryRequest
//	 * @param request
//	 * @return
//	 */
//	 IServiceResponse querySellerAvgPriceRepository(@QueryParam("")SellerRepositoryRequest sellerRepositoryRequest, @Context HttpServletRequest request);
}
