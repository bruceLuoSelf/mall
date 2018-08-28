/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DiscountInfo
 *	包	名：		com.wzitech.gamegold.goods.entity
 *	项目名称：	gamegold-goods
 *	作	者：		HeJian
 *	创建时间：	2014-2-19
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-19 下午2:26:18
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.entity;

import java.math.BigDecimal;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 商品折扣信息EO
 * @author HeJian
 *
 */
public class DiscountInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商品ID
	 */
	private Long goodsId;

	/**
	 * 购买金币数
	 */
	private Integer goldCount;
	
	/**
	 * 够买金币对应折扣
	 */
	private BigDecimal discount;

	/**
	 * 是否删除
	 */
	private Boolean isDeleted;
	
	/**
	 * @return the goodsId
	 */
	public Long getGoodsId() {
		return goodsId;
	}

	/**
	 * @param goodsId the goodsId to set
	 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * @return the goldCount
	 */
	public Integer getGoldCount() {
		return goldCount;
	}

	/**
	 * @param goldCount the goldCount to set
	 */
	public void setGoldCount(Integer goldCount) {
		this.goldCount = goldCount;
	}

	/**
	 * @return the discount
	 */
	public BigDecimal getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
