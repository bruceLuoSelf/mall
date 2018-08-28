package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 收货商游戏配置请求
 */
public class PurchaseGameRequest extends AbstractServiceRequest {
    //id
    private Long id;

    //收货商Id
    private Long purchaseId;

    //收货商名称
    private String purchaseAccount;

    //游戏名称
    private String gameName;

    //收货模式Id
    private Integer deliveryTypeId;

    //交易类目Id
    private Integer goodsTypeId;

    //交易方式Id
    private String tradeTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseAccount() {
        return purchaseAccount;
    }

    public void setPurchaseAccount(String purchaseAccount) {
        this.purchaseAccount = purchaseAccount;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getDeliveryTypeId() {
        return deliveryTypeId;
    }

    public void setDeliveryTypeId(Integer deliveryTypeId) {
        this.deliveryTypeId = deliveryTypeId;
    }

    public Integer getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(Integer goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getTradeTypeId() {
        return tradeTypeId;
    }

    public void setTradeTypeId(String tradeTypeId) {
        this.tradeTypeId = tradeTypeId;
    }
}
