package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

import java.math.BigDecimal;

/**
 * 支付单操作请求
 */
public class ReadAggreRequest extends AbstractServiceRequest {
    private Boolean isAgree;

    public Boolean getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(Boolean agree) {
        isAgree = agree;
    }
}
