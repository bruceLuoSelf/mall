/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *
 *	模	块：		AddOrderRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-14 下午4:55:05
 *
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;
import com.wzitech.gamegold.common.constants.ServicesContants;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 新增订单请求DTO
 *
 * @author SunChengfei
 *
 */
public class AddOrderRequest extends AbstractServiceRequest {

	/**
	 * 订单选择客服id
	 */
	private Long servicerId;

	/**
	 * 游戏等级
	 */
	private Integer gameLevel;

	/**
	 * 联系方式
	 */
	private String mobileNumber;

	/**
	 * QQ
	 */
	private String qq;

	/**
	 * 收货人姓名(游戏角色名)
	 */
	private String receiver;

	/**
	 * 商品ID
	 */
	private Long goodsId;

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
	 * 商品名称
	 */
	private String title;

	/**
	 * 游戏币名
	 */
	private String moneyName;

	/**
	 * 游戏id
	 */
	private String gameId;
	/**
	 * 区id
	 */
	private String regionId;

	/**
	 * 服id
	 */
	private String serverId;

	/**
	 * 阵营id
	 */
	private String raceId;

	/**
	 * 订单来源类型
	 */
	private Integer refererType;

	/**
	 * 来源网吧
	 */
	private String internetBar;

    /**
     * 商品所属栏目
     */
    private Integer goodsCat;
    
    /**
     * 商品所属卖家
     */
    private String sellerLoginAccount;

    /**
     * 是否购买保险
     */
    private Boolean isBuyInsurance;

	/**
	 * 商品总数(购买金币数)
	 */
	private Integer goldCount;

	/**
	 * 商品单价(1金币对应多少人民币)
	 */
	private BigDecimal unitPrice;

	/**
	 * 折扣
	 */
	private BigDecimal discount;

	/**
	 * 备注，买家留言
	 */
	private String notes;

	/**
	 * 发货速度(几分钟内可以发货，单位：分钟)
	 */
	private Integer deliveryTime;

	/**
	 * 交易方式(魔兽世界(国服）才会设置)
	 */
	private Integer tradeType;

	/**
	 * 红包号码
	 */
	private String redEnvelopeCode;
	/**
	 * 红包密码
	 */
	private String redEnvelopePwd;
	/**
	 * 店铺券号码
	 */
	private String shopCouponCode;
	/**
	 * 店铺券密码
	 */
	private String shopCouponPwd;

	/**
	 * 通货类型ID
	 * hyl 5.16新增
	 */
	private Integer goodsTypeId;

	/**
	 * 通货类型名称
	 * hyl 5.16新增
	 */
	private String goodsTypeName;

	/**
	 * 数字id
	 * wrf 6.7 新增
     */
	private String gameNumberId;

	/**
	 * 动态属性信息 以json格式的字符串传输 没有请传""
	 */
	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getGameNumberId() {
		return gameNumberId;
	}

	public void setGameNumberId(String gameNumberId) {
		this.gameNumberId = gameNumberId;
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
		return goodsTypeName;
	}

	/**
	 * @return the deliveryTime
	 */
	public Integer getDeliveryTime() {
		return deliveryTime;
	}

	/**
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	/**
	 * @return the servicerId
	 */
	public Long getServicerId() {
		return servicerId;
	}

	/**
	 * @param servicerId the servicerId to set
	 */
	public void setServicerId(Long servicerId) {
		this.servicerId = servicerId;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the qq
	 */
	public String getQq() {
		return qq;
	}

	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		this.qq = qq;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
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
	 * @return the unitPrice
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
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
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getRaceId() {
		return raceId;
	}
	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getRefererType() {
		return refererType;
	}

	public void setRefererType(Integer refererType) {
		this.refererType = refererType;
	}

    public Integer getGoodsCat() {
        return goodsCat;
    }

    public void setGoodsCat(Integer goodsCat) {
        this.goodsCat = goodsCat;
    }

	public Integer getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(Integer gameLevel) {
		this.gameLevel = gameLevel;
	}

	/**
	 * 得到/设置游戏等级
	 */


	public String getSellerLoginAccount() {
		return sellerLoginAccount;
	}

	public void setSellerLoginAccount(String sellerLoginAccount) {
		this.sellerLoginAccount = sellerLoginAccount;
	}

    public Boolean getIsBuyInsurance() {
        return isBuyInsurance;
    }

    public void setIsBuyInsurance(Boolean isBuyInsurance) {
        this.isBuyInsurance = isBuyInsurance;
    }

	/**
	 * @return the moneyName
	 */
	public String getMoneyName() {
		return moneyName;
	}

	/**
	 * @param moneyName the moneyName to set
	 */
	public void setMoneyName(String moneyName) {
		this.moneyName = moneyName;
	}

	public String getInternetBar() {
		return internetBar;
	}

	public void setInternetBar(String internetBar) {
		this.internetBar = internetBar;
	}

	public String getRedEnvelopeCode() {
		return redEnvelopeCode;
	}

	public void setRedEnvelopeCode(String redEnvelopeCode) {
		this.redEnvelopeCode = redEnvelopeCode;
	}

	public String getRedEnvelopePwd() {
		return redEnvelopePwd;
	}

	public void setRedEnvelopePwd(String redEnvelopePwd) {
		this.redEnvelopePwd = redEnvelopePwd;
	}

	public String getShopCouponCode() {
		return shopCouponCode;
	}

	public void setShopCouponCode(String shopCouponCode) {
		this.shopCouponCode = shopCouponCode;
	}

	public String getShopCouponPwd() {
		return shopCouponPwd;
	}

	public void setShopCouponPwd(String shopCouponPwd) {
		this.shopCouponPwd = shopCouponPwd;
	}
}
