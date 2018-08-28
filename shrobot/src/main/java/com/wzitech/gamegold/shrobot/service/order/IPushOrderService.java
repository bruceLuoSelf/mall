package com.wzitech.gamegold.shrobot.service.order;

import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;

import java.io.IOException;

/**
 * 出货单推送服务
 */
public interface IPushOrderService {
    /**
     * 推送订单
     * @param order
     * @throws IOException
     */
    void pushOrder(DeliverySubOrder order) throws IOException;
}
