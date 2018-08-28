/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		IOrderService
 * 包	名：		com.wzitech.gamegold.facade.service.order
 * 项目名称：	gamegold-facade
 * 作	者：		SunChengfei
 * 创建时间：	2014-1-14
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-1-14 下午4:52:59
 ************************************************************************************/
package com.wzitech.gamegold.jsrobot.service.order;


import com.wzitech.gamegold.jsrobot.dto.Response;
import com.wzitech.gamegold.jsrobot.service.order.dto.*;

/**
 * 订单管理服务端接口
 *
 * @author yemq
 */
public interface IOrderService {

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
     * @param request
     * @return
     */
    QueryOrderInfoResponse queryOrderInfo(QueryOrderInfoRequest request);

    /**
     * 订单移交
     * @param request
     * @return
     */
    Response transfer(TransferOrderRequest request);

    /**
     * 订单退回
     * @param request
     * @return
     */
    Response sendback(SendBackOrderRequest request);


}
