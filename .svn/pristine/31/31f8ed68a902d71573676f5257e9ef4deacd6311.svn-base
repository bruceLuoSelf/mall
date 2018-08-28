package com.wzitech.gamegold.facade.frontend.service.order;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QuerySellerOrderRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/05  wubiao              新增cookie校验
 */
public interface ICheckSellerOrderService {

    /**
     * 分页查询卖家订单数据, 返回少量字段
     *
     * @param params
     * @return
     */
    IServiceResponse queryOrderList(QuerySellerOrderRequest params,HttpServletRequest request);

    /**
     * 查询订单详情
     *
     * @param orderId 主订单号
     * @param subOrderId 子订单号
     * @return
     */
    IServiceResponse queryOrderDetail(String orderId, Long subOrderId,HttpServletRequest request);

    /**
     * 移交订单
     * @param orderId 主订单号
     * @param subOrderId 子订单号
     * @return
     */
    IServiceResponse transferOrderMethod(String orderId, Long subOrderId,HttpServletRequest request);

}
