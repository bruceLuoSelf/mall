package com.wzitech.gamegold.common.log.entity;

/**
 * 订单日志实体
 * @author yemq
 */
public class OrderLogInfo extends BaseLogInfo {

    /**
     * 订单ID
     */
    private String orderId;

    public OrderLogInfo(){}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
