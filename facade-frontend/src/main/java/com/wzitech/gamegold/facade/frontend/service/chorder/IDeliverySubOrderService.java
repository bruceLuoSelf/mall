package com.wzitech.gamegold.facade.frontend.service.chorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliverySubOrderRequest;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;

import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * 出货子订单
 * Created by jhlcitadmin on 2017/1/6.
 */

public interface IDeliverySubOrderService {

    /**
     * 查询所有的子订单
     */
    IServiceResponse findAll(@QueryParam("OrderId")String OrderId);


    IServiceResponse noQuestion(@QueryParam("SubOrderId")Long SubOrderId);

    /**
     * 根据子订单id查询子订单详情
     * */
    IServiceResponse findSubOrderById(@QueryParam("id")Long id);

}
