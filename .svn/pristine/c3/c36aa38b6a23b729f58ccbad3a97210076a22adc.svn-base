package com.wzitech.gamegold.shrobot.service.repository;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.shrobot.service.order.dto.GameAccountRequest;
import com.wzitech.gamegold.shrobot.service.order.dto.OrderListRequest;
import com.wzitech.gamegold.shrobot.service.order.dto.SplitOrderStateRequest;
import com.wzitech.gamegold.shrobot.service.repository.dto.ModifyRepositoryRequest;
import com.wzitech.gamegold.shrobot.service.repository.dto.WriteRepositoryLogRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 分仓库存管理接口
 */
public interface IRepositoryService {
    /**
     * 分仓订单列表
     *
     * @param params
     * @param request
     * @return
     */
     IServiceResponse orderList(OrderListRequest params, HttpServletRequest request);

    /**
     * 更新库存
     * @param params
     * @return
     */
    IServiceResponse update(ModifyRepositoryRequest params);

    /**
     * 写分仓日志
     * @param params
     * @return
     */
    IServiceResponse writeLog(WriteRepositoryLogRequest params);

    /**
     * 更改分仓请求状态
     * @param request
     * @return
     */
    IServiceResponse changeState(SplitOrderStateRequest request);

    /**
     * 提供盘库帐号信息列表
     *
     * @param pageSize
     * @param sign
     * @return
     */
    IServiceResponse gameAccountList(Integer pageSize,String sign);

    /**
     * 盘库
     *
     * @param gameAccountRequest
     * @return
     */
    IServiceResponse updateAccountStore(GameAccountRequest gameAccountRequest);
}
