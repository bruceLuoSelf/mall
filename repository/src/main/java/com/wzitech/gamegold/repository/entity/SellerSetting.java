package com.wzitech.gamegold.repository.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;

public class SellerSetting extends BaseEntity {
    /**
     * 游戏名
     */
    private String gameName;

    /**
     * 大区
     */
    private String region;

    /**
     * 用户账号(5173的用户账号)
     */
    private String loginAccount;

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
     * 月成交笔数
     */
    private int monthDealCount;

    /**
     * 是否可用
     */
    private Boolean isDeleted;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 排序字段
     * @return
     */
    private Integer sort;

    /**
     * 通货类目ID
     * wrf 5.11新增
     */
    private Long goodsTypeId;

    /**
     * 通货类目名称
     * wrf 5.11新增
     */
    private String goodsTypeName;

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

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
