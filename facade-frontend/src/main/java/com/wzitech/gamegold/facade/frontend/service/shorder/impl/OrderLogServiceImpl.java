package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.shorder.IOrderLogService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.OrderLogResponse;
import com.wzitech.gamegold.shorder.business.IOrderUserLogManager;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Administrator on 2017/2/17.
 */
@Service("OrderLogService")
@Path("orderLog")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class OrderLogServiceImpl extends AbstractBaseService implements IOrderLogService {
    @Autowired
    IOrderUserLogManager orderLogManager;


    @Path("saveChattingRecords")
    @Override
    @GET
    public IServiceResponse saveChattingRecords(@QueryParam("orderId") String orderId, @QueryParam("chattingRecords") String chattingRecords) {
        OrderLogResponse response = new OrderLogResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            orderLogManager.saveChattingRecords(orderId,chattingRecords);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("保存聊天记录发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("保存聊天记录发生未知异常:{}", e);
        }
        return response;
    }

    @Path("selectChattingRecords")
    @Override
    @GET
    public IServiceResponse selectChattingRecords(@QueryParam("orderId") String orderId) {
        OrderLogResponse response = new OrderLogResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            List<OrderLog> list=orderLogManager.selectChattingRecords(orderId);
            response.setOrderLogs(list);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("保存聊天记录发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("保存聊天记录发生未知异常:{}", e);
        }
        return response;
    }

}
