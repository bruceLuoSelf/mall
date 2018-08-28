package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * Created by jhlcitadmin on 2017/2/23.
 */
public class MainGameConfig extends BaseEntity {
    //游戏名称
    private String gameName;

    //游戏ID
    private String gameId;

    //是否开启收货
    private Boolean ableDelivery;

    //是否开启出售
    private Boolean ableSell;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameID) {
        this.gameId = gameID;
    }

    public Boolean getAbleDelivery() {
        return ableDelivery;
    }

    public void setAbleDelivery(Boolean ableDelivery) {
        this.ableDelivery = ableDelivery;
    }

    public Boolean getAbleSell() {
        return ableSell;
    }

    public void setAbleSell(Boolean ableSell) {
        this.ableSell = ableSell;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
