package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISReadAggrement;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReadAggreRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReadAggreResponse;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/08/21 wangmin				ZW_C_JB_00021 商城资金改造==>是否阅读协议接口
 */
@Service("ReadAggrementService")
@Path("readAggrement")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class ReadAggrementService extends AbstractBaseService implements ISReadAggrement {
    @Autowired
    ISellerManager sellerManager;

    /**
     * 查询协议阅读状态
     *
     * @param request
     * @return
     */
    @Path("isReadAggrement")
    @GET
    @Override
    public IServiceResponse isReadAggrement(@Context HttpServletRequest request) {
        ReadAggreResponse readAggreResponse = new ReadAggreResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        readAggreResponse.setResponseStatus(responseStatus);
        try {
            // 获取当前用户
            String loginAccount = CurrentUserContext.getUser().getLoginAccount();
            if (StringUtils.isBlank(loginAccount)) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return readAggreResponse;
            }
            SellerInfo sellerInfo = sellerManager.querySellerInfo(loginAccount);
            if (sellerInfo == null) {
                responseStatus.setStatus(ResponseCodes.EmptySellerInfo.getCode(),
                        ResponseCodes.EmptySellerInfo.getMessage());
                return readAggreResponse;
            }
            if (sellerInfo.getisAgree() != null && sellerInfo.getisAgree()) {
                readAggreResponse.setIsAgree(sellerInfo.getisAgree());
            } else {
                readAggreResponse.setIsAgree(Boolean.FALSE);
            }

        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取协议书发生异常", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取协议书发生未知异常:{}", e);
        }
        return readAggreResponse;
    }

    /**
     * 设置协议阅读状态
     *
     * @param readAggreRequest
     * @param request
     * @return
     */
    @Path("setReadAggrement")
    @POST
    @Override
    public IServiceResponse setReadAggrement(ReadAggreRequest readAggreRequest, @Context HttpServletRequest request) {
        ReadAggreResponse readAggreResponse = new ReadAggreResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        readAggreResponse.setResponseStatus(responseStatus);
        try {
            // 获取当前用户
            String loginAccount = CurrentUserContext.getUser().getLoginAccount();
            if (StringUtils.isBlank(loginAccount)) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return readAggreResponse;
            }
            SellerInfo sellerInfo = sellerManager.querySellerInfo(loginAccount);
            if (sellerInfo == null) {
                responseStatus.setStatus(ResponseCodes.EmptySellerInfo.getCode(),
                        ResponseCodes.EmptySellerInfo.getMessage());
                return readAggreResponse;
            }
            //更新表中协议状态
            if (readAggreRequest.getIsAgree()) {
                if (sellerInfo.getisAgree() == null || sellerInfo.getisAgree() == false) {
                    sellerInfo.setisAgree(readAggreRequest.getIsAgree());
                    sellerManager.modifySeller(sellerInfo);
                }
            }
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取协议书发生异常", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取协议书发生未知异常:{}", e);
        }
        return readAggreResponse;
    }

    /**
     * 查询新资金状态
     *
     * @param request
     * @return
     */
    @Path("getIsNewFund")
    @GET
    @Override
    public IServiceResponse getIsNewFund(@Context HttpServletRequest request) {
        ReadAggreResponse readAggreResponse = new ReadAggreResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        readAggreResponse.setResponseStatus(responseStatus);
        try {
            // 获取当前用户
            String loginAccount = CurrentUserContext.getUser().getLoginAccount();
            if (StringUtils.isBlank(loginAccount)) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return readAggreResponse;
            }
            SellerInfo sellerInfo = sellerManager.querySellerInfo(loginAccount);
            if (sellerInfo == null) {
                responseStatus.setStatus(ResponseCodes.EmptySellerInfo.getCode(),
                        ResponseCodes.EmptySellerInfo.getMessage());
                return readAggreResponse;
            }

            if (sellerInfo.getIsNewFund() != null && sellerInfo.getIsNewFund()) {
                readAggreResponse.setIsNewFund(sellerInfo.getIsNewFund());
            } else {
                readAggreResponse.setIsNewFund(Boolean.FALSE);
            }
            if (sellerInfo.getisAgree() == null || sellerInfo.getisAgree() == false) {
                readAggreResponse.setIsAgree(false);
            }else {
                readAggreResponse.setIsAgree(true);
            }
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取新资金字段发生异常", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取新资金字段发生未知异常:{}", e);
        }
        return readAggreResponse;
    }
}