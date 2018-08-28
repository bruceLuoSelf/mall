/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IGoodsInfoService
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.goods
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-1-15
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-15 上午11:54:46
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.goods;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.goods.dto.QueryGoodsRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 商品服务端接口
 * @author HeJian
 *
 */
public interface IGoodsInfoService {
	/**
	 * 根据条件分页查询商品
	 * @param addGoodsRequest
	 * @param request
	 * @return
	 */
	IServiceResponse queryGoods(QueryGoodsRequest queryGoodsRequest, HttpServletRequest request);
	
	/**
	 * 条件查询栏目商品
	 * @param selectGoodsRequest
	 * @param request
	 * @return
	 */
	IServiceResponse selectGoods(QueryGoodsRequest selectGoodsRequest, HttpServletRequest request);
	
	/**
	 * 查询单个商品
	 * @param selectGoodsRequest
	 * @param request
	 * @return
	 */
	IServiceResponse querySingleGoods(QueryGoodsRequest selectGoodsRequest, HttpServletRequest request);
	/**
	 * 查询单价
	 * @param queryUnitPriceRequest
	 * @param request
	 * @return
	 */
	IServiceResponse queryUnitPrice(QueryGoodsRequest queryUnitPriceRequest,HttpServletRequest request);

	/**
	 * 根据游戏区服阵营，查询1条最低价商品
	 * @param queryGoodsRequest
	 * @param request
	 * @return
	 */
	IServiceResponse queryLowerPriceGoods(QueryGoodsRequest queryGoodsRequest,HttpServletRequest request);

    /**
     * 查询所有栏目商品
     * @param request
     * @return
     */
    IServiceResponse queryAllCategoryGoods(QueryGoodsRequest request);

    /**
     * 根据栏目查询单个商品信息
     * @param request
     * @return
     */
    IServiceResponse querySingleCategoryGoods(QueryGoodsRequest request);
}
