package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderResponse;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.InputStream;

/**
 * 手动收货订单
 * Created by 335854 on 2016/3/29.
 */
public interface IManualDeliveryOrderService {

    /**
     * 开始交易(我已登录游戏)，for手动模式
     *
     * @param id
     * @return
     */
    IServiceResponse startTradingForManual(@QueryParam("id") Long id);

    /**
     * 手动收货，收货商分配收货角色
     *
     * @param request
     * @return
     */
    IServiceResponse assignGameAccount(DeliveryOrderRequest request);

    /**
     * 手动收货，子订单收货商确认收货
     *
     * @param subOrderId
     * @return
     */
    IServiceResponse confirmReceived(@QueryParam("subOrderId")Long subOrderId);

    /**
     * 手动收货，已发货状态，收货商取消子订单
     *
     * @param subOrderId
     * @return
     */
    IServiceResponse cancelSubOrder(@QueryParam("subOrderId")Long subOrderId);

    /**
     * 出货商取消子订单
     *
     * @param subOrderId
     * @return
     */
    IServiceResponse cancelSubOrderBySeller(@QueryParam("subOrderId")Long subOrderId);

    /**
     * 手动收货，待发货状态，出货商确认发货
     *
     * @param subOrderId
     * @return
     */
    IServiceResponse confirmDelivery(@QueryParam("subOrderId")Long subOrderId, @QueryParam("realCount")Long realCount);

    /**
     * 根据账号查询信息
     */
    IServiceResponse selectOrderByAccount(@QueryParam("account")String account);

    /**
     * 修改当前采购单的采购单价和采购量和最低采购数量
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    IServiceResponse updatePurchaseOrderPriceAndCountAndNum(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request);

    /**
     * 上下架
     * @returnids
     */
    IServiceResponse setOnline(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request);

    IServiceResponse getMyIp(@Context HttpServletRequest request);


    /**
     * 前台分页获取采购单列表
     * @param purchaseOrder
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    PurchaseOrderResponse queryOrder(@QueryParam("") PurchaseOrder purchaseOrder,
                                             @QueryParam("page") Integer page,
                                             @QueryParam("pageSize") Integer pageSize,
                                             @Context HttpServletRequest request);

    /**
     * 拍卖交易上传图
     * @param file 图
     * @param subOrderId 子订单id
     * @param orderId 主订单orderId
     * @param request
     * @return
     */
    IServiceResponse uploadImage(@Multipart(value = "file",required = false)byte[] file,
                                 @Multipart(value = "subOrderId",required = false)Long subOrderId,
                                 @Multipart(value = "orderId",required = false)String orderId,
                                 @Context HttpServletRequest request);
}
