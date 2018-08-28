package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.dto.BaseResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.IShGameTradeService;
import com.wzitech.gamegold.shorder.business.IShGameTradeManager;
import com.wzitech.gamegold.shorder.entity.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/3/16.
 */
@Controller("ShGameTradeService")
@Path("shGameTrade")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class ShGameTradeServiceImpl extends AbstractBaseService implements IShGameTradeService {

    @Autowired
    private IShGameTradeManager shGameTradeManager;

    /**
     * 根据配置游戏id获取收货模式
     * @param gameTableId
     * @return
     */
    @Override
    @Path("getShModeByGame")
    @GET
    public IServiceResponse getShModeByGame(@QueryParam("gameTableId")Long gameTableId) {
        BaseResponse response = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            if (gameTableId == null) {
                throw new SystemException(ResponseCodes.EmptyGameTableId.getCode(),ResponseCodes.EmptyGameTableId.getMessage());
            }
            List<Integer> shModeList = shGameTradeManager.getShModeByGame(gameTableId);
            response.setData(shModeList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("根据配置游戏id获取收货模式发生异常:{}", e);
        }catch (Exception e){
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据配置游戏id获取收货模式发生未知异常:{}", e);
        }
        return response;
    }

    /**
     *
     * @param gameTableId
     * @param shMode
     * @return
     */
    @Override
    @Path("getTradeByGameAndShMode")
    @GET
    public IServiceResponse getTradeByGameAndShMode(@QueryParam("gameTableId")Long gameTableId,@QueryParam("shMode")Integer shMode) {
        BaseResponse response = new BaseResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            if (gameTableId != null) {
                map.put("gameTableId",gameTableId);
            }
            if (shMode != null) {
                map.put("shMode",shMode);
            }
            List<Trade> tradeList = shGameTradeManager.getTradeByGameAndShMode(map);
            response.setData(tradeList);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (Exception e) {
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据配置游戏id和收货模式查询交易方式发生未知异常:{}", e);
        }
        return response;
    }
}
