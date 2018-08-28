/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GoodsInfo
 *	包	名：		com.wzitech.gamegold.goodsmgmt.entity
 *	项目名称：	gamegold-goods
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午12:55:11
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.goods.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 商品信息EO（前台页面显示）
 * 
 * @author SunChengfei
 * 
 */
public class GoodsInfo extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 折扣列表
	 */
	private List<DiscountInfo> discountList;

	/**
	 * 游戏名称
	 */
	private String gameName;

	/**
	 * 游戏币名称
	 */
	private String moneyName;

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
	 * 商品名称
	 */
	private String title;

	/**
	 * 单价(1游戏币兑换多少元)
	 */
	private BigDecimal unitPrice;

	/**
	 * 发货速度(几分钟内可以发货，单位：分钟)
	 */
	private Integer deliveryTime;

	/**
	 * 图片地址
	 */
	private String imageUrls;

	/**
	 * 商品所属栏目
	 */
	private Integer goodsCat;

	/**
	 * 销量
	 */
	private Integer sales;

	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 是否禁用
	 */
	private Boolean isDeleted;
	
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
     * 可销售库存
     */
    private Integer sellableCount;
    
    /**
     * 库存所属卖家
     */
    private String sellerLoginAccount;

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

	public void setGoodsTypeId(Long goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}

	public Long getGoodsTypeId() {
		return goodsTypeId;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	/**
	 * @return the discountList
	 */
	public List<DiscountInfo> getDiscountList() {
		return discountList;
	}

	/**
	 * @param discountList
	 *            the discountList to set
	 */
	public void setDiscountList(List<DiscountInfo> discountList) {
		this.discountList = discountList;
	}

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName
	 *            the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
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
	 * @param moneyName
	 *            the moneyName to set
	 */
	public void setMoneyName(String moneyName) {
		this.moneyName = moneyName;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region
	 *            the region to set
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
	 * @param server
	 *            the server to set
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
	 * @param gameRace
	 *            the gameRace to set
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
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the unitPrice
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice
	 *            the unitPrice to set
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the deliveryTime
	 */
	public Integer getDeliveryTime() {
		return deliveryTime;
	}

	/**
	 * @param deliveryTime
	 *            the deliveryTime to set
	 */
	public void setDeliveryTime(Integer deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	/**
	 * @return the imageUrls
	 */
	public String getImageUrls() {
		return imageUrls;
	}

	/**
	 * @param imageUrls
	 *            the imageUrls to set
	 */
	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}

	/**
	 * @return the goodsCat
	 */
	public Integer getGoodsCat() {
		return goodsCat;
	}

	/**
	 * @param goodsCat
	 *            the goodsCat to set
	 */
	public void setGoodsCat(Integer goodsCat) {
		this.goodsCat = goodsCat;
	}

	/**
	 * @return the sales
	 */
	public Integer getSales() {
		return sales;
	}

	/**
	 * @param sales
	 *            the sales to set
	 */
	public void setSales(Integer sales) {
		this.sales = sales;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime
	 *            the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
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

    public Integer getSellableCount() {
        return sellableCount;
    }

    public void setSellableCount(Integer sellableCount) {
        this.sellableCount = sellableCount;
    }

	public String getSellerLoginAccount() {
		return sellerLoginAccount;
	}

	public void setSellerLoginAccount(String sellerLoginAccount) {
		this.sellerLoginAccount = sellerLoginAccount;
	}
    
    
}
