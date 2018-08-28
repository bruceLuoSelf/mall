package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * Created by ljn on 2018/4/10.
 */
public class PurchaserGameTrade extends BaseEntity{

    /**
     * 收货商游戏配置表id
     */
    private Long purchaserId;

    /**
     * 交易方式表id
     */
    private Long tradeTypeId;

    /**
     * 交易方式名称
     */
    private String tradeTypeName;

    /**
     * 交易标识
     */
    private Integer tradeLogo;

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public Integer getTradeLogo() {
        return tradeLogo;
    }

    public void setTradeLogo(Integer tradeLogo) {
        this.tradeLogo = tradeLogo;
    }

    public Long getPurchaserId() {
        return purchaserId;
    }

    public void setPurchaserId(Long purchaserId) {
        this.purchaserId = purchaserId;
    }

    public Long getTradeTypeId() {
        return tradeTypeId;
    }

    public void setTradeTypeId(Long tradeTypeId) {
        this.tradeTypeId = tradeTypeId;
    }
}
