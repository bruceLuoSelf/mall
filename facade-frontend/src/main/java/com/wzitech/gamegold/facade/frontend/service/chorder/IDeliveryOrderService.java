package com.wzitech.gamegold.facade.frontend.service.chorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.CreateDeliveryOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * 出货订单服务
 */
public interface IDeliveryOrderService {
    String get(@Context HttpServletRequest request);

    /**
     * 创建出货单
     * @param params
     * @return
     */
    IServiceResponse createDeliveryOrder(CreateDeliveryOrderRequest params,@Context HttpServletRequest request);

    /**
     * 获取当前 采购单以及采购商信息
     * @param id
     * @return
     */
    IServiceResponse selectPurchaseOrderAndCgDataById(@QueryParam("id") Long id);

    /**
     * 分页查找订单
     * @param params
     * @param page
     * @param pageSize
     * @param fieldName
     * @param isAsc
     * @param request
     * @return
     */
     IServiceResponse selectDeliverOrderInPage(DeliveryOrderRequest params,
                                                     @QueryParam("page") Integer page,
                                                     @QueryParam("pageSize") Integer pageSize,
                                                     @QueryParam("fieldName") String fieldName,
                                                     @QueryParam("isAsc") Boolean isAsc,
                                                     @QueryParam("isSell") Boolean isSell,
                                                     @Context HttpServletRequest request);

    /**
     * 统计金额和数量
     * @param params
     * @param request
     * @return
     */
    IServiceResponse statisAmountAndCount(@QueryParam("") DeliveryOrderRequest params,@QueryParam("isSell") Boolean isSell,
                                          @Context HttpServletRequest request);

    /**
     * 导出数据
     * @param params
     * @param request
     * @return
     */
    void exportOrder(@QueryParam("") DeliveryOrderRequest params,
                                 @QueryParam("isSell") Boolean isSell,
                                 @Context HttpServletRequest request,
                                 @Context HttpServletResponse servletRespone);

    /**
     * 根据id查找对应的订单
     * @param id
     * @param request
     * @return
     */
    IServiceResponse selectOrderById(@QueryParam("orderId") String orderId,@Context HttpServletRequest request);

    /**
     * 开始交易(我已登录游戏)
     * @param id
     * @param request
     * @return
     */
    IServiceResponse startTrading(@QueryParam("id") Long id,@Context HttpServletRequest request);


    /**
     * 查找该订单的日志
     * @param id
     * @param request
     * @return
     */
    IServiceResponse getLogByChId(@QueryParam("id") String id, @Context HttpServletRequest request);

    /**
     * 撤单
     * @param id
     * @param reason
     * @param remark
     * @param request
     * @return
     */
    IServiceResponse cancelOrder(@QueryParam("id") Long id, @QueryParam("id")int reason, @QueryParam("id")String remark, @Context HttpServletRequest request);

    /**
     * 撤单 子订单使用
     * @param id
     * @param reason
     * @param remark
     * @param request
     * @return
     */
    IServiceResponse cancelSubOrder(@QueryParam("subOrderId") Long id, @QueryParam("reason") int reason, @QueryParam("remark") String remark, @Context HttpServletRequest request);

    /**
     * 申请部分完成
     * @param id
     * @param request
     * @return
     */
    IServiceResponse applyForCompletePart(@QueryParam("id") Long id,@Context HttpServletRequest request);

    /**
     *根据主订单id查找子订单列表
     * @param chId
     * @param request
     * @return
     */
    IServiceResponse getDeliverySubOrderList(@QueryParam("chId") Long chId, @Context HttpServletRequest request);

    /**
     * 根据主订单id查询收货商订单
     */
    IServiceResponse selectBuyerOrderById(@QueryParam("orderId") String orderId,@Context HttpServletRequest request);

    /**
     * 确认订单发货数量
     * @param json
     */
    IServiceResponse confirmShipment (@QueryParam("json") String json ) throws IOException;

    /**
     * 机器收货单交易中出现异常需要转人工（寄售物服）处理
     * */
    IServiceResponse autoDistributionService(@QueryParam("id") Long orderId,@Context HttpServletRequest request);

    /**
     * 创建申诉单
     * @param params
     * @param request
     * @return
     */
    IServiceResponse createAppealOrder(CreateDeliveryOrderRequest params, @Context HttpServletRequest request);

}
