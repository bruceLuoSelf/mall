/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TradePlaceEO
 *	包	名：		com.wzitech.gamegold.order.entity
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午11:17:48
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 游戏配置EO
 * 
 * @author SunChengfei
 * 2017/05/24    wubiao     ZW_C_JB_00008 商城增加通货
 */
public class GameConfigEO extends BaseEntity {
	private static final long serialVersionUID = 1L;

    /**
     * 游戏ID
     */
    private String gameId;

	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 游戏交易地点名称
	 */
	private String placeName;

	/**
	 * 交易地点截图
	 */
	private String placeImage;
	
	/**
	 * 游戏商品图片
	 */
	private String gameImage;
	
	/**
	 * 邮寄时间
	 */
	private Integer mailTime;
	
	/**
	 * 自动打款时间
	 */
	private Integer autoPlayTime;

	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 佣金
	 */
	private BigDecimal commision;

	/**
	 * 是否删除
	 */
	private Boolean isDeleted;

	/*********ZW_C_JB_00008_20170524 ADD*******/
	private String goodsTypeName; //商品类型
	private Long goodsTypeId; //商品ID
	/*********ZW_C_JB_00008_20170524 ADD*******/

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceImage() {
		return placeImage;
	}

	public void setPlaceImage(String placeImage) {
		this.placeImage = placeImage;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the mailTime
	 */
	public Integer getMailTime() {
		return mailTime;
	}

	/**
	 * @param mailTime the mailTime to set
	 */
	public void setMailTime(Integer mailTime) {
		this.mailTime = mailTime;
	}

	/**
	 * @return the gameImage
	 */
	public String getGameImage() {
		return gameImage;
	}

	/**
	 * @param gameImage the gameImage to set
	 */
	public void setGameImage(String gameImage) {
		this.gameImage = gameImage;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getAutoPlayTime() {
		return autoPlayTime;
	}

	public void setAutoPlayTime(Integer autoPlayTime) {
		this.autoPlayTime = autoPlayTime;
	}

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

	public BigDecimal getCommision() {
		return commision;
	}

	public void setCommision(BigDecimal commision) {
		this.commision = commision;
	}

	public String getGoodsTypeName() {return goodsTypeName;}

	public void setGoodsTypeName(String goodsTypeName) {this.goodsTypeName = goodsTypeName;}

	public Long getGoodsTypeId() {return goodsTypeId;}

	public void setGoodsTypeId(Long goodsTypeId) {this.goodsTypeId = goodsTypeId;}
}
