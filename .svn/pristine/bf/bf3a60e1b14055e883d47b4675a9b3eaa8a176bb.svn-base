package com.wzitech.gamegold.facade.frontend.service.order;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QuerySellerOrderRequest;

/**
 * 卖家订单接口
 * @author yemq
 */
public interface ISellerOrderService {

    /**
     * 分页查询卖家订单数据
     * @param params
     * @return
     */
    IServiceResponse queryOrders(QuerySellerOrderRequest params);

    /**
     * 分页查询卖家订单数据, 返回少量字段
     *
     * @param params
     * @return
     */
    IServiceResponse querySimpleOrderList(QuerySellerOrderRequest params);

    /**
     * 分页查询卖家订单数据, 返回少量字段,不需要验证登录权限，只供内部调用
     * @param params
     * @return
     */
    IServiceResponse querySimpleOrders(QuerySellerOrderRequest params);

    /**
     * 查询订单详情
     *
     * @param orderId 主订单号
     * @param subOrderId 子订单号
     * @return
     */
    IServiceResponse queryOrderDetail(String orderId, Long subOrderId);

    /**
     * 移交订单
     * @param orderId 主订单号
     * @param subOrderId 子订单号
     * @return
     */
    IServiceResponse transferOrder(String orderId, Long subOrderId);

    /**
     * 查询卖家是否有订单移交权限
     * @return
     */
    IServiceResponse hasTransferOrderPerm();
}
