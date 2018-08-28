package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.Stock;

/**
 * Created by 340032 on 2018/6/25.
 */
public interface IStockManager {

    /**
     * 更新盘存表数据
     *
     * @param Stock
     */
    void update(Stock Stock);

    /**
     * 根据订单ID查询分仓
     *
     * @param
     * @return
     */
    Stock queryByOrderId(Long subOrderId);


    void add(Stock stock);
}
