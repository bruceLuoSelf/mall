package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.shorder.IRefundOrderService;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISellerData;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.RefundOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.RefundOrderResponse;
import com.wzitech.gamegold.shorder.business.IRefundOrderManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * 退款
 */
@Service("RefundOrderService")
@Path("refundOrder")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class RefundOrderServiceImpl extends AbstractBaseService implements IRefundOrderService {
    @Autowired
    IRefundOrderManager refundOrderManager;

    @Autowired
    ISellerData sellerData;

    /**
     * 申请退款
     * @param refundOrderRequest
     * @param request
     * @return
     */
    @Path("addRefundOrder")
    @GET
    @Override
    public IServiceResponse queryPayOrderList(@QueryParam("")RefundOrderRequest refundOrderRequest, @Context
    HttpServletRequest  request) {
        RefundOrderResponse response=new RefundOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }

            // 检查卖家信息
            if (!sellerData.checkSellerForRecharge(response)) {
                return response;
            }

            String payOrderId=refundOrderRequest.getPayOrderId();
            String reason=refundOrderRequest.getReason();
            // 判断退款原因不为空
            if(StringUtils.isBlank(reason)){
                responseStatus.setStatus(ResponseCodes.NullOfReason.getCode(),
                        ResponseCodes.NullOfReason.getMessage());
                return response;
            }

            // 判断退款原因不得超过100个汉字
            if(reason.length()>=100){
                responseStatus.setStatus(ResponseCodes.OutOfReasonLength.getCode(),
                        ResponseCodes.OutOfReasonLength.getMessage());
                return response;
            }

            refundOrderManager.applyRefund(payOrderId,reason,userInfo.getLoginAccount());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        }
        catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("申请退款发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("申请退款发生未知异常:{}", e);
        }

        return response;
    }
}
