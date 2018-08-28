package com.wzitech.gamegold.jsrobot.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 订单退回请求
 * @author yemq
 */
public class SendBackOrderRequest extends AbstractServiceRequest {
    private String orderId;
    private Long subOrderId;
    /**
     * 退回原因
     */
    private String reason;
    private String sign;

    public SendBackOrderRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(Long subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
