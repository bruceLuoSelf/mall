package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.GameAccount;

/**
 * 自动配单
 *
 * @author yemq
 */
public interface IDeliveryOrderAutoConfigManager {
    /**
     * 自动配单
     *
     * @param id 出货单ID
     */
    void autoConfigOrderReady(long id);

    /**
     * 推送申诉单订单账号
     * @param order
     */
    void autoConfigAppealOrder(DeliveryOrder order);

    /**
     *
     * @param order
     */
    void autoConfigOrder(DeliveryOrder order);

    /**
     * 配单
     *
     * @param order
     * @param needCount 需要分配多少数量
     * @return long 返回创建的配单总游戏币数量
     */
    long configOrder(DeliveryOrder order, long needCount);

    boolean createSubOrder(DeliveryOrder order, GameAccount account, long configCount);
}
