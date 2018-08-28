package com.wzitech.gamegold.rc8.service.order;

import com.wzitech.gamegold.rc8.dto.Response;
import com.wzitech.gamegold.rc8.service.order.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * RC MD5加密
 * Created by 340032 on 2018/2/6.
 */
public interface IOrderNewService {
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
    QueryOrderInfoResponse queryOrderInfo(String orderId, QueryOrderInfoRequest request);

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
