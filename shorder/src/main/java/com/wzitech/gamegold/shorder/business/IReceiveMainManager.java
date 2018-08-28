package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.DeliveryOrder;

/**
 * Created by chengXY on 2017/8/22.
 */
public interface IReceiveMainManager {
    /**
     * 新资金 增加出货单卖家余额
     * */
    public void plusAmount(DeliveryOrder deliveryOrder);
}
