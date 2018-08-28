package com.wzitech.gamegold.shrobot.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by 340032 on 2017/12/29.
 */
public class RobotResponse extends AbstractServiceResponse {
    /**
     * 子订单状态
     */
    private Boolean isMoilRount;

    /**
     * 撤单状态
     * @return
     */
    private Boolean withdraw;

    /**
     * 配置值
     */
    private String configValue;

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Boolean getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Boolean withdraw) {
        this.withdraw = withdraw;
    }

    public Boolean getIsMoilRount() {
        return isMoilRount;
    }

    public void setIsMoilRount(Boolean moilRount) {
        isMoilRount = moilRount;
    }
}
