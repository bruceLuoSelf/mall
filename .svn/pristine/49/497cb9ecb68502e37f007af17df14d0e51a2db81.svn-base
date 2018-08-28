package com.wzitech.gamegold.repository.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 卖家入驻选择的游戏
 * @author yemq
 */
public class SellerGame extends BaseEntity {
    /**
     * 卖家ID
     */
    private Long sellerId;
    /**
     * 选择的游戏ID
     */
    private String gameId;

    /**
     * 选择的游戏名称(不映射数据库)
     */
    private String gameName;

    public SellerGame() {}

    public SellerGame(Long sellerId, String gameId) {
        this.sellerId = sellerId;
        this.gameId = gameId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
