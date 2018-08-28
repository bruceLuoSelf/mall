/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryConfigResultRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		wengwei
 *	创建时间：	2014-3-27
 *	描	述：		
 *	更新纪录：	1. wengwei 创建于 2014-3-27 下午05:02:29
 * 				
 ************************************************************************************/

package com.wzitech.gamegold.facade.frontend.service.order.dto;


import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 查询卖家订单DTO
 * 
 * @author wengwei
 */
public class QueryConfigResultRequest extends AbstractServiceRequest {
	/**
	 * 游戏名
	 */
	private String gameName;

	/**
	 * 游戏区
	 */
	private String region;

	/**
	 * 游戏服
	 */
	private String server;

	/**
	 * 游戏阵营
	 */
	private String gameRace;

	/**
	 * 订单状态
	 */
	private Integer orderState;

	/**
	 * 排序字段
	 */
	private String orderBy;

	/**
	 * 是否升序
	 */
	private boolean isAsc;

	/**
	 * 页面大小
	 */
	private int pageSize;

	/**
	 * index
	 */
	private int start;
	/**
	 * 订单创建范围起始
	 */
    private String startOrderCreate;
    /**
     * 订单创建范围结束
     */
    private String endOrderCreate;
    /**
     * 查询的具体订单号
     */
    private String searchOrderId;
	/**
	 * 是否删除
	 */
	private boolean isDeleted;
	/**
	 * 商品类目名
	 */
	private String goodsTypeName;

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getGameRace() {
		return gameRace;
	}

	public void setGameRace(String gameRace) {
		this.gameRace = gameRace;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isAsc() {
		return isAsc;
	}

	public void setAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public boolean getIsDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the startOrderCreate
	 */
	public String getStartOrderCreate() {
		return startOrderCreate;
	}

	/**
	 * @param startOrderCreate the startOrderCreate to set
	 */
	public void setStartOrderCreate(String startOrderCreate) {
		this.startOrderCreate = startOrderCreate;
	}

	/**
	 * @return the endOrderCreate
	 */
	public String getEndOrderCreate() {
		return endOrderCreate;
	}

	/**
	 * @param endOrderCreate the endOrderCreate to set
	 */
	public void setEndOrderCreate(String endOrderCreate) {
		this.endOrderCreate = endOrderCreate;
	}

	public String getSearchOrderId() {
		return searchOrderId;
	}

	
	public void setSearchOrderId(String searchOrderId) {
		this.searchOrderId = searchOrderId;
	}

}
