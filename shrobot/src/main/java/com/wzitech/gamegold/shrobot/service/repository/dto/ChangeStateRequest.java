package com.wzitech.gamegold.shrobot.service.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 更改分仓请求状态
 */
public class ChangeStateRequest extends AbstractServiceRequest {
    /**
     * 分仓订单号
     */
    @JsonProperty("order_id")
    private String orderId;
    /**
     * 状态
     * <li>1：分仓中</li>
     * <li>2：分仓结束</li>
     */
    private Integer status;
    private String sign;

    public ChangeStateRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
