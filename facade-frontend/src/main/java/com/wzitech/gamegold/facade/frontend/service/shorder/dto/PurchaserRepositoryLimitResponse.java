package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * @author ljn
 * @date 2018/6/12.
 */
public class PurchaserRepositoryLimitResponse extends AbstractServiceResponse {

    /**
     * 是否分仓
     */
    private Boolean isSplit;
    /**
     * 收货商库存上限配置
     */
    private Long purchaserRepositoryCount;
    /**
     * 收货商库存缺口配置
     */
    private Long purchaserNeedCount;
    /**
     * 游戏库存上限配置
     */
    private Long gameRepositoryCount;
    /**
     * 游戏库存缺口配置
     */
    private Long gameNeedCount;

    /**
     * 分仓总开关
     */
    private Boolean splitRepositorySwitch;

    public Boolean getSplitRepositorySwitch() {
        return splitRepositorySwitch;
    }

    public void setSplitRepositorySwitch(Boolean splitRepositorySwitch) {
        this.splitRepositorySwitch = splitRepositorySwitch;
    }

    public Boolean getIsSplit() {
        return isSplit;
    }

    public void setIsSplit(Boolean split) {
        isSplit = split;
    }

    public Long getPurchaserRepositoryCount() {
        return purchaserRepositoryCount;
    }

    public void setPurchaserRepositoryCount(Long purchaserRepositoryCount) {
        this.purchaserRepositoryCount = purchaserRepositoryCount;
    }

    public Long getPurchaserNeedCount() {
        return purchaserNeedCount;
    }

    public void setPurchaserNeedCount(Long purchaserNeedCount) {
        this.purchaserNeedCount = purchaserNeedCount;
    }

    public Long getGameRepositoryCount() {
        return gameRepositoryCount;
    }

    public void setGameRepositoryCount(Long gameRepositoryCount) {
        this.gameRepositoryCount = gameRepositoryCount;
    }

    public Long getGameNeedCount() {
        return gameNeedCount;
    }

    public void setGameNeedCount(Long gameNeedCount) {
        this.gameNeedCount = gameNeedCount;
    }
}
