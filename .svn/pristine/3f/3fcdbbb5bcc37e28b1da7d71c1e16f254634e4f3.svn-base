package com.wzitech.gamegold.facade.frontend.service.shorder;

import com.wzitech.chaos.framework.server.common.IServiceResponse;

import javax.ws.rs.QueryParam;

/**
 * Created by ljn on 2018/3/16.
 */
public interface IShGameTradeService {

    IServiceResponse getShModeByGame(@QueryParam("gameTableId")Long gameTableId);

    IServiceResponse getTradeByGameAndShMode(@QueryParam("gameTableId")Long gameTableId,@QueryParam("shMode")Integer shMode);
}
