package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.accessLimit.AccessLimit;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.IManualDeliveryOrderService;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISellerData;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.UploadPicsRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.UploadPicsResponse;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.shorder.business.IManualDeliveryOrderManager;
import com.wzitech.gamegold.shorder.business.IPurchaseOrderManager;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.entity.ImageUploadElements;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.enums.PicSourceEnum;
import com.wzitech.gamegold.shorder.enums.ShowUserImgEnum;
import com.wzitech.gamegold.shorder.utils.SevenBaoFund;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 手动收货订单
 */
@Service("ManualDeliveryOrderService")
@Path("/manualShOrder")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class ManualDeliveryOrderServiceImpl extends AbstractBaseService implements IManualDeliveryOrderService {
    @Autowired
    IPurchaseOrderManager purchaseOrderManager;
    @Autowired
    private IManualDeliveryOrderManager manualDeliveryOrderManager;
    @Autowired
    private IPurchaserDataManager purchaserDataManager;
    @Autowired
    ISellerManager sellerManager;
    @Autowired
    SevenBaoFund sevenBaoFund;
    @Autowired
    ISellerData sellerData;


    /**
     * 开始交易(我已登录游戏)，for手动模式
     *
     * @param id
     * @return
     */
    @Override
    @GET
    @Path("/startTradingForManual")
    public IServiceResponse startTradingForManual(@QueryParam("id") Long id) {
        logger.info("我已登录for手动模式开始，参数：id:{}", id);
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            manualDeliveryOrderManager.startTradingForManual(id);
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
            logger.error("我已登录for手动模式发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("我已登录for手动模式发生未知异常:{}", ex);
        }
        logger.info("我已登录for手动模式结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 手动收货，收货商分配收货角色
     *
     * @param request
     * @return
     */
    @Override
    @POST
    @Path("/assignGameAccount")
    public IServiceResponse assignGameAccount(DeliveryOrderRequest request) {
        logger.info("手动收货，收货商分配收货角色开始，参数：orderId:{},roleName:{},count:{}", new Object[]{request.getOrderId(), request.getRoleName(), request.getCount()});
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            manualDeliveryOrderManager.assignGameAccount(request.getOrderId(), request.getRoleName(), request.getCount());
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
            logger.error("手动收货，收货商分配收货角色发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("手动收货，收货商分配收货角色发生未知异常:{}", ex);
        }
        logger.info("手动收货，收货商分配收货角色结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 手动收货，子订单收货商确认收货
     *
     * @param subOrderId
     * @return
     */
    @Override
    @GET
    @Path("/confirmReceived")
    public IServiceResponse confirmReceived(@QueryParam("subOrderId") Long subOrderId) {
        logger.info("手动收货，子订单收货商确认收货开始，参数：subOrderId:{}", subOrderId);
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            manualDeliveryOrderManager.confirmReceived(subOrderId);
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
            logger.error("手动收货，子订单收货商确认收货发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("手动收货，子订单收货商确认收货发生未知异常:{}", ex);
        }
        logger.info("手动收货，子订单收货商确认收货结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 手动收货，已发货状态，收货商取消子订单
     *
     * @param subOrderId
     * @return
     */
    @Override
    @GET
    @Path("/cancelSubOrder")
    public IServiceResponse cancelSubOrder(@QueryParam("subOrderId") Long subOrderId) {
        logger.info("手动收货，已发货状态，收货商取消子订单开始，参数：subOrderId:{}", subOrderId);
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            manualDeliveryOrderManager.cancelSubOrder(subOrderId);
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
            logger.error("手动收货，已发货状态，收货商取消子订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("手动收货，已发货状态，收货商取消子订单发生未知异常:{}", ex);
        }
        logger.info("手动收货，已发货状态，收货商取消子订单结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 出货商取消子订单
     *
     * @param subOrderId
     * @return
     */
    @Override
    @GET
    @Path("/cancelSubOrderBySeller")
    public IServiceResponse cancelSubOrderBySeller(@QueryParam("subOrderId") Long subOrderId) {
        logger.info("出货商取消子订单开始，参数：subOrderId:{}", subOrderId);
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            manualDeliveryOrderManager.cancelSubOrderBySeller(subOrderId);
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
            logger.error("出货商取消子订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("出货商取消子订单发生未知异常:{}", ex);
        }
        logger.info("出货商取消子订单结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 手动收货，待发货状态，出货商确认发货
     *
     * @param subOrderId
     * @return
     */
    @Override
    @GET
    @Path("/confirmDelivery")
    public IServiceResponse confirmDelivery(@QueryParam("subOrderId") Long subOrderId, @QueryParam("realCount") Long realCount) {
        logger.info("手动收货，待发货状态，出货商确认发货开始，参数：subOrderId:{},realCount:{}", subOrderId, realCount);
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            manualDeliveryOrderManager.confirmDelivery(subOrderId, realCount);
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
            logger.error("手动收货，待发货状态，出货商确认发货发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("手动收货，待发货状态，出货商确认发货发生未知异常:{}", ex);
        }
        logger.info("手动收货，待发货状态，出货商确认发货结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 根据账号查询信息
     *
     * @return
     */
    @Override
    @GET
    @Path("/selectOrderByAccount")
    public IServiceResponse selectOrderByAccount(@QueryParam("account") String account) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            PurchaserData purchaserData = purchaserDataManager.selectByAccount(account);
            response.setPurchaserData(purchaserData);
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("根据账号查询信息发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据账号查询信息发生未知异常:{}", ex);
        }
        logger.info("根据账号查询信息结束，返回数据：{}", ToStringBuilder.reflectionToString(response));
        return response;
    }

    /**
     * 修改当前采购单的采购单价和采购量和最低采购数量
     *
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    @Path("updatePurchaseOrderPriceAndCountAndNum")
    @POST
    @Override
    @AccessLimit
    public IServiceResponse updatePurchaseOrderPriceAndCountAndNum(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {

            //根据采购单的id更新采购单中的采购价格和采购数量
            Long id = purchaseOrderRequest.getId();
            BigDecimal price = purchaseOrderRequest.getPrice();
            Long count = purchaseOrderRequest.getCount();
            Long minCount = purchaseOrderRequest.getMinCount();
            boolean f = purchaseOrderManager.updatePurchaseOrderPriceAndCountAndNum(id, price, count, minCount);

            if (f) {
                responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.SaveFaile.getCode(), ResponseCodes.SaveFaile.getMessage());
            }

        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("修改当前采购单的采购单价和采购量和最低采购数量发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("修改当前采购单的采购单价和采购量和最低采购数量发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * 上下架
     */
    @Path("setOnline")
    @POST
    @Override
    public IServiceResponse setOnline(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {

        PurchaseOrderResponse response = new PurchaseOrderResponse();
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
            //检查收货商上架时资金
            if (purchaseOrderRequest.getIsOnline()) {
                if (!sellerData.checkOnlineAmount(response)) {
                    return response;
                }
            }

            String result = purchaseOrderManager.setPurchaseOrderOnline(purchaseOrderRequest.getIds(), purchaseOrderRequest
                    .getIsOnline(), userInfo.getLoginAccount());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), result);
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("采购单上下架发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("采购单上下架发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * 获取IP
     */
    @Path("getMyIp")
    @GET
    @Override
    public IServiceResponse getMyIp(@Context HttpServletRequest request) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            response.setIp(ip);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取IP发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取IP发生未知异常:{}", e);
        }

        return response;
    }


    /**
     * 前台分页获取采购单列表
     */
    @GET
    @Path("/queryOrder")
    public PurchaseOrderResponse queryOrder(@QueryParam("") PurchaseOrder purchaseOrder,
                                            @QueryParam("page") Integer page,
                                            @QueryParam("pageSize") Integer pageSize,
                                            @Context HttpServletRequest request) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", purchaseOrder.getGameName());
            paramMap.put("region", purchaseOrder.getRegion());
            paramMap.put("server", purchaseOrder.getServer());
            paramMap.put("gameRace", purchaseOrder.getGameRace());
            paramMap.put("count", 0);
            //purchaseOrderManager.selectList(paramMap,);
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("前台分页获取采购单列表发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("前台分页获取采购单列表发生未知异常:{}", e);
        }
        return response;
    }

    @Override
    @Path("uploadImage")
    @Consumes("multipart/form-data")
    @Produces("text/html;charset=UTF-8")
    @POST
    @AccessLimit(time = 1, maxCount = 1, isConfig = false)
    public IServiceResponse uploadImage(@Multipart(value = "file", required = false) byte[] file,
                                        @Multipart(value = "subOrderId", required = false) Long subOrderId,
                                        @Multipart(value = "orderId", required = false) String orderId,
                                        @Context HttpServletRequest request) {
        UploadPicsResponse response = new UploadPicsResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            UploadPicsRequest requestPic = new UploadPicsRequest();
            requestPic.setFileBytes(file);
            requestPic.setRequestSource("yxbmall.5173.com");
            requestPic.setUType("0");
            requestPic.setAppNo(null);
            requestPic.setToken(null);
            requestPic.setVersionNumber(0);
            requestPic.setId(null);
            requestPic.setServiceType(2);
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            if (file == null) {
                throw new SystemException(ResponseCodes.EmptyUploadImage.getCode(), ResponseCodes.EmptyUploadImage.getMessage());
            }
            if (StringUtils.isBlank(orderId)) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
            }
            ImageUploadElements imageUploadElements = manualDeliveryOrderManager.saveImageUrlForSubOrder(subOrderId, requestPic.toString(), orderId, PicSourceEnum.CUSTOMER.getCode(), ShowUserImgEnum.SHOW_IMG.getCode());
            if (imageUploadElements.isSucceed()) {
                response.setMessage("上传成功");
                response.setStatus("true");
                response.setUrl(imageUploadElements.getOriginalUrl());
            }
            if (!imageUploadElements.isSucceed()) {
                response.setMessage(imageUploadElements.getErrorMessage());
                response.setStatus("false");
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getMessage());
            logger.error("上传拍卖交易图片发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("上传拍卖交易图片发生异常:{}", e);
        }
        return response;
    }
}
