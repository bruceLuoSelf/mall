package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.RemoveDeliveryOrderRequest;

/**
 * 撤单
 * Created by 340032 on 2017/12/27.
 */

public interface IDeliveryOrderService {
    public IServiceResponse removeShOrder(RemoveDeliveryOrderRequest request);

}
