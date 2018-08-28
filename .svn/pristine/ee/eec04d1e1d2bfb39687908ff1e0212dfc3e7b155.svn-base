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

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.game.entity.AnalysisUrlResponse;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;

import java.math.BigDecimal;
import java.util.List;

/**
 * 根据条件分页查询商品请求DTO响应
 * @author HeJian
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008商城增加通货
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

	private AnalysisUrlResponse analysisUrlResponse;

    /**
     * 安心买热卖商品配置
     */
    private HotRecommendConfig hotRecommendConfig;

	/**ZW_C_JB_00008_20170512 start add**/
	/**
	 * 商品单位
	 * @return
     */
	private String unitName;

	private BigDecimal minBuyAmount;

	public BigDecimal getMinBuyAmount() {
		return minBuyAmount;
	}

	public void setMinBuyAmount(BigDecimal minBuyAmount) {
		this.minBuyAmount = minBuyAmount;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**ZW_C_JB_00008_20170512 end**/

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

	public AnalysisUrlResponse getAnalysisUrlResponse() {
		return analysisUrlResponse;
	}

	public void setAnalysisUrlResponse(AnalysisUrlResponse analysisUrlResponse) {
		this.analysisUrlResponse = analysisUrlResponse;
	}
}
