/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryOrderInfoResponse
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午8:16:40
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.wzitech.gamegold.facade.serializer.JaxbDateSerializer;

/**
 * 获取订单信息响应
 * 
 * @author HeJian
 * 
 */
@XmlRootElement(name="Order")
public class QueryOrderInfoResponse {

    private String msg;

    private boolean status;
    
    /**
     * 是否有密保卡
     */
    private boolean hasPasspod;

	private String yxbMall;
	
	/**
	 * 订单编号
	 */
	private String OrderId;
	
	/**
	 * 订单创建时间
	 */
	private Date createDate;
	
	/**
	 * 发布单编号（半自动未用）
	 */
	private String BizOfferId;

	/**
	 * 发布单名称（半自动未用）
	 */
	private String BizOfferName;

	/**
	 * 游戏名称
	 */
	private String GameName;

	/**
	 * 游戏区名称 卖家所在区
	 */
	private String GameAreaName;

	/**
	 * 游戏服名称  卖家所在服务器
	 */
	private String GameAreaServer;

	/**
	 * 发布单物品类别（装备，游戏币，其它）
	 */
	private String BizOfferTypeName;

	/**
	 * 买家收货角色名称
	 */
	private String BuyerGameRole;
	
	/**
	 * 买家角色等级
	 */
	private int GameGrade;

	/**
	 * 卖家发货角色名称
	 */
	private String SellerGameRoles;

	/**
	 * 交易物品数量
	 */
	private double Quantity;

	/**
	 * 游戏币的单位名称
	 */
	private String MoneyUnitName;

	/**
	 * 出售游戏币的数据（可能为发布单出售游戏币的数量）
	 */
	private double SellMoney;

	/**
	 * 交易物品原价
	 */
	private double OriginalPrice;

	/**
	 * 交易物品金额
	 */
	private double Price;

	/**
	 * 交易员
	 */
	private String DeliverOpId;

	/**
	 * 游戏登录帐号
	 */
	private String Account;

	/**
	 * 登录密码（加密）
	 */
	private String Password;

	/**
	 * 订单状态（0：交易中 1：撤单申请 2：已撤单 3：卖家确认移交 4：移交给买家 5：交易成功 6：账号租赁）
	 */
	private String OrderStatus;

	/**
	 * 物品清单（半自动未用）
	 */
	private String BizOfferContent;

	/**
	 * 游戏帐号注册信息
	 */
	private String AccountRegInfos;

	/**
	 * 服务器当前时间
	 */
	private Date ServerTime;
	
	/**
	 * 当前时间
	 */
	private Date currentTime;
	
	/**
	 * 赔付时长
	 */
	private String delayTime;
	
	/**
	 * 倒计时
	 */
	private String tradeSpeedTime;
	
	/**
	 * 分单时间
	 */
	private Date assignDeliverOpTime;
	
	/**
	 * 游戏阵营ID
	 */
	private String GameRaceId;

	/**
	 * 游戏阵营名称
	 */
	private String GameRaceName;

	/**
	 * 买家名称
	 */
	private String BuyerName;

	/**
	 * 买家电话
	 */
	private String BuyerTel;

	/**
	 * 买家Email
	 */
	private String BuyerEmail;

	/**
	 * 买家手机
	 */
	private String BuyerMobile;

	/**
	 * 买家QQ
	 */
	private String BuyerQQ;

	/**
	 * 卖家名称
	 */
	private String SellerNames;

	/**
	 * 卖家电话
	 */
	private String SellerTels;

	/**
	 * 卖家Email
	 */
	private String SellerEmails;

	/**
	 * 卖家手机
	 */
	private String SellerMobiles;

	/**
	 * 卖家QQ
	 */
	private String SellerQQs;

	/**
	 * 交易方式（当面，邮寄）
	 */
	private String CustomBuyPatterns;

	/**
	 * 固定交易地点列表（多条以“|”号分隔）
	 */
	private String NamedPlacesOfDelivery;

	/**
	 * 固定交易地点（未用）
	 */
	private String FixedTradePlace;

	private String FixedTradePlacePicURL;

	private String LogoImg;

	/**
	 * 密保卡下载链接（未用）
	 */
	private String PasspodUrl;

	/**
	 * 游戏运行配置信息路径RC2.1.6.0以前使用
	 */
	private String GameConfigPath;

	/**
	 * 通货类型ID
	 * lvchengsheng 5.16新增 ZW_C_JB_00008
	 */
	private Long goodsTypeId;

	/**
	 * 通货类型名称
	 * lvchengsheng 5.16新增 ZW_C_JB_00008
	 */
	private String goodsTypeName;

	/**
	 *商品数字ID
	 */
	private String digitalId;


	private String fields;

	/**
	 * 买家所在服务器
	 */
	private String buyerGameAreaServer;

	/**
	 * 买家所在区
	 */
	private String buyerGameAreaName;

	/**
	 * 是否跨区
	 */
	private String interregional;

	@XmlElement(name = "BuyerGameAreaServer")
	public String getBuyerGameAreaServer() {
		return buyerGameAreaServer;
	}

	public void setBuyerGameAreaServer(String buyerGameAreaServer) {
		this.buyerGameAreaServer = buyerGameAreaServer;
	}

	@XmlElement(name = "BuyerGameAreaName")
	public String getBuyerGameAreaName() {
		return buyerGameAreaName;
	}

	public void setBuyerGameAreaName(String buyerGameAreaName) {
		this.buyerGameAreaName = buyerGameAreaName;
	}

	@XmlElement(name = "Interregional")
	public String getInterregional() {
		return interregional;
	}

	public void setInterregional(String interregional) {
		this.interregional = interregional;
	}

	@XmlElement(name = "Fields")
	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}


	@XmlElement(name = "DigitalId")
	public String getDigitalId() {
		return digitalId;
	}

	public void setDigitalId(String digitalId) {
		this.digitalId = digitalId;
	}

	@XmlElement(name = "GoodsTypeId")
	public Long getGoodsTypeId() {
		return this.goodsTypeId;
	}

	public void setGoodsTypeId(Long goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}

	@XmlElement(name = "GoodsTypeName")
	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}


	//start add by 汪俊杰 20170511 用于环信一对一vpn判断
	/**
	 * 卖家帐号
	 */
	private String sellerLoginAccount;

	@XmlElement(name = "SellerLoginAccount")
	public String getSellerLoginAccount() {
		return sellerLoginAccount;
	}

	public void setSellerLoginAccount(String sellerLoginAccount) {
		this.sellerLoginAccount = sellerLoginAccount;
	}
	//end

	/**
	 * @return the hasPasspod
	 */
	@XmlElement(name = "HasPasspod")
	public boolean isHasPasspod() {
		return hasPasspod;
	}

	/**
	 * @param hasPasspod the hasPasspod to set
	 */
	public void setHasPasspod(boolean hasPasspod) {
		this.hasPasspod = hasPasspod;
	}

	/**
	 * @return the yxbMall
	 */
	@XmlElement(name = "YXBMALL")
	public String getYxbMall() {
		return yxbMall;
	}

	/**
	 * @param yxbMall
	 *            the yxbMall to set
	 */
	public void setYxbMall(String yxbMall) {
		this.yxbMall = yxbMall;
	}

	/**
	 * @return the orderId
	 */
	@XmlElement(name = "OrderId")
	public String getOrderId() {
		return OrderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String OrderId) {
		this.OrderId = OrderId;
	}

	/**
	 * @return the createDate
	 */
	@XmlElement(name = "CreatedDate")
	@XmlJavaTypeAdapter(JaxbDateSerializer.class)
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the bizOfferId
	 */
	@XmlElement(name = "BizOfferId")
	public String getBizOfferId() {
		return BizOfferId;
	}

	/**
	 * @param bizOfferId
	 *            the bizOfferId to set
	 */
	public void setBizOfferId(String BizOfferId) {
		this.BizOfferId = BizOfferId;
	}

	/**
	 * @return the bizOfferName
	 */
	@XmlElement(name = "BizOfferName")
	public String getBizOfferName() {
		return BizOfferName;
	}

	/**
	 * @param bizOfferName
	 *            the bizOfferName to set
	 */
	public void setBizOfferName(String BizOfferName) {
		this.BizOfferName = BizOfferName;
	}

	/**
	 * @return the gameName
	 */
	@XmlElement(name = "GameName")
	public String getGameName() {
		return GameName;
	}

	/**
	 * @param gameName
	 *            the gameName to set
	 */
	public void setGameName(String GameName) {
		this.GameName = GameName;
	}

	/**
	 * @return the gameAreaName
	 */
	@XmlElement(name = "GameAreaName")
	public String getGameAreaName() {
		return GameAreaName;
	}

	/**
	 * @param gameAreaName
	 *            the gameAreaName to set
	 */
	public void setGameAreaName(String GameAreaName) {
		this.GameAreaName = GameAreaName;
	}

	/**
	 * @return the gameAreaServer
	 */
	@XmlElement(name = "GameAreaServer")
	public String getGameAreaServer() {
		return GameAreaServer;
	}

	/**
	 * @param gameAreaServer
	 *            the gameAreaServer to set
	 */
	public void setGameAreaServer(String GameAreaServer) {
		this.GameAreaServer = GameAreaServer;
	}

	/**
	 * @return the bizOfferTypeName
	 */
	@XmlElement(name = "BizOfferTypeName")
	public String getBizOfferTypeName() {
		return BizOfferTypeName;
	}

	/**
	 * @param bizOfferTypeName
	 *            the bizOfferTypeName to set
	 */
	public void setBizOfferTypeName(String BizOfferTypeName) {
		this.BizOfferTypeName = BizOfferTypeName;
	}

	/**
	 * @return the buyerGameRole
	 */
	@XmlElement(name = "BuyerGameRole")
	public String getBuyerGameRole() {
		return BuyerGameRole;
	}

	/**
	 * @param buyerGameRole
	 *            the buyerGameRole to set
	 */
	public void setBuyerGameRole(String BuyerGameRole) {
		this.BuyerGameRole = BuyerGameRole;
	}

	@XmlElement(name = "GameGrade")
	public int getGameGrade() {
		return GameGrade;
	}

	public void setGameGrade(int gameGrade) {
		GameGrade = gameGrade;
	}

	@XmlElement(name="SellerGameRole")
	public String getSellerGameRoles() {
		return SellerGameRoles;
	}

	public void setSellerGameRoles(String sellerGameRoles) {
		SellerGameRoles = sellerGameRoles;
	}

	/**
	 * @return the quantity
	 */
	@XmlElement(name = "Quantity")
	public double getQuantity() {
		return Quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(double Quantity) {
		this.Quantity = Quantity;
	}

	/**
	 * @return the moneyUnitName
	 */
	@XmlElement(name = "MoneyUnitName")
	public String getMoneyUnitName() {
		return MoneyUnitName;
	}

	/**
	 * @param moneyUnitName
	 *            the moneyUnitName to set
	 */
	public void setMoneyUnitName(String MoneyUnitName) {
		this.MoneyUnitName = MoneyUnitName;
	}

	/**
	 * @return the sellMoney
	 */
	@XmlElement(name = "SellMoney")
	public double getSellMoney() {
		return SellMoney;
	}

	/**
	 * @param sellMoney
	 *            the sellMoney to set
	 */
	public void setSellMoney(double SellMoney) {
		this.SellMoney = SellMoney;
	}

	/**
	 * @return the originalPrice
	 */
	@XmlElement(name = "OriginalPrice")
	public double getOriginalPrice() {
		return OriginalPrice;
	}

	/**
	 * @param originalPrice
	 *            the originalPrice to set
	 */
	public void setOriginalPrice(double OriginalPrice) {
		this.OriginalPrice = OriginalPrice;
	}

	/**
	 * @return the price
	 */
	@XmlElement(name = "Price")
	public double getPrice() {
		return Price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double Price) {
		this.Price = Price;
	}

	/**
	 * @return the deliverOpId
	 */
	@XmlElement(name = "DeliverOpId")
	public String getDeliverOpId() {
		return DeliverOpId;
	}

	/**
	 * @param deliverOpId
	 *            the deliverOpId to set
	 */
	public void setDeliverOpId(String DeliverOpId) {
		this.DeliverOpId = DeliverOpId;
	}

	/**
	 * @return the account
	 */
	@XmlElement(name = "Account")
	public String getAccount() {
		return Account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String Account) {
		this.Account = Account;
	}

	/**
	 * @return the password
	 */
	@XmlElement(name = "Password")
	public String getPassword() {
		return Password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String Password) {
		this.Password = Password;
	}

	/**
	 * @return the orderStatus
	 */
	@XmlElement(name = "OrderStatus")
	public String getOrderStatus() {
		return OrderStatus;
	}

	/**
	 * @param orderStatus
	 *            the orderStatus to set
	 */
	public void setOrderStatus(String OrderStatus) {
		this.OrderStatus = OrderStatus;
	}

	/**
	 * @return the bizOfferContent
	 */
	@XmlElement(name = "BizOfferContent")
	public String getBizOfferContent() {
		return BizOfferContent;
	}

	/**
	 * @param bizOfferContent
	 *            the bizOfferContent to set
	 */
	public void setBizOfferContent(String BizOfferContent) {
		this.BizOfferContent = BizOfferContent;
	}

	@XmlElement(name="AccountRegInfo")
	public String getAccountRegInfos() {
		return AccountRegInfos;
	}

	public void setAccountRegInfos(String accountRegInfos) {
		AccountRegInfos = accountRegInfos;
	}

	/**
	 * @return the serverTime
	 */
	@XmlElement(name = "ServerTime")
    @XmlJavaTypeAdapter(JaxbDateSerializer.class)
    public Date getServerTime() {
		return ServerTime;
	}

	/**
	 * @param serverTime
	 *            the serverTime to set
	 */
	public void setServerTime(Date ServerTime) {
		this.ServerTime = ServerTime;
	}
	
	/**
	 * @return the currentTime
	 */
	@XmlElement(name = "CurrentTime")
    @XmlJavaTypeAdapter(JaxbDateSerializer.class)
	public Date getCurrentTime() {
		return currentTime;
	}

	/**
	 * @param currentTime the currentTime to set
	 */
	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	/**
	 * @return the delayTime
	 */
	@XmlElement(name = "DelayTime")
	public String getDelayTime() {
		return delayTime;
	}

	/**
	 * @param delayTime the delayTime to set
	 */
	public void setDelayTime(String delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * @return the tradeSpeedTime
	 */
	@XmlElement(name = "TradeSpeedTime")
	public String getTradeSpeedTime() {
		return tradeSpeedTime;
	}

	/**
	 * @param tradeSpeedTime the tradeSpeedTime to set
	 */
	public void setTradeSpeedTime(String tradeSpeedTime) {
		this.tradeSpeedTime = tradeSpeedTime;
	}

	/**
	 * @return the assignDeliverOpTime
	 */
	@XmlElement(name = "AssignDeliverOpTime")
    @XmlJavaTypeAdapter(JaxbDateSerializer.class)
	public Date getAssignDeliverOpTime() {
		return assignDeliverOpTime;
	}

	/**
	 * @param assignDeliverOpTime the assignDeliverOpTime to set
	 */
	public void setAssignDeliverOpTime(Date assignDeliverOpTime) {
		this.assignDeliverOpTime = assignDeliverOpTime;
	}

	/**
	 * @return the gameRaceId
	 */
	@XmlElement(name = "GameRaceId")
	public String getGameRaceId() {
		return GameRaceId;
	}

	/**
	 * @param gameRaceId
	 *            the gameRaceId to set
	 */
	public void setGameRaceId(String GameRaceId) {
		this.GameRaceId = GameRaceId;
	}

	/**
	 * @return the gameRaceName
	 */
	@XmlElement(name = "GameRaceName")
	public String getGameRaceName() {
		return GameRaceName;
	}

	/**
	 * @param gameRaceName
	 *            the gameRaceName to set
	 */
	public void setGameRaceName(String GameRaceName) {
		this.GameRaceName = GameRaceName;
	}

	/**
	 * @return the buyerName
	 */
	@XmlElement(name = "BuyerName")
	public String getBuyerName() {
		return BuyerName;
	}

	/**
	 * @param buyerName
	 *            the buyerName to set
	 */
	public void setBuyerName(String BuyerName) {
		this.BuyerName = BuyerName;
	}

	/**
	 * @return the buyerTel
	 */
	@XmlElement(name = "BuyerTel")
	public String getBuyerTel() {
		return BuyerTel;
	}

	/**
	 * @param buyerTel
	 *            the buyerTel to set
	 */
	public void setBuyerTel(String BuyerTel) {
		this.BuyerTel = BuyerTel;
	}

	/**
	 * @return the BuyerEmail
	 */
	@XmlElement(name = "BuyerEmail")
	public String getBuyerEmail() {
		return BuyerEmail;
	}

	/**
	 * @param buyerEmail
	 *            the BuyerEmail to set
	 */
	public void setBuyerEmail(String BuyerEmail) {
		this.BuyerEmail = BuyerEmail;
	}

	/**
	 * @return the BuyerMobile
	 */
	@XmlElement(name = "BuyerMobile")
	public String getBuyerMobile() {
		return BuyerMobile;
	}

	/**
	 * @param buyerMobile
	 *            the BuyerMobile to set
	 */
	public void setBuyerMobile(String BuyerMobile) {
		this.BuyerMobile = BuyerMobile;
	}

	/**
	 * @return the buyerQQ
	 */
	@XmlElement(name = "BuyerQQ")
	public String getBuyerQQ() {
		return BuyerQQ;
	}

	/**
	 * @param BuyerQQ
	 *            the BuyerQQ to set
	 */
	public void setBuyerQQ(String BuyerQQ) {
		this.BuyerQQ = BuyerQQ;
	}

	@XmlElement(name="SellerName")
	public String getSellerNames() {
		return SellerNames;
	}

	public void setSellerNames(String sellerNames) {
		SellerNames = sellerNames;
	}

	@XmlElement(name="SellerTel")
	public String getSellerTels() {
		return SellerTels;
	}

	public void setSellerTels(String sellerTels) {
		SellerTels = sellerTels;
	}

	@XmlElement(name="SellerEmail")
	public String getSellerEmails() {
		return SellerEmails;
	}

	public void setSellerEmails(String sellerEmails) {
		SellerEmails = sellerEmails;
	}

	@XmlElement(name="SellerMobile")
	public String getSellerMobiles() {
		return SellerMobiles;
	}

	public void setSellerMobiles(String sellerMobiles) {
		SellerMobiles = sellerMobiles;
	}

	@XmlElement(name="SellerQQ")
	public String getSellerQQs() {
		return SellerQQs;
	}

	public void setSellerQQs(String sellerQQs) {
		SellerQQs = sellerQQs;
	}

	/**
	 * @return the CustomBuyPatterns
	 */
	@XmlElement(name = "CustomBuyPatterns")
	public String getCustomBuyPatterns() {
		return CustomBuyPatterns;
	}

	/**
	 * @param CustomBuyPatterns
	 *            the CustomBuyPatterns to set
	 */
	public void setCustomBuyPatterns(String CustomBuyPatterns) {
		this.CustomBuyPatterns = CustomBuyPatterns;
	}

	/**
	 * @return the nNmedPlacesOfDelivery
	 */
	@XmlElement(name = "NamedPlacesOfDelivery")
	public String getNamedPlacesOfDelivery() {
		return NamedPlacesOfDelivery;
	}

	/**
	 * @param NamedPlacesOfDelivery
	 *            the NamedPlacesOfDelivery to set
	 */
	public void setNamedPlacesOfDelivery(String NamedPlacesOfDelivery) {
		this.NamedPlacesOfDelivery = NamedPlacesOfDelivery;
	}

	/**
	 * @return the FixedTradePlace
	 */
	@XmlElement(name = "FixedTradePlace")
	public String getFixedTradePlace() {
		return FixedTradePlace;
	}

	/**
	 * @param FixedTradePlace
	 *            the FixedTradePlace to set
	 */
	public void setFixedTradePlace(String FixedTradePlace) {
		this.FixedTradePlace = FixedTradePlace;
	}

	/**
	 * @return the FixedTradePlacePicURL
	 */
	@XmlElement(name = "FixedTradePlacePicURL")
	public String getFixedTradePlacePicURL() {
		return FixedTradePlacePicURL;
	}

	/**
	 * @param FixedTradePlacePicURL
	 *            the FixedTradePlacePicURL to set
	 */
	public void setFixedTradePlacePicURL(String FixedTradePlacePicURL) {
		this.FixedTradePlacePicURL = FixedTradePlacePicURL;
	}

	/**
	 * @return the LogoImg
	 */
	@XmlElement(name = "LogoImg")
	public String getLogoImg() {
		return LogoImg;
	}

	/**
	 * @param LogoImg
	 *            the LogoImg to set
	 */
	public void setLogoImg(String LogoImg) {
		this.LogoImg = LogoImg;
	}

	/**
	 * @return the passpodUrl
	 */
	@XmlElement(name = "PasspodUrl")
	public String getPasspodUrl() {
		return PasspodUrl;
	}

	/**
	 * @param passpodUrl the passpodUrl to set
	 */
	public void setPasspodUrl(String passpodUrl) {
		PasspodUrl = passpodUrl;
	}

	/**
	 * @return the gameConfigPath
	 */
	@XmlElement(name = "GameConfigPath")
	public String getGameConfigPath() {
		return GameConfigPath;
	}

	/**
	 * @param gameConfigPath
	 *            the gameConfigPath to set
	 */
	public void setGameConfigPath(String GameConfigPath) {
		this.GameConfigPath = GameConfigPath;
	}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
