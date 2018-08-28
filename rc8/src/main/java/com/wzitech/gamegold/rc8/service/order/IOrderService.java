/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *
 *	模	块：		IOrderService
 *	包	名：		com.wzitech.gamegold.facade.service.order
 *	项目名称：	gamegold-facade
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-14 下午4:52:59
 *
 ************************************************************************************/
package com.wzitech.gamegold.rc8.service.order;

import com.wzitech.gamegold.rc8.dto.Response;
import com.wzitech.gamegold.rc8.service.order.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * 订单管理服务端接口
 *
 * @author yemq
 */
public interface IOrderService {
    /**
     * 获取订单取消可选的理由列表
     *
     * @return
     */
    String getCancelOrderReasonXml(String name, String version, String sign) throws UnsupportedEncodingException;

    /**
     * 查询订单列表
     *
     * @param request
     * @return
     */
    QueryOrderListResponse queryOrderList(QueryOrderListRequest request);

    /**
     * 查询订单详情
     *
     * @param orderId
     * @param request
     * @return
     */
    String queryOrderInfo(String orderId, QueryOrderInfoRequest request);

    /**
     * 订单移交
     * @param orderId
     * @param request
     * @return
     */
    Response transfer(String orderId, TransferOrderRequest request);

    /**
     * 订单结单
     * @param orderId
     * @param request
     * @return
     */
    Response statement(String orderId, StatementOrderRequest request);

    /**
     * 订单退款
     * @param orderId
     * @param request
     * @return
     */
    Response refund(String orderId, RefundOrderRequest request);

    /**
     * 获取退款原因
     * @param request
     * @return
     */
    String refundReasonList(HttpServletRequest request) throws UnsupportedEncodingException;

}
