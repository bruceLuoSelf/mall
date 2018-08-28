/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		UserInfoEO
 *	包	名：		com.wzitech.gamegold.funds.entity
 *	项目名称：	gamegold-funds
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午13:10:26
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.funds.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.constants.ServicesContants;
import org.apache.commons.lang3.StringUtils;

/**
 * 交易流水信息类
 * @author ztjie
 *
 */
public class TransactionInfo extends BaseEntity{

	private static final long serialVersionUID = 9127808347710973406L;
	
	/**
	 * 客服账号
	 */
	private String serviceAccount;
	
	/**
	 * 操作员帐号
	 */
	private String optionerAccount;
	
	/**
	 * 卖家账号
	 */
	private String loginAccount;	
	
	/**
	 * 卖家游戏角色名
	 */
	private String sellerGameRole;

	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 成交游戏账号
	 */
	private String gameAccount;
	
	/**
	 * 商品名称
	 */
	private String title;
	
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
	 * 买家5173账号
	 */
	private String buyerAccount;
	
	/**
	 * 商品单价(1游戏币兑换多少元)
	 */
	private BigDecimal orderUnitPrice;
	
	/**
	 * 库存单价(1游戏币兑换多少元)
	 */
	private BigDecimal repositoryUnitPrice;
	
	/**
	 * 购买总数(购买金币数)
	 */
	private Integer goldCount;
	
	/**
	 * 订单总金额
	 */
	private BigDecimal orderTotalPrice;
	
	/**
	 * 卖家收益
	 */
	private BigDecimal incomeTotalPrice;

	/**
	 * 差价
	 */
	private BigDecimal balance;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	
	/**
	 * 付款时间
	 */
	private Date payTime;

	/**
	 * 红包金额
	 */
	private BigDecimal redEnvelope;

	/**
	 * 佣金
	 */
	private BigDecimal commission;

	/**
	 * 店铺券金额
	 */
	private BigDecimal shopCoupon;

	/**
	 * 订单来源
	 */
	private Integer refererType;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	private Integer goodsTypeId;

	/**
	 * 通货类型名称
	 * hyl 5.12新增
	 */
	private String goodsTypeName;

	/**
	 * 发货游戏/区/服
	 * @return
	 * SERVICE_GAME_PROP
     */
	private String serviceGameProp;

	public String getServiceGameProp() {
		return serviceGameProp;
	}

	public void setServiceGameProp(String serviceGameProp) {
		this.serviceGameProp = serviceGameProp;
	}

	public Integer getGoodsTypeId() {
		return goodsTypeId;
	}

	public void setGoodsTypeId(Integer goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}

	/**
	 *
	 * @return
	 */
	public String getGoodsTypeName() {
		if(StringUtils.isBlank(goodsTypeName)){
			goodsTypeName = ServicesContants.GOODS_TYPE_GOLD;
		}
		return goodsTypeName;
	}


	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public String getGameAccount() {
		return gameAccount;
	}

	public void setGameAccount(String gameAccount) {
		this.gameAccount = gameAccount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public BigDecimal getOrderUnitPrice() {
		return orderUnitPrice;
	}

	public void setOrderUnitPrice(BigDecimal orderUnitPrice) {
		this.orderUnitPrice = orderUnitPrice;
	}

	public BigDecimal getRepositoryUnitPrice() {
		return repositoryUnitPrice;
	}

	public void setRepositoryUnitPrice(BigDecimal repositoryUnitPrice) {
		this.repositoryUnitPrice = repositoryUnitPrice;
	}

	public Integer getGoldCount() {
		return goldCount;
	}

	public void setGoldCount(Integer goldCount) {
		this.goldCount = goldCount;
	}

	public BigDecimal getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public BigDecimal getIncomeTotalPrice() {
		return incomeTotalPrice;
	}

	public void setIncomeTotalPrice(BigDecimal incomeTotalPrice) {
		this.incomeTotalPrice = incomeTotalPrice;
	}

	public String getServiceAccount() {
		return serviceAccount;
	}

	public void setServiceAccount(String serviceAccount) {
		this.serviceAccount = serviceAccount;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getOptionerAccount() {
		return optionerAccount;
	}

	public void setOptionerAccount(String optionerAccount) {
		this.optionerAccount = optionerAccount;
	}

	public String getSellerGameRole() {
		return sellerGameRole;
	}

	public void setSellerGameRole(String sellerGameRole) {
		this.sellerGameRole = sellerGameRole;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getBuyerAccount() {
		return buyerAccount;
	}

	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	public BigDecimal getRedEnvelope() {
		return redEnvelope;
	}

	public void setRedEnvelope(BigDecimal redEnvelope) {
		this.redEnvelope = redEnvelope;
	}

	public BigDecimal getShopCoupon() {
		return shopCoupon;
	}

	public void setShopCoupon(BigDecimal shopCoupon) {
		this.shopCoupon = shopCoupon;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public Integer getRefererType() {
		return refererType;
	}

	public void setRefererType(Integer refererType) {
		this.refererType = refererType;
	}


}
