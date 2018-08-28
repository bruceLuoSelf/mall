package com.wzitech.gamegold.repository.business;

/**
 * Created by 汪俊杰 on 2017/6/21.
 */
public interface IShrobotRefundOrderManager {
    /**
     * 自动退款超过三个月的充值订单
     *
     * @param payOrderId
     * @param loginAccount
     */
    void autoRefundTimeoutPayOrder(String payOrderId, String loginAccount, int day);

    /**
     * 自动退款超过三个月的充值订单(禁止调用)
     *
     * @param payOrderId
     * @param loginAccount
     */
    void autoRefundTimeoutPayOrderPrivate(String payOrderId, String loginAccount, int day);
}
