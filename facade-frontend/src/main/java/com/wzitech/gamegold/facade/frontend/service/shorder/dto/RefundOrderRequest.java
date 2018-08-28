package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 *退款请求
 */
public class RefundOrderRequest extends AbstractServiceRequest {
    /**
     * 充值单号
     */
    private String payOrderId;

    /**
     * 退款原因
     */
    private String reason;

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
