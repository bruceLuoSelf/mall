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

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by SunChengfei on 2017/1/4.
 *
 *  Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/05/12  huangyanling        ZW_C_JB_00008 商城增加通货
 * 2017/06/07  wurongfan           商城增加通货优化
 */
public class OrderInfoEO extends BaseEntity {
	private static final long serialVersionUID = 3887607936419795868L;

	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 订单所属用户ID
	 */
	private String uid;
	
	/**
	 * 订单所属用户帐号
	 */
	private String userAccount;
	
	/**
	 * 订单选择客服id
	 */
	private Long servicerId;
	
	/**
	 * 客服信息
	 */
	private UserInfoEO servicerInfo;
	
	/**
	 * 交易方式
	 */
	private Integer tradeType;
	
	/**
	 * 联系方式(手机)
	 */
	private String mobileNumber;
	
	/**
	 * QQ
	 */
	private String qq;
	
	/**
	 * 收货人姓名(游戏角色)
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
	
	private GameConfigEO tradePlaceEO;
	
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
	 * 游戏等级
	 */
	private Integer gameLevel;

	/**
	 * 收货角色数字ID
	 */
	private String gameNumberId;   /**2017/06/07  wrf add**/

	public String getGameNumberId() {
		return gameNumberId;
	}

	public void setGameNumberId(String gameNumberId) {
		this.gameNumberId = gameNumberId;
	}

	/**
	 * 得到/设置游戏等级
	 */
	public Integer getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(Integer gameLevel) {
		this.gameLevel = gameLevel;
	}

	/**
	 * 发货速度(几分钟内可以发货，单位：分钟)
	 */
	private Integer deliveryTime;
	
	/**
	 * 商品名称
	 */
	private String title;
	
	/**
	 * 购买总数(购买金币数)
	 */
	private Long goldCount;
	
	/**
	 * 商品单价(1金币对应多少人民币)
	 */
	private BigDecimal unitPrice;
	
	/**
	 * 游戏币名
	 */
	private String moneyName;
	
	/**
	 * 备注，买家留言
	 */
	private String notes;
	
	/**
	 * 总费用
	 */
	private BigDecimal totalPrice;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 人工操作
	 */
	private Boolean manualOperation;

	/**
	 * 是否延迟
	 */
	private Boolean isDelay;
	
	/**
	 * 是否有货
	 */
	private Boolean isHaveStore;
	
	/**
	 * 下单时间
	 */
	private Date createTime;

	/**
	 * 付款时间
	 */
	private Date payTime;
	
	/**
	 * 发货时间
	 */
	private Date sendTime;
	
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 取消原因
	 */
    private String cancelReson;
    
    private Boolean isCopy;
    
    private Integer refundReason;
    
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
     * 商品所属栏目
     */
    private Integer goodsCat;
    
    /**
     * 商品所属卖家
     */
    private String sellerLoginAccount;

    /**
     * 是否购买了保险
     */
    private Boolean isBuyInsurance;

    /**
     * 保险费率
     */
    private BigDecimal insuranceRate;

    /**
     * 保险金额
     */
    private BigDecimal insuranceAmount;

    /**
     * 保险过期时间
     */
    private Date insuranceExpireTime;

	/**
	 * 客服服务费
	 */
	private int serviceCharge;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 来源网吧,只有refererType=RefererType.InternetBarAlliance时，才有此属性
	 */
	private String internetBar;

	/**
	 * 是否已经评价
	 */
	private Boolean isEvaluate;

	/**
	 * 是否已经追加评价
	 */
	private Boolean isReEvaluate;

	/**
	 * 红包金额
	 */
	private Double redEnvelope;

	/**
	 * 店铺券金额
	 */
	private Double shopCoupon;

	/**
	 * 红包号码(不写入数据库)
	 */
	private String redEnvelopeCode;
	/**
	 * 红包密码(不写入数据库)
	 */
	private String redEnvelopePwd;
	/**
	 * 店铺券号码(不写入数据库)
	 */
	private String shopCouponCode;
	/**
	 * 店铺券密码(不写入数据库)
	 */
	private String shopCouponPwd;
	/**
	* 限制购买金额（不写入数据库）
	 * */
	private BigDecimal limitPrice;

	/**
	 * 通货类型ID
	 * wrf 5.11新增
	 */
	private Long goodsTypeId;

	/**
	 * 通货类型名称
	 * wrf 5.11新增
	 */
	private String goodsTypeName;

	/**
	 * 设备ID(app)
	* */
	private String mobileId;

	/**
	 * 设备名(app)
	 * */
	private String mobileType;
	/**
	 * 请求人的Ip
	 * */
	private String TerminalIp;

	/**
	 * 动态属性
	 */
	private String field;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTerminalIp() {
		return TerminalIp;
	}

	public void setTerminalIp(String terminalIp) {
		TerminalIp = terminalIp;
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}

	public Long getGoodsTypeId() {
		return goodsTypeId;
	}

	public void setGoodsTypeId(Long goodsTypeId) {
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

	public BigDecimal getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(BigDecimal limitPrice) {
		this.limitPrice = limitPrice;
	}

	public OrderInfoEO(){}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the servicerId
	 */
	public Long getServicerId() {
		return servicerId;
	}

	/**
	 * @return the servicerInfo
	 */
	public UserInfoEO getServicerInfo() {
		return servicerInfo;
	}

	/**
	 * @param servicerInfo the servicerInfo to set
	 */
	public void setServicerInfo(UserInfoEO servicerInfo) {
		this.servicerInfo = servicerInfo;
	}

	/**
	 * @return the isHaveStore
	 */
	public Boolean getIsHaveStore() {
		return isHaveStore;
	}

	/**
	 * @return the moneyName
	 */
	public String getMoneyName() {
		if(null!=moneyName){
			moneyName=moneyName.trim();
		}
		return moneyName;
	}

	/**
	 * @param moneyName the moneyName to set
	 */
	public void setMoneyName(String moneyName) {
		this.moneyName = moneyName;
	}

	/**
	 * @return the payTime
	 */
	public Date getPayTime() {
		return payTime;
	}

	/**
	 * @param payTime the payTime to set
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	/**
	 * @param isHaveStore the isHaveStore to set
	 */
	public void setIsHaveStore(Boolean isHaveStore) {
		this.isHaveStore = isHaveStore;
	}

	/**
	 * @param servicerId the servicerId to set
	 */
	public void setServicerId(Long servicerId) {
		this.servicerId = servicerId;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
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

	public GameConfigEO getTradePlaceEO() {
		return tradePlaceEO;
	}

	public void setTradePlaceEO(GameConfigEO tradePlaceEO) {
		this.tradePlaceEO = tradePlaceEO;
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
	 * @return the userAccount
	 */
	public String getUserAccount() {
		return userAccount;
	}

	/**
	 * @param userAccount the userAccount to set
	 */
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
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

	public Integer getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	/**
	 * @return the goldCount
	 */
	public Long getGoldCount() {
		return goldCount;
	}

	/**
	 * @param goldCount the goldCount to set
	 */
	public void setGoldCount(Long goldCount) {
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

	/**
	 * @return the totalPrice
	 */
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * @return the orderState
	 */
	public Integer getOrderState() {
		return orderState;
	}

	/**
	 * @param orderState the orderState to set
	 */
	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	public Boolean getManualOperation() {
		return manualOperation;
	}
	
	public void setManualOperation(Boolean manualOperation) {
		this.manualOperation = manualOperation;
	}
	
	public Boolean getIsDelay() {
		return isDelay;
	}

	public void setIsDelay(Boolean isDelay) {
		this.isDelay = isDelay;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the sendTime
	 */
	public Date getSendTime() {
		return sendTime;
	}

	/**
	 * @param sendTime the sendTime to set
	 */
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the cancelReson
	 */
	public String getCancelReson() {
		return cancelReson;
	}
	/**
	 * @param cancelReson the cancelReson to set
	 */
	public void setCancelReson(String cancelReson) {
		this.cancelReson = cancelReson;
	}
	
	public Boolean getIsCopy() {
		return isCopy;
	}
	
	public void setIsCopy(Boolean isCopy) {
		this.isCopy = isCopy;
	}
	
	public Integer getRefundReason() {
		return refundReason;
	}
	
	public void setRefundReason(Integer refundReason) {
		this.refundReason = refundReason;
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

    public BigDecimal getInsuranceRate() {
        return insuranceRate;
    }

    public void setInsuranceRate(BigDecimal insuranceRate) {
        this.insuranceRate = insuranceRate;
    }

    public BigDecimal getInsuranceAmount() {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount) {
        this.insuranceAmount = insuranceAmount;
    }

    public Date getInsuranceExpireTime() {
        return insuranceExpireTime;
    }

    public void setInsuranceExpireTime(Date insuranceExpireTime) {
        this.insuranceExpireTime = insuranceExpireTime;
    }

	public int getServiceCharge() {
		return serviceCharge;
	}

	public void setServiceCharge(int serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInternetBar() {
		return internetBar;
	}

	public void setInternetBar(String internetBar) {
		this.internetBar = internetBar;
	}

	public Boolean getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(Boolean isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public Boolean getIsReEvaluate() {
		return isReEvaluate;
	}

	public void setIsReEvaluate(Boolean isReEvaluate) {
		this.isReEvaluate = isReEvaluate;
	}

	public Double getRedEnvelope() {
		return redEnvelope;
	}

	public void setRedEnvelope(Double redEnvelope) {
		this.redEnvelope = redEnvelope;
	}

	public Double getShopCoupon() {
		return shopCoupon;
	}

	public void setShopCoupon(Double shopCoupon) {
		this.shopCoupon = shopCoupon;
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
