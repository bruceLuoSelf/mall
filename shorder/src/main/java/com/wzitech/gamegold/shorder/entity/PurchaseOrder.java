package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单
 */
public class PurchaseOrder extends BaseEntity{
    /**
     * 收货方5173账号
     */
    private String buyerAccount;
    /**
     * 收货方5173UID
     */
    private String buyerUid;
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
     * 收货数量
     */
    private Long count;
    /**
     * 收货单价
     */
    private BigDecimal price;
    /**
     * 单笔最小收货量
     */
    private Long minCount;

    /**
     * 交易方式标识
     */
    private String tradeLogo;

    /**
     * 采购商配置id
     */
    private Long purchaserId;

    public Long getPurchaserId() {
        return purchaserId;
    }

    public void setPurchaserId(Long purchaserId) {
        this.purchaserId = purchaserId;
    }

    public String getTradeLogo() {
        return tradeLogo;
    }

    public void setTradeLogo(String tradeLogo) {
        this.tradeLogo = tradeLogo;
    }

    public Long getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Long goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    /**
     * 是否上架
     */
    private Boolean isOnline;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 收货方店铺名称
     */
    private String shopName;

    /**
     * 成交率
     */
    private BigDecimal cjl;

    /**
     * 平均用时
     */
    private BigDecimal pjys;

    /**
     * 店铺信誉
     */
    private Integer credit;

    /**
     * 交易开始时间
     */
    private Integer startTime;

    /**
     * 交易结束时间
     */
    private Integer endTime;

    /**
     * 交易量
     */
    private Long tradingVolume;

    /**
     * 交易地点
     */
    private String tradeAddress;

    private Integer sellerType;
    /**
     * 单位
     */
    private String moneyName;

    /**
     * 收货方式
     * @return
     */
    private Integer deliveryType;

    /**
     *  收货模式名称
     */
    private String tradeTypeName;

    private  Long goodsType;

    private String goodsTypeName;

    private String tradeTypeId;
    /**
     * 因,可用金额不足而下架的标识
     */
    private Boolean isRobotDown;

    public Boolean getIsRobotDown() {
        return isRobotDown;
    }

    public void setIsRobotDown(Boolean robotDown) {
        isRobotDown = robotDown;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public PurchaseOrder() {
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getMinCount() {
        return minCount;
    }

    public void setMinCount(Long minCount) {
        this.minCount = minCount;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public BigDecimal getCjl() {
        return cjl;
    }

    public void setCjl(BigDecimal cjl) {
        this.cjl = cjl;
    }

    public BigDecimal getPjys() {
        return pjys;
    }

    public void setPjys(BigDecimal pjys) {
        this.pjys = pjys;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Long getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(Long tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getTradeAddress() {
        return tradeAddress;
    }

    public void setTradeAddress(String tradeAddress) {
        this.tradeAddress = tradeAddress;
    }

    public String getBuyerUid() {
        return buyerUid;
    }

    public void setBuyerUid(String buyerUid) {
        this.buyerUid = buyerUid;
    }

    public Integer getSellerType() {
        return sellerType;
    }

    public void setSellerType(Integer sellerType) {
        this.sellerType = sellerType;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public String getTradeTypeId() {
        return tradeTypeId;
    }

    public void setTradeTypeId(String tradeTypeId) {
        this.tradeTypeId = tradeTypeId;
    }
}
