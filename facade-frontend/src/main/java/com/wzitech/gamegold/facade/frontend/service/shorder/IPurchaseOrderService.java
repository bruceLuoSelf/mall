package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderResponse;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * 采购单
 * Created by 335854 on 2016/3/29.
 */
public interface IPurchaseOrderService {
    /**
     * 查询该采购方的分页采购单
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    IServiceResponse queryPurchaseOrderList(@QueryParam("")PurchaseOrderRequest purchaseOrderRequest, @Context
    HttpServletRequest request);

    /**
     * 批量设置采购单的上下架
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    IServiceResponse setPurchaseOrderOnline(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request);

    /**
     * 自动上架所以被下架的采购单
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
//    IServiceResponse setPurchaseOrderAutomatic(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request);

    /**
     * 修改当前采购单的采购单价和采购量
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    IServiceResponse updatePurchaseOrderPriceAndCount(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request);

    /**
     * 前台分页获取采购单列表
     * @param purchaseOrder
     * @param page
     * @param pageSize
     * @param fieldName
     * @param sortName
     * @param request
     * @return
     */
    PurchaseOrderResponse queryPurchaseOrder(@QueryParam("") PurchaseOrder purchaseOrder,
                                             @QueryParam("page") Integer page,
                                             @QueryParam("pageSize") Integer pageSize,
                                             @QueryParam("fieldName") String fieldName,
                                             @QueryParam("sort") String sortName,
                                             @Context HttpServletRequest request);

    /**
     * 上下架
     */
    IServiceResponse onlineAll(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request);

    /**
     * 批量删除采购单
     * @param purchaseOrderRequest
     * @return
     */
    IServiceResponse deleteAll(PurchaseOrderRequest purchaseOrderRequest,@Context HttpServletRequest request);

}
