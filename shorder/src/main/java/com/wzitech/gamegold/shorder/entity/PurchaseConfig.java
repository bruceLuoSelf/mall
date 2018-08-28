package com.wzitech.gamegold.shorder.entity;

import java.util.List;

/**
 * Created by 汪俊杰 on 2018/1/9.
 */
public class PurchaseConfig {
    private ShGameConfig gameConfig;

    private List<DeliveryConfig> deliveryConfigList;

    private List<Config> configList;

    private List<Trade> tradeList;

    public List<Trade> getTradeList() {
        return tradeList;
    }

    public void setTradeList(List<Trade> tradeList) {
        this.tradeList = tradeList;
    }

    public ShGameConfig getGameConfig() {
        return gameConfig;
    }

    public void setGameConfig(ShGameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public List<DeliveryConfig> getDeliveryConfigList() {
        return deliveryConfigList;
    }

    public void setDeliveryConfigList(List<DeliveryConfig> deliveryConfigList) {
        this.deliveryConfigList = deliveryConfigList;
    }

    public List<Config> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Config> configList) {
        this.configList = configList;
    }
}
