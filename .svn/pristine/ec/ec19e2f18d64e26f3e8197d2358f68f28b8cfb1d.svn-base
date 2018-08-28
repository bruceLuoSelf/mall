package com.wzitech.gamegold.facade.service.complaint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wzitech.gamegold.facade.serializer.DateSerializer;

import java.util.Date;

public class OrderInfoItem {
    @JsonProperty("BasicType")
	private String basicType;

    @JsonProperty("Id")
	private String id;

    @JsonProperty("CreatedDate")
    @JsonSerialize(using = DateSerializer.class)
	private Date createTime;

    @JsonProperty("Name")
	private String title;

    @JsonProperty("GameName")
	private String gameName;

    @JsonProperty("AreaName")
	private String areaName;

    @JsonProperty("ServerName")
    private String serverName;

    @JsonProperty("RawSum")
	private String rawSum;

    @JsonProperty("OrderPayStatusValue")
    private int orderPayStatusValue;

	/**
	 * 通货类型ID
	 * lcs 5.15新增
	 */
	@JsonProperty("GoodsTypeId")
	private Long goodsTypeId;

	/**
	 * 通货类型名称
	 * lcs 5.15新增
	 */
	@JsonProperty("GoodsTypeName")
	private String goodsTypeName;

	public Long getGoodsTypeId(Long goodsTypeId) {
		return this.goodsTypeId;
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

	public String getBasicType() {
		return basicType;
	}

	public void setBasicType(String basicType) {
		this.basicType = basicType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getRawSum() {
		return rawSum;
	}

	public void setRawSum(String rawSum) {
		this.rawSum = rawSum;
	}

    public int getOrderPayStatusValue() {
        return orderPayStatusValue;
    }

    public void setOrderPayStatusValue(int orderPayStatusValue) {
        this.orderPayStatusValue = orderPayStatusValue;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
