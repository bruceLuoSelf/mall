package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;

import java.util.List;

/**
 * Created by 339939 on 2018/4/18.
 */
public interface IPurchaserGameTradeRedis {

    void add(Long id , List<PurchaserGameTrade> list);

    void delete(Long id);

    List<PurchaserGameTrade> getPurchaserGameTrade(Long id);

    void deleteAll();
}
