package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * 分仓日志
 */
public interface ISplitRepositoryLogService {
    /**
     * 日志
     * @param purchaseOrderRequest
     * @param page
     * @param pageSize
     * @param request
     * @return
     */
    IServiceResponse queryRepositoryLogList(@QueryParam("")PurchaseOrderRequest purchaseOrderRequest,  @QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize, @Context HttpServletRequest request);
}
