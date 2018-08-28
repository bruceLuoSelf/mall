package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;

import javax.ws.rs.QueryParam;

/**
 * Created by Administrator on 2017/2/17.
 */
public interface IOrderLogService {

    /**
     * 保存聊天记录
     *
     * @param OrderId
     * @param ChattingRecords
     */
    IServiceResponse saveChattingRecords(@QueryParam("OrderId") String OrderId, @QueryParam("ChattingRecords") String ChattingRecords);

    /**
     * 查询聊天记录
     *
     * @param OrderId
     * @return
     */
    IServiceResponse selectChattingRecords(@QueryParam("OrderId") String OrderId);


}
