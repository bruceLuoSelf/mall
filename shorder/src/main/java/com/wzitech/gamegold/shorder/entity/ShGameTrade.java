package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * Created by ljn on 2018/3/13.
 */
public class ShGameTrade extends BaseEntity{

    /**
     * 游戏配置表id
     */
    private Long gameTableId;

    /**
     * 交易配置表id
     */
    private Long tradeTableId;

    /**
     * 收货模式
     */
    private Integer shMode;

    /**
     * 游戏名:商品类型
     */
    private String gameGoodsTypeName;

    /**
     * 交易类型名称
     */
    private String tradeTypeName;

    /**
     * 交易模式
     */
    private Trade trade;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public String getGameGoodsTypeName() {
        return gameGoodsTypeName;
    }

    public void setGameGoodsTypeName(String gameGoodsTypeName) {
        this.gameGoodsTypeName = gameGoodsTypeName;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public void setTradeTypeName(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public Long getGameTableId() {
        return gameTableId;
    }

    public void setGameTableId(Long gameTableId) {
        this.gameTableId = gameTableId;
    }

    public Long getTradeTableId() {
        return tradeTableId;
    }

    public void setTradeTableId(Long tradeTableId) {
        this.tradeTableId = tradeTableId;
    }

    public Integer getShMode() {
        return shMode;
    }

    public void setShMode(Integer shMode) {
        this.shMode = shMode;
    }
}
