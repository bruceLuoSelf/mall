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
package com.wzitech.gamegold.gamegold.app.service.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.game.entity.AnalysisUrlResponse;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;

import java.util.List;

/**
 * 根据条件分页查询商品请求DTO响应
 *
 */
public class QueryGoodsInfoResponse extends AbstractServiceResponse{
	
	/**
	 * 商品分页实体集合
	 */
	private GenericPage<GoodsDTO> goodsInfos;
	
	/**
	 * 栏目商品列表
	 */
	private List<GoodsDTO> goodsList;
	
	/**
	 * 商品实体
	 */
	private GoodsDTO goodsInfo;

	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 游戏区
	 */
	private String gameRegion;

	/**
	 * 游戏服
	 */
	private String gameServer;

	/**
	 * 游戏阵营
	 */
	private String gameRace;

	public String getGameName() {
		return gameName;
	}


	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameRegion() {
		return gameRegion;
	}


	public void setGameRegion(String gameRegion) {
		this.gameRegion = gameRegion;
	}

	public String getGameServer() {
		return gameServer;
	}


	public void setGameServer(String gameServer) {
		this.gameServer = gameServer;
	}

	public String getGameRace() {
		return gameRace;
	}


	public void setGameRace(String gameRace) {
		this.gameRace = gameRace;
	}

    /**
     * 安心买热卖商品配置
     */
    private HotRecommendConfig hotRecommendConfig;

	public GenericPage<GoodsDTO> getGoodsInfos() {
		return goodsInfos;
	}

	public void setGoodsInfos(GenericPage<GoodsDTO> goodsInfos) {
		this.goodsInfos = goodsInfos;
	}

	public List<GoodsDTO> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsDTO> goodsList) {
		this.goodsList = goodsList;
	}

	public GoodsDTO getGoodsInfo() {
		return goodsInfo;
	}

	public void setGoodsInfo(GoodsDTO goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	public HotRecommendConfig getHotRecommendConfig() {
		return hotRecommendConfig;
	}

	public void setHotRecommendConfig(HotRecommendConfig hotRecommendConfig) {
		this.hotRecommendConfig = hotRecommendConfig;
	}


}
