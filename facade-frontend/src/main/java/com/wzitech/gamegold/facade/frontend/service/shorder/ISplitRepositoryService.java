package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.SplitRepositoryOrderRequest;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

/**
 * @author ljn
 * @date 2018/6/12.
 */
public interface ISplitRepositoryService {

    /**
     * 查询分仓订单列表
     * @param repositoryRequest
     * @param request
     * @return
     */
    IServiceResponse queryOrderList(@RequestBody SplitRepositoryOrderRequest repositoryRequest,HttpServletRequest request);

    /**
     * 根据分仓订单号查询订单详情
     * @param orderNo
     * @return
     */
    IServiceResponse queryOrderDetails(@QueryParam("orderNo") String orderNo);

    /**
     * 导出分仓订单列表
     * @param repositoryRequest
     * @param request
     * @param response
     * @return
     */
    IServiceResponse export(SplitRepositoryOrderRequest repositoryRequest,HttpServletRequest request,HttpServletResponse response);
}
