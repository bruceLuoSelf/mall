package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReadAggreRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 * Created by wangmin
 * Date:2017/8/21
 * 是否阅读协议接口
 */
public interface ISReadAggrement {
    /**
     * 查询是否阅读协议
     * @param request
     * @return
     */
    IServiceResponse isReadAggrement( @Context HttpServletRequest request);

    /**
     * 设置阅读字段
     * @param readAggreRequest
     * @param request
     * @return
     */
    IServiceResponse setReadAggrement(ReadAggreRequest readAggreRequest, @Context HttpServletRequest request);

    /**
     *查询新资金字段状态
     * @param request
     * @return
     */
    IServiceResponse getIsNewFund( @Context HttpServletRequest request);
}
