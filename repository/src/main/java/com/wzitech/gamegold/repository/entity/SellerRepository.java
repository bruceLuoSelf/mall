package com.wzitech.gamegold.repository.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;

/**
 * 店铺库存
 * @author wangjunjie
 *
 */
public class SellerRepository extends BaseEntity {
    /**
     * 卖家登录5173账号
     */
    private String seller;

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
     * 可销售库存（目前存在于DNF中）
     */
    private Long sellableCount;

    /**
     * 单位
     */
    private String moneyName;

    /**
     * 单价(1游戏币兑换多少元)
     */
    private BigDecimal unitPrice;

    /**
     * 佣金
     */
    private BigDecimal commision;

    /**
     * 商家信誉
     */
    private int praiseCount;

    /**
     * 成功率
     */
    private BigDecimal successPercent;

    /**
     * 平均发货速度
     */
    private int deliverSpeed;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 是否寄售
     */
    private Boolean isShieldedType;

    /**
     * 月成交笔数
     */
    private int monthDealCount;

    /**
     * 排序字段
     * @return
     */
    private Integer sort;

    /**
     * 商品类型
     */
    private String goodsTypeName;

    private String goodsTypeId;

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(String goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
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

    public Long getSellableCount() {
        return sellableCount;
    }

    public void setSellableCount(Long sellableCount) {
        this.sellableCount = sellableCount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getCommision() {
        return commision;
    }

    public void setCommision(BigDecimal commision) {
        this.commision = commision;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public BigDecimal getSuccessPercent() {
        return successPercent;
    }

    public void setSuccessPercent(BigDecimal successPercent) {
        this.successPercent = successPercent;
    }

    public int getDeliverSpeed() {
        return deliverSpeed;
    }

    public void setDeliverSpeed(int deliverSpeed) {
        this.deliverSpeed = deliverSpeed;
    }

    public int getMonthDealCount() {
        return monthDealCount;
    }

    public void setMonthDealCount(int monthDealCount) {
        this.monthDealCount = monthDealCount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Boolean getIsShieldedType() {
        return isShieldedType;
    }

    public void setIsShieldedType(Boolean isShieldedType) {
        this.isShieldedType = isShieldedType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
