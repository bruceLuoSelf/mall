package com.wzitech.gamegold.shorder.dto;

/**
 * Created by ljn on 2018/3/19.
 */
public class GameTradeConfigDTO {

    /**
     * 游戏名
     */
    private  String gameName;
    /**
     * 商品类型id
     */
    private Integer goodsTypeId;
    /**
     * 商品类型名称
     */
    private String goodsTypeName;
    /**
     * 收货游戏配置(收货方式)
     */
    private String shGameConfigs;
    /**
     * 收货游戏配置id(收货方式)
     */
    private String shGameConfigId;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(Integer goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getShGameConfigs() {
        return shGameConfigs;
    }

    public void setShGameConfigs(String shGameConfigs) {
        this.shGameConfigs = shGameConfigs;
    }

    public String getShGameConfigId() {
        return shGameConfigId;
    }

    public void setShGameConfigId(String shGameConfigId) {
        this.shGameConfigId = shGameConfigId;
    }
}
