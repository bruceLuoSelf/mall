package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.PurchaseGame;

import java.util.List;
import java.util.Map;

/**
 * 收货商游戏配置RedisDao
 */
public interface IPurchaseGameRedisDao {

//    List<PurchaseGame> queryAll(Map<String,String> map);

    void save(PurchaseGame purchaseGame);

    int delete(PurchaseGame purchaseGame);

}
