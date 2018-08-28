/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		AnalysisUrlResponse
 * 包	名：		com.wzitech.gamegold.common.game.entity
 * 项目名称：	    gamegold-common
 * 作	者：		SunChengfei
 * 创建时间：	    2014年2月24日
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014年2月24日 下午8:56:30
 ************************************************************************************/
package com.wzitech.gamegold.common.game.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 解析页面Url，返回信息
 * @author SunChegnfei
 *
 * Update History
 * DATE           NAME       REASON FOR UPDATE
 * ---------------------------------------------
 * 2017.5.12     339928     ZW_C_JB_00008 商城增加通货
 *
 */
public class AnalysisUrlResponse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏区
     */
    private String gameRegion;

    /**
     * 游戏服
     */
    private String gameServer;

    /**
     * 游戏阵营
     */
    private String gameRace;

    /**
     *  商品类目
     */
    private String goodsTypeName;


    @JsonProperty("gc")
    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public String getGameName() {
        return gameName;
    }

    @JsonProperty("gm")
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameRegion() {
        return gameRegion;
    }

    @JsonProperty("ga")
    public void setGameRegion(String gameRegion) {
        this.gameRegion = gameRegion;
    }

    public String getGameServer() {
        return gameServer;
    }

    @JsonProperty("gs")
    public void setGameServer(String gameServer) {
        this.gameServer = gameServer;
    }

    public String getGameRace() {
        return gameRace;
    }

    @JsonProperty("gr")
    public void setGameRace(String gameRace) {
        this.gameRace = gameRace;
    }

}
