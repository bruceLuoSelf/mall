/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderInfoEO
 *	包	名：		com.wzitech.gamegold.order.entity
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-13
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-13 下午1:18:44
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 出库订单信息
 * @author ztjie
 *
 */
public class ShippingInfoEO extends BaseEntity {
	private static final long serialVersionUID = 3887607936419795868L;
						
	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 买家帐号
	 */
	private String buyer;
	
	/**
	 * 买家角色名
	 */
	private String buyerRole;
	
	/**
	 * 买家角色等级
	 */
	private Integer buyerRoleLevel;
	
	/**
	 * 游戏属性(游戏/区/服)
	 */
	private String gameProp;
	
	/**
	 * 游戏名称
	 */
	private String gameName;
	
	/**
	 * 所在区
	 */
	private String region;
	
	/**
	 * 所在服
	 */
	private String server;
	
	/**
	 * 所在阵营
	 */
	private String gameRace;
	
	/**
	 * 买家QQ
	 */
	private String buyerQQ;
	
	/**
	 * 买家电话
	 */
	private String buyerPhoneNumber;
	
	/*
	 * 卖家帐号
	 */
	//private String seller;
	
	/*
	 * 卖家QQ
	 */
	//private String sellerQQ;
	
	/*
	 * 卖家电话
	 */
	//private String sellerPhoneNumber;
	
	/**
	 * 商品名称
	 */
	private String title;
	
	/**
	 * 购买总数(购买金币数)
	 */
	private Integer goldCount;
	
	/**
	 * 商品单价(1金币对应多少人民币)
	 */
	private BigDecimal unitPrice;
	/**
	 * 游戏币名
	 */
	private String moneyName;
	
	/**
	 * 总费用
	 */
	private BigDecimal totalPrice;
	
	/**
	 * 交易方式
	 */
	private Integer tradeType;
	
	/**
	 * 交易地点
	 */
	private String tradePlace;
	/**
	 * 订单选择客服id
	 */
	private Long servicerId;
	
	/**
	 * 客服信息
	 */
	private UserInfoEO servicerInfo;
	
	

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 备注，买家留言
	 */
	private String notes;
	
	/**
	 * 由子订单状态得到的订单状态信息
	 */
	private Integer state;
	
	private Boolean isCopy;

	/**
	 * 新增商品类型 20170513 zhujun
	*/
	private  Long goodsTypeId;

	private String goodsTypeName;

	/**
	 * 收货角色数字ID
	 */
	private String gameNumberId;


	/**
	 * 扩展信息
	 */
	private String field;

	/**
	 * 区服互通添加
	 * 游戏属性(游戏/区/服)
	 */
	private String shippingGameProp;

	/**
	 * 区服互通添加
	 * 游戏名称
	 */
	private String shippingGameName;

	/**
	 * 区服互通添加
	 * 所在区
	 */
	private String shippingRegion;

	/**
	 * 区服互通添加
	 * 所在服
	 */
	private String shippingServer;

	/**
	 * 区服互通添加
	 * 所在阵营
	 */
	private String shippingGameRace;

	public String getGameNumberId() {
		return gameNumberId;
	}

	public void setGameNumberId(String gameNumberId) {
		this.gameNumberId = gameNumberId;
	}

	public String getMoneyName() {
		return moneyName;
	}

	public void setMoneyName(String moneyName) {
		this.moneyName = moneyName;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getGoldCount() {
		return goldCount;
	}

	public void setGoldCount(Integer goldCount) {
		this.goldCount = goldCount;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public Long getServicerId() {
		return servicerId;
	}
	public void setServicerId(Long servicerId) {
		this.servicerId = servicerId;
	}
	
	public UserInfoEO getServicerInfo() {
		return servicerInfo;
	}
	public void setServicerInfo(UserInfoEO servicerInfo) {
		this.servicerInfo = servicerInfo;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getBuyerRole() {
		return buyerRole;
	}

	public void setBuyerRole(String buyerRole) {
		this.buyerRole = buyerRole;
	}

	public String getGameProp() {
		return gameProp;
	}

	public void setGameProp(String gameProp) {
		this.gameProp = gameProp;
	}

	public String getBuyerQQ() {
		return buyerQQ;
	}

	public void setBuyerQQ(String buyerQQ) {
		this.buyerQQ = buyerQQ;
	}

	public String getBuyerPhoneNumber() {
		return buyerPhoneNumber;
	}

	public void setBuyerPhoneNumber(String buyerPhoneNumber) {
		this.buyerPhoneNumber = buyerPhoneNumber;
	}
	

	public String getTradePlace() {
		return tradePlace;
	}

	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getBuyerRoleLevel() {
		return buyerRoleLevel;
	}

	public void setBuyerRoleLevel(Integer buyerRoleLevel) {
		this.buyerRoleLevel = buyerRoleLevel;
	}

	public Boolean getIsCopy() {
		return isCopy;
	}

	public void setIsCopy(Boolean isCopy) {
		this.isCopy = isCopy;
	}

	public Long getGoodsTypeId() {
		return goodsTypeId;
	}

	public void setGoodsTypeId(Long goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}
