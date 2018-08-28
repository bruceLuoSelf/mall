/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryGoodsResponse
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.goods.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-1-15
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-15 下午1:47:15
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.goods.dto;

import java.util.List;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;

/**
 * 根据条件分页查询商品请求DTO响应
 * @author HeJian
 *
 */
public class QueryGoodsResponse extends AbstractServiceResponse{
	
	/**
	 * 商品分页实体集合
	 */
	private GenericPage<GoodsInfo> goodsInfos;
	
	/**
	 * 栏目商品列表
	 */
	private List<GoodsInfo> goodsList;
	
	/**
	 * 商品实体
	 */
	private GoodsInfo goodsInfo;

    /**
     * 安心买热卖商品配置
     */
    private HotRecommendConfig hotRecommendConfig;

	/**
	 * @return the goodsInfos
	 */
	public GenericPage<GoodsInfo> getGoodsInfos() {
		return goodsInfos;
	}

	/**
	 * @param goodsInfos the goodsInfos to set
	 */
	public void setGoodsInfos(GenericPage<GoodsInfo> goodsInfos) {
		this.goodsInfos = goodsInfos;
	}

	/**
	 * @return the goodsList
	 */
	public List<GoodsInfo> getGoodsList() {
		return goodsList;
	}

	/**
	 * @param goodsList the goodsList to set
	 */
	public void setGoodsList(List<GoodsInfo> goodsList) {
		this.goodsList = goodsList;
	}

	/**
	 * @return the goodsInfo
	 */
	public GoodsInfo getGoodsInfo() {
		return goodsInfo;
	}

	/**
	 * @param goodsInfo the goodsInfo to set
	 */
	public void setGoodsInfo(GoodsInfo goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

    public HotRecommendConfig getHotRecommendConfig() {
        return hotRecommendConfig;
    }

    public void setHotRecommendConfig(HotRecommendConfig hotRecommendConfig) {
        this.hotRecommendConfig = hotRecommendConfig;
    }
}
