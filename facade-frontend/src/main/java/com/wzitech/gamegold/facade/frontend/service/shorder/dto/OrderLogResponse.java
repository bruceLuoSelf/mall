package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.OrderLog;

import java.util.List;

/**
 * Created by Administrator on 2017/2/17.
 */
public class OrderLogResponse extends AbstractServiceResponse {

    private List<OrderLog> orderLogs;

    public List<OrderLog> getOrderLogs() {
        return orderLogs;
    }

    public void setOrderLogs(List<OrderLog> orderLogs) {
        this.orderLogs = orderLogs;
    }
}
