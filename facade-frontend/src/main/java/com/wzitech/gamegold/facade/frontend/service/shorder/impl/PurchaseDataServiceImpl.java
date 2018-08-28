package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.IPurchaseDataService;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReadAggreRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.ReadAggreResponse;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import com.wzitech.gamegold.shorder.utils.SevenBaoFund;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.math.BigDecimal;

/**
 * 手动收货订单
 */
@Service("PurchaseDataService")
@Path("/purchaseData")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class PurchaseDataServiceImpl extends AbstractBaseService implements IPurchaseDataService {
    @Autowired
    private IPurchaserDataManager purchaserDataManager;

    @Autowired
    SevenBaoFund sevenBaoFund;

    @Autowired
    ISellerManager sellerManager;

    @Autowired
    ISellerDBDAO sellerDao;

    /**
     * 获取当前收货商数据
     *
     * @return
     */
    @Override
    @GET
    @Path("/getCurrentPurchaserData")
    public IServiceResponse getCurrentPurchaserData() {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            PurchaserData purchaserData = purchaserDataManager.getCurrentPurchaserData();
            response.setPurchaserData(purchaserData);
            //调用7bao配置保证金，可以金额接口
            SystemConfig systemConfig = sevenBaoFund.createFund();
            if (systemConfig==null){
                logger.info("可用收货金配置不能为空:{}",systemConfig);
                throw new SystemException(ResponseCodes.Configuration.getCode(), ResponseCodes.Configuration.getMessage());
            }
            String AvailableFundValue = systemConfig.getAvailableFundValue();
            String configValue = systemConfig.getConfigValue();
            Long id = purchaserData.getId();
            SellerInfo sellerInfo = sellerDao.selectById(id);
            if (sellerInfo != null && sellerInfo.getisAgree() != null && sellerInfo.getisAgree() == true && sellerInfo.getIsNewFund() != null && sellerInfo.getIsNewFund() == true) {
                if (purchaserData.getAvailableAmountZBao().compareTo(new BigDecimal(configValue)) == -1) {
                    throw new SystemException(ResponseCodes.AvailableFundKey.getCode(), ResponseCodes.AvailableFundKey.getMessage());
                }
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("修改收货方式发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("修改收货方式发生未知异常:{}", ex);
        }
        logger.info("修改收货方式结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 修改收货方式
     *
     * @return
     */
    @Override
    @GET
    @Path("/updateDeliveryType")
    public IServiceResponse updateDeliveryType(@QueryParam("deliveryType") int deliveryType) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            purchaserDataManager.updateDeliveryType(deliveryType);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("修改手动收货状态发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("修改手动收货状态发生未知异常:{}", ex);
        }
        logger.info("修改手动收货状态发货结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 修改收货方式
     *
     * @return
     */
    @Override
    @GET
    @Path("/updateTradeType")
    public IServiceResponse updateTradeType(@QueryParam("tradeType") String tradeType, @QueryParam("tradeTypeName") String tradeTypeName) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            purchaserDataManager.updateTradeType(tradeType, tradeTypeName);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("修改收货方式发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("修改收货方式发生未知异常:{}", ex);
        }
        logger.info("修改收货方式结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }


    /**
     * 设置协议阅读状态
     *
     * @param readAggreRequest
     * @param request
     * @return
     */

    @Override
    @Path("setReadAggrement")
    @POST
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

            if (!readAggreRequest.getIsAgree()) {
                sellerInfo.setisAgree(Boolean.FALSE);
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("设置协议状态发生异常", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("设置协议状态发生未知异常:{}", e);
        }
        return readAggreResponse;
    }

    /**
     * 收货商是否阅读协议/新旧资金开关接口
     *
     * @param
     * @return
     */
    @Path("getFundInfo")
    @GET
    @Override
    public IServiceResponse getFundInfo() {
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
            } else {
                readAggreResponse.setIsAgree(true);
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
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
