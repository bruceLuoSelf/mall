package com.wzitech.gamegold.order.business;

import java.util.Date;

/**
 * Created by 339928 on 2017/12/7.
 */
public interface IOrderCenterWithDate {
    void autoSellOrderToOrderCenterWithDate(Long dateBegin,Long dateEnd);
}
