package com.wzitech.gamegold.facade.frontend.service.chorder.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.BeanMapper;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.common.enums.OrderCenterOrderStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.common.utils.RequestRealIp;
import com.wzitech.gamegold.facade.frontend.accessLimit.AccessLimit;
import com.wzitech.gamegold.facade.frontend.service.chorder.IDeliveryOrderService;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.CreateDeliveryOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.chorder.dto.DeliveryOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.excel.ExportExcel;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISellerData;
import com.wzitech.gamegold.repository.business.IHuanXinManager;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.business.impl.AsyncPushToMainMethodsImple;
import com.wzitech.gamegold.shorder.dao.IHxAccountRedisDao;
import com.wzitech.gamegold.shorder.dao.IRobotImgDAO;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.shorder.enums.ShowUserImgEnum;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 出货订单服务
 */
@Service("DeliveryOrderService")
@Path("/chorder")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class DeliveryOrderServiceImpl extends AbstractBaseService implements IDeliveryOrderService {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    ISellerData sellerData;
    @Autowired
    private AsyncPushToMainMethodsImple asyncPushToMainMehtods;
    @Autowired
    private IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    private IPurchaseOrderManager purchaseOrderManager;
    @Autowired
    private IDeliveryOrderLogManager deliveryOrderLogManager;
    @Autowired
    private IDeliverySubOrderManager deliverySubOrderManager;
    @Autowired
    private IPurchaserGameTradeManager purchaserGameTradeManager;

    @Autowired
    private IDeliveryOrderAutoConfigManager deliveryOrderAutoConfigManager;

    @Autowired
    private IHuanXinManager huanXinManager;

    @Autowired
    private IHxAccountManager hxAccountManager;

    @Autowired
    private IHxAccountRedisDao hxAccountRedisDao;
    @Autowired
    private IFundManager fundManager;

    @Autowired
    ISystemConfigManager systemConfigManager;
    @Autowired
    private IDeliveryConfigManager deliveryConfigManager;
    @Autowired
    private IRobotImgDAO robotImgDAO;

    @Override
    @GET
    @Path("/getIP")
    public String get(@Context HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 创建出货单
     *
     * @param params
     * @return
     */
    @Override
    @POST
    @Path("/createDeliveryOrder")
    public IServiceResponse createDeliveryOrder(CreateDeliveryOrderRequest params, @Context HttpServletRequest request) {
        Long time0 = System.currentTimeMillis();
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        DeliveryOrder order = new DeliveryOrder();
        try {
//            //判断收货方式，如果是手工收货就返回
//            if(params.getDeliveryType().intValue()== ShDeliveryTypeEnum.Manual.getCode()){
//                responseStatus.setStatus(ResponseCodes.ErrorDeliveryType.getCode(),
//                        ResponseCodes.ErrorDeliveryType.getMessage());
//                return response;
//            }

            logger.info("创建出货单开始：{}", params);
            order = BeanMapper.map(params, DeliveryOrder.class);
            order.setSellerIp(RequestRealIp.getRequestRealIp(request));
            DeliveryOrder deliveryOrder = deliveryOrderManager.createOrder(order);
            //创建订单的时候生成环信id
            //创建订单后拿到订单id,根据出货单查询出货商用户id
//            DeliveryOrder deliveryOrder = deliveryOrderManager.queryDeliveryOrderByOrderId(orderId);

            //创建订单后，将订单推送到主站
            Long time1 = System.currentTimeMillis();
            int orderStatus = OrderCenterOrderStatus.WAIT_SEND.getCode();
            asyncPushToMainMehtods.orderPushToMain(deliveryOrder, orderStatus);
            logger.info("推送订单中心用时：{}", System.currentTimeMillis() - time1);

//            //机器收货不要生成环信id
//            Long time = System.currentTimeMillis();
//            if (deliveryOrder.getDeliveryType() == ShDeliveryTypeEnum.Manual.getCode()) {
//                huanXinManager.registerHx(deliveryOrder);
//            }
//            logger.info("注册环信用时：{}",System.currentTimeMillis()-time);
            response.setOrderId(order.getOrderId());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            logger.info("创建出货单总用时：{}", System.currentTimeMillis() - time0);
        } catch (SystemException ex) {
            //补偿
            fundManager.freezeZBao(order);

            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("创建出货单发生异常:{}", ex);
        } catch (Exception ex) {
            fundManager.freezeZBao(order);

            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("创建出货单发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 获取当前 采购单以及采购商信息
     * 获取提醒信息并返回给前台显示
     *
     * @param id
     * @return
     */
    @GET
    @Path("/selectPurchaseOrderAndCgDataById")
    @Override
    public IServiceResponse selectPurchaseOrderAndCgDataById(@QueryParam("id") Long id) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            PurchaseOrder purchaseOrder = purchaseOrderManager.selectPurchaseOrderAndCgDataById(id);
            if (purchaseOrder != null) {
                purchaserGameTradeManager.setParameter(purchaseOrder);
                //获取交易信息
                PurchaseConfig purchaseConfig = purchaseOrderManager.getPurchaseConfig(purchaseOrder.getGameName(), purchaseOrder.getGoodsType().intValue(), purchaseOrder.getDeliveryType());
                if (purchaseConfig != null) {
                    response.setPurchaseConfig(purchaseConfig);
                }
            }
            response.setPurchaseOrder(purchaseOrder);
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
            logger.error("获取采购单以及采购商信息发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取采购单以及采购商信息发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 分页查找订单
     *
     * @param params
     * @param page
     * @param pageSize
     * @param fieldName
     * @param isAsc
     * @param request
     * @return
     */
    @GET
    @Path("selectDeliverOrderInPage")
    @Override
    public IServiceResponse selectDeliverOrderInPage(@QueryParam("") DeliveryOrderRequest params,
                                                     @QueryParam("page") Integer page,
                                                     @QueryParam("pageSize") Integer pageSize,
                                                     @QueryParam("fieldName") String fieldName,
                                                     @QueryParam("isAsc") Boolean isAsc,
                                                     @QueryParam("isSell") Boolean isSell,
                                                     @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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
            if (!isSell && !sellerData.checkSellerForRecharge(response)) {
                return response;
            }

            if (page == null || page <= 0) {
                page = 1;
            }
            if (pageSize == null || pageSize <= 0) {
                pageSize = 25;
            }
//            else if (pageSize > 100) {
//                //防止大批量的查询
//                pageSize = 100;
//            }
            int start = (page - 1) * pageSize;

            String startTime = params.getStartTime();
            String endTime = params.getEndTime();
            Date startOrderCreate = null;
            Date endOrderCreate = null;
            if (StringUtils.isNotBlank(startTime)) {
                startTime += " 00:00:00";
                startOrderCreate = DATE_FORMAT.parse(startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                endTime += " 23:59:59";
                endOrderCreate = DATE_FORMAT.parse(endTime);
            }
            if (daysBetween(startOrderCreate, endOrderCreate) > 30) {
                responseStatus.setStatus(ResponseCodes.Over30Days.getCode(), ResponseCodes.Over30Days.getMessage());
                return response;
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", params.getGameName());
            paramMap.put("region", params.getRegion());
            paramMap.put("server", params.getServer());
            paramMap.put("gameRace", params.getGameRace());
            paramMap.put("orderId", params.getOrderId());
            paramMap.put("createStartTime", startOrderCreate);
            paramMap.put("createEndTime", endOrderCreate);
            paramMap.put("statusList", params.getStatus());
            if (isSell) {
                paramMap.put("sellerAccount", userInfo.getLoginAccount());
            } else {
                paramMap.put("buyerAccount", userInfo.getLoginAccount());
            }

            GenericPage<DeliveryOrder> genericPage = deliveryOrderManager.queryListInPage(paramMap, start, pageSize, fieldName, isAsc);
            List<DeliveryOrder> data = genericPage.getData();
            Long totalCount = genericPage.getTotalCount();

            response.setDeliveryOrderList(data);
            response.setTotalCount(totalCount);
            response.setPageSize(pageSize);
            response.setCurrPage(page);
            response.setTotalPage(genericPage.getTotalPageCount());
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
            logger.error("查找出货单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查找出货单发生未知异常:{}", ex);
        }
        return response;
    }

    private int daysBetween(Date smdate, Date bdate) {
        try {
            if (smdate == null || bdate == null) {
                return 0;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));
        } catch (Exception ex) {
            logger.error("时间差发生未知异常:{}", ex);
        }
        return 0;
    }

    /**
     * 统计金额和数量
     *
     * @param params
     * @param request
     * @return
     */
    @GET
    @Path("statisAmountAndCount")
    @Override
    public IServiceResponse statisAmountAndCount(@QueryParam("") DeliveryOrderRequest params,
                                                 @QueryParam("isSell") Boolean isSell,
                                                 @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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

            String startTime = params.getStartTime();
            String endTime = params.getEndTime().toString();
            Date startOrderCreate = null;
            Date endOrderCreate = null;
            if (StringUtils.isNotBlank(startTime)) {
                startTime += " 00:00:00";
                startOrderCreate = DATE_FORMAT.parse(startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                endTime += " 23:59:59";
                endOrderCreate = DATE_FORMAT.parse(endTime);
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", params.getGameName());
            paramMap.put("region", params.getRegion());
            paramMap.put("server", params.getServer());
            paramMap.put("gameRace", params.getGameRace());
            paramMap.put("orderId", params.getOrderId());
            paramMap.put("createStartTime", startOrderCreate);
            paramMap.put("createEndTime", endOrderCreate);
            paramMap.put("statusList", "4,5");
            if (isSell) {
                paramMap.put("sellerAccount", userInfo.getLoginAccount());
            } else {
                paramMap.put("buyerAccount", userInfo.getLoginAccount());
            }
            DeliveryOrder deliveryOrder = deliveryOrderManager.statisAmountAndCount(paramMap);

            response.setDeliveryOrder(deliveryOrder);
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
            logger.error("统计金额和数量发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("统计金额和数量发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 导出数据
     *
     * @param params
     * @param request
     * @return
     */
    @GET
    @Path("exportOrder")
    @Override
    public void exportOrder(@QueryParam("") DeliveryOrderRequest params,
                            @QueryParam("isSell") Boolean isSell,
                            @Context HttpServletRequest request,
                            @Context HttpServletResponse servletRespone) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return;
            }

            String startTime = params.getStartTime();
            String endTime = params.getEndTime();
            Date startOrderCreate = null;
            Date endOrderCreate = null;
            if (StringUtils.isNotBlank(startTime)) {
                startTime += " 00:00:00";
                startOrderCreate = DATE_FORMAT.parse(startTime);
            }
            if (StringUtils.isNotBlank(endTime)) {
                endTime += " 23:59:59";
                endOrderCreate = DATE_FORMAT.parse(endTime);
            }

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", params.getGameName());
            paramMap.put("region", params.getRegion());
            paramMap.put("server", params.getServer());
            paramMap.put("gameRace", params.getGameRace());
            paramMap.put("orderId", params.getOrderId());
            paramMap.put("createStartTime", startOrderCreate);
            paramMap.put("createEndTime", endOrderCreate);
            paramMap.put("statusList", params.getStatus());
            paramMap.put("buyerAccount", userInfo.getLoginAccount());

            List<DeliveryOrder> list = deliveryOrderManager.selectAllOrder(paramMap);
//            if (list != null && list.size() > 0) {
            exportToExcel(list, request, servletRespone);
//            }
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            logger.error("查找出货单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            logger.error("查找出货单发生未知异常:{}", ex);
        }
    }

    /**
     * 根据id查找对应的订单
     *
     * @param orderId
     * @param request
     * @return
     */
    @GET
    @Path("selectOrderById")
    @Override
    public IServiceResponse selectOrderById(@QueryParam("orderId") String orderId,
                                            @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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

            DeliveryOrder deliveryOrder = deliveryOrderManager.getByOrderId(orderId);
            if (deliveryOrder == null) {
                responseStatus.setStatus(ResponseCodes.EmptyOrderInfo.getCode(),
                        ResponseCodes.EmptyOrderInfo.getMessage());
                return response;
            }
            //解决交易类目前台出现undefined问题
            if (deliveryOrder.getGoodsTypeName() == null) {
                deliveryOrder.setGoodsTypeName("");
            }
            //if it is a appealOrder
            if (StringUtils.isNotBlank(deliveryOrder.getAppealOrder())) {
                DeliverySubOrder subOrderByAppealOrder = deliverySubOrderManager.findSubOrderById(Long.parseLong(deliveryOrder.getAppealOrder()));
                if (subOrderByAppealOrder != null) {
                    response.setAppealOrderStatus(subOrderByAppealOrder.getAppealOrderStatus());
                    response.setMainOrderId(subOrderByAppealOrder.getOrderId());
                }

                DeliverySubOrder subOrder = deliverySubOrderManager.findSubOrderByOrderId(orderId);
                if (subOrder != null) {
                    response.setEndReason(subOrder.getOtherReason());
                }
            }
            if (deliveryOrder.getSellerAccount().equals(userInfo.getLoginAccount()) || deliveryOrder.getBuyerAccount().equals(userInfo.getLoginAccount())) {
                response.setDeliveryOrder(deliveryOrder);

                //获取交易信息
                DeliveryConfig deliveryConfig = deliveryConfigManager.getDeliveryConfigByCondition(deliveryOrder.getGameName(), deliveryOrder.getGoodsType().intValue(), deliveryOrder.getDeliveryType(), deliveryOrder.getTradeType());
                if (deliveryConfig != null) {
                    response.setDeliveryConfig(deliveryConfig);
                }
                responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
            }
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("根据id查找订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据id查找订单发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 开始交易(我已登录游戏)
     * 季洋心改造
     *
     * @param id
     * @param request
     * @return
     */
    @GET
    @Path("startTrading")
    @Override
    @AccessLimit(time = 2, maxCount = 1, isConfig = false)
    public IServiceResponse startTrading(@QueryParam("id") Long id,
                                         @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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
            //判断收货方式 如果是人工收货就返回
            DeliveryOrder order = deliveryOrderManager.getById(id);
            if (order.getDeliveryType().intValue() == ShDeliveryTypeEnum.Manual.getCode()) {
                responseStatus.setStatus(ResponseCodes.ErrorDeliveryType.getCode(),
                        ResponseCodes.ErrorDeliveryType.getMessage());
                return response;
            }
            //判断是否是当前的出货用户点击登录按钮
            if (order.getSellerAccount() != null && !order.getSellerAccount().equals(userInfo.getLoginAccount())) {
                responseStatus.setStatus(ResponseCodes.NotYourOrder.getCode(),
                        ResponseCodes.NotYourOrder.getMessage());
                return response;
            }
            if (order.getStatus().intValue() != DeliveryOrderStatus.WAIT_TRADE.getCode()) {
                responseStatus.setStatus(ResponseCodes.StateAfterNotIn.getCode(),
                        ResponseCodes.StateAfterNotIn.getMessage());
                return response;
            }
            //进入排队阶段
            deliveryOrderManager.startTradingFormOrder(order);
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
            logger.error("根据id查找订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据id查找订单发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 撤单 主订单撤单
     *
     * @param id
     * @param request
     * @return
     */
    @GET
    @Path("cancelOrder")
    @Override
    public IServiceResponse cancelOrder(@QueryParam("id") Long id, @QueryParam("reason") int reason, @QueryParam("remark") String remark, @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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
            //判断订单收货方式，如果是人工收货 就返回
//            DeliveryOrder order=deliveryOrderManager.getById(id);
//            if(order.getDeliveryType().intValue()==ShDeliveryTypeEnum.Manual.getCode()){
//                responseStatus.setStatus(ResponseCodes.ErrorDeliveryType.getCode(),
//                        ResponseCodes.ErrorDeliveryType.getMessage());
//                return  response;
//            }
            deliveryOrderManager.cancelOrder(id, reason, remark);
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
            logger.error("根据id查找订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据id查找订单发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 撤单 主订单撤单
     *
     * @param id
     * @param request
     * @return
     */
    @GET
    @Path("cancelSubOrder")
    @Override
    public IServiceResponse cancelSubOrder(@QueryParam("subOrderId") Long id, @QueryParam("reason") int reason, @QueryParam("remark") String remark, @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        DeliverySubOrder deliverySubOrder = deliverySubOrderManager.queryUniqueSubOrder(id);
        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            if (!(deliverySubOrder.getSellerUid().equals(userInfo.getUid()) || deliverySubOrder.getBuyerUid().equals(userInfo.getUid()))) {
                responseStatus.setStatus(ResponseCodes.NoPermit.getCode(),
                        ResponseCodes.NoPermit.getMessage());
                return response;
            }
            Map<Long, Long> countMap = new HashMap<Long, Long>();
            countMap.put(id, 0L);
            Map<Long, Integer> reasonMap = new HashMap<Long, Integer>();
            reasonMap.put(id, reason);
            Map<Long, String> remarkMap = new HashMap<Long, String>();
            remarkMap.put(id, remark);
            deliveryOrderManager.handleOrderForMailDeliveryOrderMax(countMap, deliverySubOrder.getOrderId(), null, reasonMap, remarkMap);
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
            logger.error("根据id查找订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据id查找订单发生未知异常:{}", ex);
        }
        return response;
    }


    /**
     * 申请部分完成
     *
     * @param id
     * @param request
     * @return
     */
    @GET
    @Path("applyForCompletePart")
    @Override
    public IServiceResponse applyForCompletePart(@QueryParam("id") Long id, @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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
            // 判断收货方式如果是人工收货就返回
            DeliveryOrder order = deliveryOrderManager.getById(id);
            if (order.getDeliveryType().intValue() == ShDeliveryTypeEnum.Manual.getCode()) {
                responseStatus.setStatus(ResponseCodes.ErrorDeliveryType.getCode(),
                        ResponseCodes.ErrorDeliveryType.getMessage());
                return response;
            }
            deliveryOrderManager.applyForCompletePart(id);
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
            logger.error("根据id查找订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据id查找订单发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 查找该订单的日志
     *
     * @param id
     * @param request
     * @return
     */
    @GET
    @Path("getLogByChId")
    @Override
    public IServiceResponse getLogByChId(@QueryParam("id") String id,
                                         @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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
            response.setUserInfo(userInfo);
            List<OrderLog> orderLogList = deliveryOrderLogManager.getByOrderId(id, OrderLog.TYPE_NORMAL);
            response.setOrderLogList(orderLogList);
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
            logger.error("根据id查找订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据id查找订单发生未知异常:{}", ex);
        }
        return response;
    }

    private void exportToExcel(List<DeliveryOrder> list,
                               HttpServletRequest request,
                               HttpServletResponse servletRespone) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet();

        ExportExcel exportExcel = new ExportExcel(wb, sheet);

        // 创建单元格样式
        HSSFCellStyle cellStyle = wb.createCellStyle();

        // 指定单元格居中对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);

        // 创建报表头部
        String headString = "交易流水列表";
        int columnSize = 11;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columHeader = new String[]{"订单号", "收货方", "发货方", "游戏区服",
                "商品数量", "订单单价", "订单总额", "实际交易数量", "实际成交金额", "订单时间", "状态"};
        exportExcel.createColumHeader(1, columHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

        // 循环创建中间的单元格的各项的值
        if (list != null) {
            int i = 2;
            for (DeliveryOrder deliveryOrder : list) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.cteateCell(wb, row, (short) 0, cellstyle, deliveryOrder.getOrderId());
                exportExcel.cteateCell(wb, row, (short) 1, cellstyle, deliveryOrder.getBuyerAccount());
                exportExcel.cteateCell(wb, row, (short) 2, cellstyle, deliveryOrder.getSellerAccount());
                exportExcel.cteateCell(wb, row, (short) 3, cellstyle, deliveryOrder.getGameName() + "/" + deliveryOrder.getRegion() + "/" + deliveryOrder.getServer());
                exportExcel.cteateCell(wb, row, (short) 4, cellstyle, deliveryOrder.getCount().toString());
                exportExcel.cteateCell(wb, row, (short) 5, cellstyle, deliveryOrder.getPrice().toString());
                exportExcel.cteateCell(wb, row, (short) 6, cellstyle, deliveryOrder.getAmount().toString());
                exportExcel.cteateCell(wb, row, (short) 7, cellstyle, deliveryOrder.getRealCount().toString());
                exportExcel.cteateCell(wb, row, (short) 8, cellstyle, deliveryOrder.getRealAmount().toString());
                String endTime = "";
                if (deliveryOrder.getCreateTime() != null) {
                    endTime = format.format(deliveryOrder.getCreateTime());
                }
                exportExcel.cteateCell(wb, row, (short) 9, cellstyle, endTime);
                exportExcel.cteateCell(wb, row, (short) 10, cellstyle, DeliveryOrderStatus.getNameByCode(deliveryOrder
                        .getStatus()));
            }
        }
        //String exportPath = WebServerContants.FILES_EXPORT_PATH;
        String path = "/srv/export";
        String fileName = UUID.randomUUID().toString() + ".xls";
        File file = new File(path);
        file.mkdirs();
        String filePath = path + "/" + fileName;
        exportExcel.outputExcel(filePath);
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            servletRespone.setContentType("text/plain");
            servletRespone.setHeader("Location", fileName);
            servletRespone.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            inputStream = new FileInputStream(filePath);
            outputStream = servletRespone.getOutputStream();
            byte[] buffer = new byte[1024 * 2];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据主订单id查找子订单列表
     *
     * @param chId
     * @param request
     * @return
     */
    @GET
    @Path("getDeliverySubOrderList")
    @Override
    public IServiceResponse getDeliverySubOrderList(@QueryParam("chId") Long chId, @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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

            List<DeliverySubOrder> deliverySubOrderList = deliverySubOrderManager.querySubOrdersByOrderId(chId);
            if (CollectionUtils.isNotEmpty(deliverySubOrderList)) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (DeliverySubOrder deliverySubOrder : deliverySubOrderList) {
                    map.clear();
                    map.put("subOrderId", deliverySubOrder.getId());
                    map.put("orderId", deliverySubOrder.getOrderId());
                    map.put("type", ShowUserImgEnum.SHOW_IMG.getCode());
                    List<RobotImgEO> robotImgEOs = robotImgDAO.selectByMap(map);
                    deliverySubOrder.setRobotImgList(robotImgEOs);
                }
            }
            response.setDeliverySubOrderList(deliverySubOrderList);
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
            logger.error("根据订单号查找子订单列表发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据订单号查找子订单列表发生未知异常:{}", ex);
        }
        return response;
    }

    @GET
    @Path("selectBuyerOrderById")
    @Override
    public IServiceResponse selectBuyerOrderById(@QueryParam("orderId") String orderId, @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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
            DeliveryOrder deliveryOrder = deliveryOrderManager.getByOrderId(orderId);
            //解决交易类目前台出现undifind问题
            if (deliveryOrder.getGoodsType() == null) {
                deliveryOrder.setGoodsTypeName("");
            }
            if (deliveryOrder.getBuyerAccount().equals(userInfo.getLoginAccount())) {
                response.setDeliveryOrder(deliveryOrder);
                responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.AccessDeliveryOrderPermissionDenied.getCode(), ResponseCodes.AccessDeliveryOrderPermissionDenied.getMessage());
            }
        } catch (SystemException ex) {
            // 捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.error("根据id查找订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据id查找订单发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 添加申诉单 by jyx
     *
     * @param params
     * @return
     */
    @Override
    @Path("/createAppealOrder")
    @POST
    public IServiceResponse createAppealOrder(CreateDeliveryOrderRequest params, @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            DeliveryOrder order = BeanMapper.map(params, DeliveryOrder.class);
            order.setSellerIp(RequestRealIp.getRequestRealIp(request));
            //创建订单时增加订单逻辑判断 创建订单为排队状态 自动化介入 分配物服
            DeliveryOrder appealOrder = deliveryOrderManager.createAppealOrder(order);
            response.setDeliveryOrder(appealOrder);
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
            logger.error("根据id查找订单发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("根据id查找订单发生未知异常:{}", ex);
        }
        return response;
    }


    /**
     * 确认发货数量 机器收货使用
     * 我已发货
     * by jyx
     * json key 子订单号
     *
     * @param json
     */
    @Path("confirmShipment")
    @GET
    @Override
    public IServiceResponse confirmShipment(@QueryParam("json") String json) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        logger.info(" 确认订单发货数量:{}", json);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> map = null;
        try {
            map = mapper.readValue(json, Map.class);
            if (map.size() == 0) {
                throw new SystemException(ResponseCodes.NullData.getCode(), ResponseCodes.NullData.getMessage());
            }
            if (json != null) {
                deliveryOrderManager.confirmShipment(map);
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (IOException e) {
            logger.info(" 确认订单发货数量报错json序列化异常:{}", e);
            responseStatus.setStatus(ResponseCodes.NullData.getCode(), ResponseCodes.NullData.getMessage());
        } catch (SystemException ex) {
            //捕获系统异常
            String msg = null;
            if (ArrayUtils.isEmpty(ex.getArgs())) {
                msg = ResponseCodes.getResponseByCode(ex.getErrorCode()).getMessage();
            } else {
                msg = ex.getArgs()[0];
            }
            responseStatus.setStatus(ex.getErrorCode(), msg);
            logger.info(" 确认订单发货数量报错:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.info("确认订单发货数量报错发生未知异常:{}", ex);
        }
        logger.info("确认订单发货数量完成:{}");
        return response;
    }

    @GET
    @Override
    @Path("turnToArtifical")
    public IServiceResponse autoDistributionService(@QueryParam("id") Long orderId, @Context HttpServletRequest request) {
        DeliveryOrderResponse response = new DeliveryOrderResponse();
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
            if (orderId == null) {
                throw new SystemException("订单号不能为空");
            }
            Integer exceptionFromAuto = 1;
            deliveryOrderManager.subOrderAutoDistributionManager(orderId, exceptionFromAuto, null);
            logger.info("订单：{}已移交给寄售物服", orderId);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (Exception e) {
            logger.info("订单：{}转人工处理出现异常", orderId);
            e.printStackTrace();
            throw new SystemException("转人工出现未知异常");
        }
        return response;
    }

}

