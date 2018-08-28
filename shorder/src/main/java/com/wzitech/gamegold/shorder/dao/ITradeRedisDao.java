package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.Trade;

import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface ITradeRedisDao {

    public void save(Trade trade);

    public int delete(Trade trade);

    public List<Trade> selectById(String name);
}
