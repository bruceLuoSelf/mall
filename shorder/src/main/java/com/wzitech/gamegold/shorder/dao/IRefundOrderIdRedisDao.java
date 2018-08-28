package com.wzitech.gamegold.shorder.dao;

/**
 * 通过缓存生成退款订单号
 */
public interface IRefundOrderIdRedisDao {
    /**
     * 缓存中订单生成序列的KEY
     */
    public final static String ORDER_ID_SEQ = "CACHE_SH_REFUND_ORDER_ID_SEQ";

    /**
     * 生成订单号
     * @return
     */
    String getOrderId();
}
