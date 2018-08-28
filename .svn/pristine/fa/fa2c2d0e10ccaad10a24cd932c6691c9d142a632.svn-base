/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderHtmlQueryResponse
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2014-3-8
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-3-8 下午6:53:34
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.order.dto;

import java.math.BigDecimal;
import java.util.List;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.goods.dto.GoodsDTO;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import com.wzitech.gamegold.order.entity.InsuranceSettings;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 创建订单页查询返回
 * @author SunChengfei
 *
 */
public class OrderHtmlQueryResponse extends AbstractServiceResponse {
	/**
	 * 客服实体
	 */
	private List<UserInfoEO> userInfoEOs;

	/**
	 * 商品实体
	 */
	private GoodsDTO goodsInfo;
	
	/**
	 * 订单信息
	 */
	private OrderInfoEO orderInfoEO;

	/**
	 * 返回最新5笔订单
	 */
	private List<OrderInfoEO> orderInfoEOs;
	
	/**
	 * 服务器当前时间
	 */
	private long currentDate;
	
	/**
	 * 库存最大值
	 */
	private Long maxCount;
	/**
	 * 最小购买数量
	 */
	private BigDecimal minBuyAmount;

    /**
     * 保险配置
     */
    private InsuranceSettings insuranceSettings;
	
	/**
	 * @return the userInfoEOs
	 */
	public List<UserInfoEO> getUserInfoEOs() {
		return userInfoEOs;
	}

	/**
	 * @param userInfoEOs the userInfoEOs to set
	 */
	public void setUserInfoEOs(List<UserInfoEO> userInfoEOs) {
		this.userInfoEOs = userInfoEOs;
	}

	public BigDecimal getMinBuyAmount() {
		return minBuyAmount;
	}

	public void setMinBuyAmount(BigDecimal minBuyAmount) {
		this.minBuyAmount = minBuyAmount;
	}

	/**
	 * @return the goodsInfo
	 */
	public GoodsDTO getGoodsInfo() {
		return goodsInfo;
	}

	/**
	 * @param goodsInfo the goodsInfo to set
	 */
	public void setGoodsInfo(GoodsDTO goodsInfo) {
		this.goodsInfo = goodsInfo;
	}

	/**
	 * @return the orderInfoEO
	 */
	public OrderInfoEO getOrderInfoEO() {
		return orderInfoEO;
	}

	/**
	 * @param orderInfoEO the orderInfoEO to set
	 */
	public void setOrderInfoEO(OrderInfoEO orderInfoEO) {
		this.orderInfoEO = orderInfoEO;
	}

	/**
	 * @return the currentDate
	 */
	public long getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(long currentDate) {
		this.currentDate = currentDate;
	}

	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}

	public List<OrderInfoEO> getOrderInfoEOs() {
		return orderInfoEOs;
	}

	public void setOrderInfoEOs(List<OrderInfoEO> orderInfoEOs) {
		this.orderInfoEOs = orderInfoEOs;
	}

    public InsuranceSettings getInsuranceSettings() {
        return insuranceSettings;
    }

    public void setInsuranceSettings(InsuranceSettings insuranceSettings) {
        this.insuranceSettings = insuranceSettings;
    }
}
