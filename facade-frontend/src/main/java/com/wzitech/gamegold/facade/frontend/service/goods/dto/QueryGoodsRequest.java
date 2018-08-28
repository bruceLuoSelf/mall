/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryGoodsRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.goods.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-1-15
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-15 下午1:45:43
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.goods.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

import java.math.BigDecimal;

/**
 * 根据条件分页查询商品请求DTO
 * @author HeJian
 *
 */
public class QueryGoodsRequest extends AbstractServiceRequest{
	
	/**
	 * 是否下架
	 */
	private Boolean isDeleted;
	
	/**
	 * 用户ID
	 */
	private Long uid;
	
	/**
	 * 商品Id
	 */
	private Long goodsId;
	
	/**
	 * 当前页面url
	 */
	private String currentUrl;
	
	/**
	 * 游戏名
	 */
	private String gameName;
	/**
	 * 游戏ID
	 */
	private String gameID;
	/**
	 * 所在区
	 */
	private String region;
	/**
	 * 区ID
	 */
	private String regionID;
	
	/**
	 * 所在服
	 */
	private String server;
	/**
	 * 服ID
	 */
	private String serverID;
	
	/**
	 * 所属阵营
	 */
	private String gameRace;
	/**
	 * 阵营ID
	 */
	private String raceID;
	
	/**
	 * 商品栏目
	 */
	private Integer goodsCat;
	
	/**
	 * 商品名称
	 */
	private String title;
	
	/**
	 * 排序字段
	 */
	private String orderBy;
	
	/**
	 * 是否升序
	 */
	private Boolean isAsc;
	
	/**
	 * 页面大小
	 */
	private int pageSize;
	
	/**
	 * 第几页
	 */
	private int pubSize;
	/**
	 * 卖家账号
	 */
	public String LoginAccount;

	/**
	 * 商品类型
	 */
	public String goodsTypeName;

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	/**
	 * @return the uid
	 */
	public Long getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

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
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the gameRace
	 */
	public String getGameRace() {
		return gameRace;
	}

	/**
	 * @param gameRace the gameRace to set
	 */
	public void setGameRace(String gameRace) {
		this.gameRace = gameRace;
	}

	/**
	 * @return the goodsCat
	 */
	public Integer getGoodsCat() {
		return goodsCat;
	}

	/**
	 * @param goodsCat the goodsCat to set
	 */
	public void setGoodsCat(Integer goodsCat) {
		this.goodsCat = goodsCat;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the orderBy
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Boolean getIsAsc() {
		return isAsc;
	}

	@JsonProperty("isAsc")
	public void setIsAsc(Boolean isAsc) {
		this.isAsc = isAsc;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the pubSize
	 */
	public int getPubSize() {
		return pubSize;
	}

	/**
	 * @param pubSize the pubSize to set
	 */
	public void setPubSize(int pubSize) {
		this.pubSize = pubSize;
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
	@JsonProperty("isDeleted")
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public String getRegionID() {
		return regionID;
	}

	public void setRegionID(String regionID) {
		this.regionID = regionID;
	}

	public String getServerID() {
		return serverID;
	}

	public void setServerID(String serverID) {
		this.serverID = serverID;
	}

	public String getRaceID() {
		return raceID;
	}

	public void setRaceID(String raceID) {
		this.raceID = raceID;
	}


	public String getLoginAccount() {
		return LoginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		LoginAccount = loginAccount;
	}
}
