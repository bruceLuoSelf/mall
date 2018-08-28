package com.wzitech.gamegold.facade.frontend.service.sync.dto;

import java.math.BigDecimal;

/**
 * Created by 340082 on 2018/6/13.
 */
public class SyncRepositoryRequest {

    private String gameName;
    private String  region;
    private String server;
    private String gameAccount;
    private String gameRole;
    private BigDecimal price;
    private Long sellableGoldCount;
    private BigDecimal totalPrice;
    private Long shGameAccountId;

    public Long getShGameAccountId() {
        return shGameAccountId;
    }

    public void setShGameAccountId(Long shGameAccountId) {
        this.shGameAccountId = shGameAccountId;
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

    public String getGameAccount() {
        return gameAccount;
    }

    public void setGameAccount(String gameAccount) {
        this.gameAccount = gameAccount;
    }

    public String getGameRole() {
        return gameRole;
    }

    public void setGameRole(String gameRole) {
        this.gameRole = gameRole;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getSellableGoldCount() {
        return sellableGoldCount;
    }

    public void setSellableGoldCount(Long sellableGoldCount) {
        this.sellableGoldCount = sellableGoldCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
