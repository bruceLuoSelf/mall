package com.wzitech.gamegold.facade.service.receiving;

import com.wzitech.gamegold.facade.service.receiving.dto.*;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * Created by 340082 on 2017/5/5.
 */

public interface IReceivingService {

    /**
     * 根据订单id查询出货商订单详情
     *
     * @param queryOrderDetaileRequest
     * @param request
     * @return
     */
    QueryOrderDetaileResponse getOrderDetails(QueryOrderDetaileRequest queryOrderDetaileRequest, HttpServletRequest request);


    /**
     * 邮寄收货获取订单详情接口
     *
     * @param queryOrderDetaileRequest
     * @param request
     * @return
     */
    QueryMailOrderDetailResponse getMailOrderDetails(QueryOrderDetaileRequest queryOrderDetaileRequest, HttpServletRequest request);


    /**
     * 根据订单id查询到的状态
     *
     * @param queryOrderDetaileRequest
     * @param request
     * @return
     */
    QueryOrderDetaileResponse getOrderStatus(QueryOrderDetaileRequest queryOrderDetaileRequest, HttpServletRequest request);

    /**
     * 获取邮寄收货订单状态
     *
     * @param queryOrderDetaileRequest
     * @param request
     * @return
     */
    QueryMailOrderDetailResponse getOrderStatusWithMailDelivery(QueryOrderDetaileRequest queryOrderDetaileRequest, HttpServletRequest request);


    /**
     * 查询异常转人工订单列表
     *
     * @param orderListRequest
     * @param request
     * @return
     */
    QueryMachineAbnormalTurnManualOrderListResponse queryMachineAbnormalTurnManualOrderList(QueryMachineAbnormalTurnManualOrderListRequest orderListRequest, HttpServletRequest request);

    /**
     * 邮寄收货查询子订单列表接口
     *
     * @param orderListRequest
     * @param request
     * @return
     */
    QueryMachineAbnormalTurnManualOrderListResponse queryMachineAbnormalTurnManualOrderListWithMailDeliveryOrder(QueryMachineAbnormalTurnManualOrderListRequest orderListRequest, HttpServletRequest request);

    /**
     * 物服完单
     *
     * @param logisticsSheetRequest
     * @param request
     * @return
     */
    LogisticsSheetResponse logisticsSheet(LogisticsSheetRequest logisticsSheetRequest, HttpServletRequest request);

    /**
     * |
     * 推送消息实现
     *
     * @param pushMessageRequest
     * @param request
     * @return
     */
    PushMessageResponse pushMessage(PushMessageRequest pushMessageRequest, HttpServletRequest request);

    /**
     * 取消订单接口
     *
     * @param cancellationOfOrderRequest
     * @param request
     * @return
     */
    CancellationOfOrderResponse cancellationOfOrder(CancellationOfOrderRequest cancellationOfOrderRequest, HttpServletRequest request);

    /**
     * 获取取消订单接口原因
     *
     * @param
     * @param request
     * @return
     */
    String cancellationOrderReason(HttpServletRequest request);

    /**
     * 邮寄收货完单接口
     */
    LogisticsSheetResponse mailDeliveryManualOper(LogisticsSheetRequest logisticsSheetRequest, HttpServletRequest request);

    /**
     * 拍卖交易上传图
     *
     * @param file       图片文件流
     * @param subOrderId 子订单id  例如"5122"
     * @param orderId    主订单orderId  例如"SH107018451144"
     * @param request
     * @return
     */
    UploadPicsResponse uploadImage(@Multipart(value = "file", required = false) byte[] file,
                                   @Multipart(value = "subOrderId", required = false) Long subOrderId,
                                   @Multipart(value = "orderId", required = false) String orderId,
                                   @Context HttpServletRequest request);

    PreventAccountConflictResponse preventAccountConflict(@QueryParam("gameName") String gameName,
                                                          @QueryParam("region") String region,
                                                          @QueryParam("server") String server,
                                                          @QueryParam("gameRace") String gameRace,
                                                          @QueryParam("gameRole") String gameRole,
                                                          @QueryParam("loginAccount") String loginAccount,
                                                          @QueryParam("gameAccount") String gameAccount);
}
