package com.wzitech.gamegold.shorder.dao;

import java.util.concurrent.TimeUnit;

/**
 * 通过缓存生成出货订单号
 */
public interface IDeliveryOrderIdRedisDao {
    /**
     * 缓存中订单生成序列的KEY
     */
    public final static String ORDER_ID_SEQ = "CACHE_SH_DELIVERY_ORDER_ID_SEQ";

    /**
     * 生成订单号
     * @return
     */
    String getOrderId();

    /**
     * 新资金：生成订单号
     * */
    String getNewFundOrderId();

    /**
     * 锁
     */
    boolean lock(long timeout, TimeUnit timeUnit, String lockKey);

    /**
     * 关锁
     */
    boolean unlock(String unlockKey );
}
