package com.wzitech.gamegold.gamegold.app.service.order;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.gamegold.app.service.order.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.math.BigDecimal;

/**
 * 订单管理服务端接口
 * Created by lcs on 2017/2/15.
 */
public interface IOrderService {

    /**
     * 根据游戏获取保险配置
     *
     * @param orderInsuranceQueryRequest
     * @param request
     * @return
     */

    IServiceResponse orderQueryInsurance(OrderInsuranceQueryRequest orderInsuranceQueryRequest, @Context HttpServletRequest request);
    /**
     * 新增订单
     * @param addOrderRequest
     * @param request
     * @return
     */
    IServiceResponse addOrder(AddOrderRequest addOrderRequest, @Context HttpServletRequest request);

    /**
     * 新增订单(有单价修改)
     * @param addOrderRequest
     * @param request
     * @return
     */
    IServiceResponse addOrderWithUnitPrice(AddOrderRequest addOrderRequest, @Context HttpServletRequest request);
    /**
     * 查询订单列表
     * @param queryOrderRequest
     * @param request
     * @return
     */
    IServiceResponse queryOrder(QueryOrderRequest queryOrderRequest, @Context HttpServletRequest request);


    /**
     * 根据多种状态查询订单列表
     * @param queryOrderRequest
     * @param request
     * @return
     */
    IServiceResponse queryOrderByType(QueryOrderTypeRequest queryOrderRequest, @Context HttpServletRequest request);


    /**
     * 根据订单号获取订单详情
     * @param queryOrderByIdRequest
     * @param request
     * @return
     */
    IServiceResponse queryOrderById(QueryOrderRequest queryOrderByIdRequest, @Context HttpServletRequest request);

    /**
     * app新增订单(有单价修改)
     * @param addOrderRequest
     * @param request
     * @return
     */
    IServiceResponse addOrderWithUnitPriceForApp(AddOrderRequest addOrderRequest, @Context HttpServletRequest request);

}
