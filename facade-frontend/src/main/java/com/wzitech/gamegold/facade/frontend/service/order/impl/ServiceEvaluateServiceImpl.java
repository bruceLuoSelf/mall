package com.wzitech.gamegold.facade.frontend.service.order.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.order.IServiceEvaluateService;
import com.wzitech.gamegold.facade.frontend.service.order.dto.EvaluateRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.EvaluateResponse;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.IServiceEvaluateManager;
import com.wzitech.gamegold.order.entity.ServiceEvaluate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.List;

@Service("ServiceEvaluateService")
@Path("evaluate")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class ServiceEvaluateServiceImpl  extends AbstractBaseService implements IServiceEvaluateService {
    @Autowired
    IServiceEvaluateManager serviceEvaluateManager;

    @Autowired
    IOrderInfoManager orderInfoManager;

    @Path("queryByOrderId")
    @POST
    @Override
    public IServiceResponse queryByOrderId(EvaluateRequest evaluateRequest, @Context HttpServletRequest request){
        logger.debug("根据订单编号查找评价:{}", evaluateRequest);
        EvaluateResponse response=new EvaluateResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            String orderId = evaluateRequest.getOrderId();
            List<ServiceEvaluate> list = serviceEvaluateManager.queryByOrderId(orderId);
            if (list != null && list.size() > 0) {
                for (ServiceEvaluate serviceEvaluate : list) {
                    long serviceId = serviceEvaluate.getServiceId();

                    //查找当前订单号对应的订单信息
                    OrderInfoEO orderInfoEO = orderInfoManager.selectById(orderId);
                    if (orderInfoEO != null) {
                        if (serviceId == orderInfoEO.getServicerId().longValue()) {
                            //担保客服
                            response.setScore2(serviceEvaluate.getScore());
                        } else {
                            //物服
                            response.setScore1(serviceEvaluate.getScore());
                            response.setScore3(serviceEvaluate.getScore());
                        }
                        response.setRemark(serviceEvaluate.getRemark());
                    }
                }
            }
            response.setTotalCount(list.size());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }
        catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("根据订单编号查找评价发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据订单编号查找评价发生异常:{}", ex);
        }
        logger.debug("根据订单编号查找评价响应信息:{}", response);
        return response;
    }

    @Path("addevaluate")
    @POST
    @Override
    public IServiceResponse addEvaluate(EvaluateRequest evaluateRequest, @Context HttpServletRequest request){
        logger.debug("当前新增评价:{}", evaluateRequest);
        EvaluateResponse response=new EvaluateResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            String orderId = evaluateRequest.getOrderId();
            int score1 = evaluateRequest.getScore1();
            int score2 = evaluateRequest.getScore2();
            int score3 = evaluateRequest.getScore3();
            String remark = evaluateRequest.getRemark();

            serviceEvaluateManager.add(orderId, score1, score2, score3, remark);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前新增评价发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前新增评价发生异常:{}", ex);
        }
        logger.debug("当前新增评价响应信息:{}", response);
        return response;
    }

    @Path("modifyevaluate")
    @POST
    @Override
    public IServiceResponse modifyEvaluate(EvaluateRequest evaluateRequest, @Context HttpServletRequest request) {
        logger.debug("当前修改评价:{}", evaluateRequest);
        EvaluateResponse response=new EvaluateResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            String orderId = evaluateRequest.getOrderId();
            int score1 = evaluateRequest.getScore1();
            int score2 = evaluateRequest.getScore2();
            int score3 = evaluateRequest.getScore3();
            String remark = evaluateRequest.getRemark();

            serviceEvaluateManager.update(orderId, score1, score2, score3, remark);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("当前修改评价发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("当前修改评价发生异常:{}", ex);
        }
        logger.debug("当前修改评价响应信息:{}", response);
        return response;
    }
}
