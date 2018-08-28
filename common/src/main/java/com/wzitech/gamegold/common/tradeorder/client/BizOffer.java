/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		BizOffer
 *	包	名：		com.wzitech.gamegold.common.tradeorder.dto.response
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-4-9
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-4-9 下午7:00:20
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.tradeorder.client;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author SunChengfei
 *
 */
public class BizOffer {
	private String gameName;
	
	private String gameAreaName;
	
	private String gameServerName;
	
	private String gameRaceName;
	
	private String bizOfferId;
	
	private String bizOfferName;
	
	private String createDate;
	
	private BigDecimal price;
	
	private BigDecimal totalPrice;
	
	private String buyUrl;
	
	private int serviceType;

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	@JsonProperty("GameName")
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the gameAreaName
	 */
	public String getGameAreaName() {
		return gameAreaName;
	}

	/**
	 * @param gameAreaName the gameAreaName to set
	 */
	@JsonProperty("GameAreaName")
	public void setGameAreaName(String gameAreaName) {
		this.gameAreaName = gameAreaName;
	}

	/**
	 * @return the gameServerName
	 */
	public String getGameServerName() {
		return gameServerName;
	}

	/**
	 * @param gameServerName the gameServerName to set
	 */
	@JsonProperty("GameServerName")
	public void setGameServerName(String gameServerName) {
		this.gameServerName = gameServerName;
	}

	/**
	 * @return the gameRaceName
	 */
	public String getGameRaceName() {
		return gameRaceName;
	}

	/**
	 * @param gameRaceName the gameRaceName to set
	 */
	@JsonProperty("GameRaceName")
	public void setGameRaceName(String gameRaceName) {
		this.gameRaceName = gameRaceName;
	}

	/**
	 * @return the bizOfferId
	 */
	public String getBizOfferId() {
		return bizOfferId;
	}

	/**
	 * @param bizOfferId the bizOfferId to set
	 */
	@JsonProperty("BizOfferId")
	public void setBizOfferId(String bizOfferId) {
		this.bizOfferId = bizOfferId;
	}

	/**
	 * @return the bizOfferName
	 */
	public String getBizOfferName() {
		return bizOfferName;
	}

	/**
	 * @param bizOfferName the bizOfferName to set
	 */
	@JsonProperty("BizOfferName")
	public void setBizOfferName(String bizOfferName) {
		this.bizOfferName = bizOfferName;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	@JsonProperty("CreateDate")
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	@JsonProperty("Price")
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * @return the totalPrice
	 */
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	@JsonProperty("TotalPrice")
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * @return the buyUrl
	 */
	public String getBuyUrl() {
		return buyUrl;
	}

	/**
	 * @param buyUrl the buyUrl to set
	 */
	@JsonProperty("BuyUrl")
	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}

	/**
	 * @return the serviceType
	 */
	public int getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	@JsonProperty("ServiceType")
	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}
	
}
