package com.wzitech.gamegold.facade.frontend.service.order;


import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.EvaluateRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public interface IServiceEvaluateService {
    /**
     * 根据订单查找评价信息
     * @param evaluateRequest
     * @param request
     * @return
     */
    IServiceResponse queryByOrderId(EvaluateRequest evaluateRequest, @Context HttpServletRequest request);

    /**
     * 新增评价
     * @param evaluateRequest
     * @param request
     * @return
     */
    IServiceResponse addEvaluate(EvaluateRequest evaluateRequest, HttpServletRequest request);

    /**
     * 吸怪评价
     * @param evaluateRequest
     * @param request
     * @return
     */
    IServiceResponse modifyEvaluate(EvaluateRequest evaluateRequest, HttpServletRequest request);
}
