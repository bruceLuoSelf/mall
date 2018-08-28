/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryOrderRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		LuChangkai
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. LuChangkai 创建于 2014-1-14 下午6:11:21
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.gamegold.app.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

import java.util.Date;

/**
 * 查询订单请求DTO
 * @author LuChangkai
 *
 */
public class QueryOrderRequest extends AbstractServiceRequest{
	/**
	 * 签名字符串
	 */
	private String sign;

	/**
	 * 页面大小
	 */
	private Integer pageSize;

	/**
	 * index
	 */
	private Integer start;

	/**
	 * 排序字段
	 */
	private String orderBy;

	/**
	 * 订单状态
	 */
	private Integer orderState;

	/**
	 * 多个订单状态
	 */
	private String orderStates;

	/**
	 * 订单创建范围起始
	 */
	private String startOrderCreate;

	/**
	 * 订单创建范围结束
	 */
	private String endOrderCreate;

	/**
	 * 订单号
	 */
	private String orderId;

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
	 * 5173注册用户账号UID
	 */

	private String userId;

	/**
	 * 5173登录账号
	 */

	private String userAccount;

	/**
	 * 商品类型
	 */
	private String goodsTypeName;


	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getStartOrderCreate() {
		return startOrderCreate;
	}

	public void setStartOrderCreate(String startOrderCreate) {
		this.startOrderCreate = startOrderCreate;
	}

	public String getEndOrderCreate() {
		return endOrderCreate;
	}

	public void setEndOrderCreate(String endOrderCreate) {
		this.endOrderCreate = endOrderCreate;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getOrderStates() {
		return orderStates;
	}

	public void setOrderStates(String orderStates) {
		this.orderStates = orderStates;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}
}
