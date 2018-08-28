package com.wzitech.gamegold.facade.frontend.service.goods.dto;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;

/**
 * 商品DTO
 *
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 * @author yemq
 */
public class GoodsDTO extends BaseEntity {
    /**
     * 商品名称
     */
    private String title;
    /**
     * 商品所属栏目
     */
    private Integer goodsCat;
    /**
     * 图片地址
     */
    private String imageUrls;
    /**
     * 发货速度(几分钟内可以发货，单位：分钟)
     */
    private Integer deliveryTime;
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
     * 所属阵营
     */
    private String gameRace;
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
     * 单价(1游戏币兑换多少元)
     */
    private BigDecimal unitPrice;
    /**
     * 游戏币名称
     */
    private String moneyName;
    /**
     * 可销售库存
     */
    private Long sellableCount;
    /**
     * 库存所属卖家
     */
    private String sellerLoginAccount;
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
     * 超时赔付率
     */
    private int monthDealCount;
    /**
     * 是否禁用
     */
    private Boolean isDeleted;

    private String shopName;
    /**ZW_C_JB_00008_20170516  wrf start add**/
    /**
     * 商品类型
     */
    private String goodsTypeName;
    /**
     * 商品ID
     */
    private Long goodsTypeId;

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

    /**ZW_C_JB_00008_20170516  wrf end **/



    public GoodsDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getGoodsCat() {
        return goodsCat;
    }

    public void setGoodsCat(Integer goodsCat) {
        this.goodsCat = goodsCat;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public Long getSellableCount() {
        return sellableCount;
    }

    public void setSellableCount(Long sellableCount) {
        this.sellableCount = sellableCount;
    }

    public String getSellerLoginAccount() {
        return sellerLoginAccount;
    }

    public void setSellerLoginAccount(String sellerLoginAccount) {
        this.sellerLoginAccount = sellerLoginAccount;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
}
