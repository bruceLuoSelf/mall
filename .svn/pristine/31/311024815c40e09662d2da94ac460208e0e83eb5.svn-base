package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * @author 340096
 * @date 2018/1/24.
 * 7bao补单，请求商城的DTO
 */
public class DealFallOrderRequest extends AbstractServiceRequest {
    /**
     * 7bao请求过来的订单号 可以是商城订单号（YX），也可以是主站订单号（N）
     * */
    private String orderId;

    /**
     * 订单号类型  1:商城订单号   2:主站订单号
     * */
    private int orderType;

    /**
     * MD5加密sign
     * */
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
