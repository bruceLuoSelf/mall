package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReceiveMainRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

/**
 * Created by chengXY on 2017/8/22.
 */
public interface IReceiveMainService {
    /**
     * 从主站回调回来，说明此出货单的卖家开通了7bao新资金
     * 接收主站回调：对此次出货单的卖家余额加钱操作
     * */
    public HttpServletResponse plusAmount(@FormParam("") ReceiveMainRequest receiveMainRequest, @Context HttpServletRequest request,
                                          @Context HttpServletResponse response);
}
