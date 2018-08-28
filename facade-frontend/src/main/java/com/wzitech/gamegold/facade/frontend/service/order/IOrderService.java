/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		IOrderService
 * 包	名：		com.wzitech.gamegold.facade.frontend.service.order
 * 项目名称：	gamegold-facade-frontend
 * 作	者：		LuChangkai
 * 创建时间：	2014-1-14
 * 描	述：
 * 更新纪录：	1. LuChangkai 创建于 2014-1-14 下午4:52:59
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.order;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 订单管理服务端接口
 * @author LuChangkai
 *
 */
public interface IOrderService {

    /**
     * 按时间推送出售流程订单
     */
    void orderPushOrderCenterWithDate(Long dateBegin,Long dateEnd);


    /**
     * 收货订单推送订单中心
     */
    void deliveryOrderPushOrderCenter(Long dateBegin,Long dateEnd);


    /**
     * 创建订单页-合并该页所有查询
     * @param orderHtmlQueryRequest
     * @param request
     * @return
     */
    IServiceResponse orderHtmlQuery(OrderHtmlQueryRequest orderHtmlQueryRequest, HttpServletRequest request);

    /**
     * 新增订单
     * @param addOrderRequest
     * @param request
     * @return
     */
    IServiceResponse addOrder(AddOrderRequest addOrderRequest, HttpServletRequest request);

    /**
     * 修改订单状态为已收获
     * @param modifyOrderRequest
     * @param request
     * @return
     */
    IServiceResponse reciverOrder(ModifyOrderRequest modifyOrderRequest, HttpServletRequest request);

    /**
     * 订单超时
     * @param modifyOrderRequest
     * @param request
     * @return
     */
    IServiceResponse delayOrder(ModifyOrderRequest modifyOrderRequest, HttpServletRequest request);

    /**
     * 查询订单
     * @param queryOrderRequest
     * @param request
     * @return
     */
    IServiceResponse queryOrder(QueryOrderRequest queryOrderRequest, HttpServletRequest request);

    /**
     * 根据订单id查询订单
     * @param queryOrderByIdRequest
     * @param request
     * @return
     */
    IServiceResponse queryOrderById(QueryOrderRequest queryOrderByIdRequest, HttpServletRequest request);

    /**
     * 卖家查询所有订单
     * @param query
     * @param request
     * @return
     */
    IServiceResponse querySellerOrder(QueryConfigResultRequest querySellerOrder, HttpServletRequest request);

    /**
     * 获取每个用户最新5笔订单
     * @param queryOrderFiveRequest
     * @param request
     * @return
     */
    IServiceResponse queryOrderFive(QueryOrderRequest queryOrderFiveRequest, HttpServletRequest request);

    /**
     * 在redis最新5笔订单中，获取符合游戏信息的订单
     * @param queryNewOrder
     * @param request
     * @return
     */
    IServiceResponse queryNewOrder(QueryOrderRequest querynewRequest, HttpServletRequest request);

    /**
     * 验证优惠券是否有效
     * @param request
     * @return
     */
    IServiceResponse validateDiscountCoupon(ValidateDiscountCouponRequest request);

    /**
     * 根据订单号查询订单信息
     * @param orderId
     * @return
     */
    IServiceResponse queryOrder(String orderId);
}
