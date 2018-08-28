package com.wzitech.gamegold.facade.frontend.service.repository.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;

import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/16.
 */
public class MConnectionResponse extends AbstractServiceResponse {
    /**
     * 参考价
     */
    private String unitPrice;

    /**
     * 总库存
     */
    private String totalCount;

    /**
     * 单位
     */
    private String moneyName;

    private String mAddMsg;

    private ShGameConfig shGameConfig;

    public String getmAddMsg() {
        return mAddMsg;
    }

    public void setmAddMsg(String mAddMsg) {
        this.mAddMsg = mAddMsg;
    }

    public ShGameConfig getShGameConfig() {
        return shGameConfig;
    }

    public void setShGameConfig(ShGameConfig shGameConfig) {
        this.shGameConfig = shGameConfig;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }
}
