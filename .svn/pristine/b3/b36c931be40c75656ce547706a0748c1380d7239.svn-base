package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReadAggreRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * 采购商数据
 * Created by 335854 on 2016/3/29.
 */
public interface IPurchaseDataService {

    /**
     * 获取当前收货商数据
     * @return
     */
    IServiceResponse getCurrentPurchaserData();

    /**
     * 修改收货方式
     * @return
     */
    IServiceResponse updateDeliveryType(@QueryParam("deliveryType") int deliveryType);

    /**
     * 修改收货方式
     * @return
     */
    IServiceResponse updateTradeType(@QueryParam("tradeType")String tradeType,@QueryParam("tradeTypeName")String tradeTypeName);

    /**
     * 设置阅读字段
     * @param readAggreRequest
     * @param request
     * @return
     */
    IServiceResponse setReadAggrement(ReadAggreRequest readAggreRequest, @Context HttpServletRequest request);

    /**
     *收货商是否阅读协议/新旧资金开关接口
     * @param request
     * @return
     */
    IServiceResponse getFundInfo();
}
