

package com.wzitech.gamegold.order.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

public class ConfigPowerEO extends BaseEntity {
	
	/**
	 * 游戏名称
	 */
	private String gameName;
	
	/**
	 * 是否开启自动配单权限
	 */
	private Boolean configPower;
	
	/**
	 * 交易方式
	 */
	private Integer tradeType;
	/**
	 * 最大配单数额
	 */
	private Integer configMaxCount;

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

	public Integer getConfigMaxCount() {
		return configMaxCount;
	}

	public void setConfigMaxCount(Integer configMaxCount) {
		this.configMaxCount = configMaxCount;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Boolean getConfigPower() {
		return configPower;
	}

	public void setConfigPower(Boolean configPower) {
		this.configPower = configPower;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}
}
