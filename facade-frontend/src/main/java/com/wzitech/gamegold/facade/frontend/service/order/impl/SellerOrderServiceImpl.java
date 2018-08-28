package com.wzitech.gamegold.facade.frontend.service.order.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.order.ISellerOrderService;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QuerySellerOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QuerySellerOrderResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QueryTransferPermResponse;
import com.wzitech.gamegold.facade.frontend.service.order.dto.TransferOrderResponse;
import com.wzitech.gamegold.order.business.IGameConfigManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.ISubOrderDetailManager;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.dto.SubOrderDetailDTO;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 卖家订单管理
 *
 * @author yemq
 */
@Service("SellerOrderService")
@Path("/seller/order")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class SellerOrderServiceImpl extends AbstractBaseService implements ISellerOrderService {
    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    IOrderInfoManager orderInfoManager;

    @Autowired
    IConfigResultInfoDBDAO configResultInfoDBDAO;

    @Autowired
    ISellerManager sellerManager;

    @Autowired
    ISubOrderDetailManager subOrderDetailManager;

    /**
     * 分页查询卖家订单数据
     *
     * @param params
     * @return
     */
    @GET
    @Path("/list")
    @Override
    public IServiceResponse queryOrders(@QueryParam("") QuerySellerOrderRequest params) {

        QuerySellerOrderResponse response = new QuerySellerOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }

            Date startOrderCreate = null;
            Date endOrderCreate = null;
            String startOrder = params.getStartOrderCreate();
            String endOrder = params.getEndOrderCreate();
            if (StringUtils.isNotBlank(startOrder)) {
                startOrder += " 00:00:00";
                startOrderCreate = sdf.parse(startOrder);
            }
            if (StringUtils.isNotBlank(endOrder)) {
                endOrder += " 23:59:59";
                endOrderCreate = sdf.parse(endOrder);
            }

            if (params.getPageSize() == null) {
                params.setPageSize(20);
            }

            if (params.getStart() == null) {
                params.setStart(0);
            }

            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("sellerAccount", userInfo.getLoginAccount());
            queryMap.put("sellerUid", userInfo.getUid());
            queryMap.put("orderId", params.getOrderId());
            queryMap.put("gameName", params.getGameName());
            queryMap.put("gameRegion", params.getRegion());
            queryMap.put("gameServer", params.getServer());
            queryMap.put("gameRace", params.getGameRace());
            queryMap.put("startCreateTime", startOrderCreate);
            queryMap.put("endCreateTime", endOrderCreate);
            queryMap.put("isDeleted", false);

            if (StringUtils.isNotBlank(params.getSubOrderId())) {
                queryMap.put("subOrderId", Long.parseLong(params.getSubOrderId()));
            }

            if (StringUtils.isNotBlank(params.getOrderState())) {
                queryMap.put("state", Integer.parseInt(params.getOrderState()));
            }
            if (StringUtils.isNotBlank(params.getIsConsignment())) {
                queryMap.put("isConsignment", Boolean.parseBoolean(params.getIsConsignment()));
            }

            GenericPage<SubOrderDetailDTO> page = subOrderDetailManager.querySellerOrders(queryMap, "CREATE_TIME",
                    true, params.getPageSize(), params.getStart());


            // 没有小助手权限的卖家，不显示买家信息
            SellerInfo sellerInfo = sellerManager.findByAccountAndUid(userInfo.getLoginAccount(), userInfo.getUid());
            boolean result = false;
            if (sellerInfo != null && (sellerInfo.getIsHelper() == null || !sellerInfo.getIsHelper())) {
                result = true;
            }
            if (page.hasData()) {
                List<SubOrderDetailDTO> datas = page.getData();
                for (int i = 0, j = datas.size(); i < j; i++) {
                    SubOrderDetailDTO dto = datas.get(i);
                    subOrderDetailManager.dealData(dto);
                    if (result) {
                        dto.setBuyerInfo(null);
                    }
                }
            }

            response.setResult(page);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询卖家订单列表发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询卖家订单列表发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 分页查询卖家订单数据, 返回少量字段
     *
     * @param params
     * @return
     */
    @GET
    @Path("/simpleOrderList")
    @Override
    public IServiceResponse querySimpleOrderList(@QueryParam("") QuerySellerOrderRequest params) {
        QuerySellerOrderResponse response = new QuerySellerOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }

            Date startOrderCreate = null;
            Date endOrderCreate = null;
            String startOrder = params.getStartOrderCreate();
            String endOrder = params.getEndOrderCreate();
            if (StringUtils.isNotBlank(startOrder)) {
                startOrder += " 00:00:00";
                startOrderCreate = sdf.parse(startOrder);
            }
            if (StringUtils.isNotBlank(endOrder)) {
                endOrder += " 23:59:59";
                endOrderCreate = sdf.parse(endOrder);
            }

            if (params.getPageSize() == null) {
                params.setPageSize(20);
            }

            if (params.getStart() == null) {
                params.setStart(0);
            }

            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("sellerAccount", userInfo.getLoginAccount());
            queryMap.put("sellerUid", userInfo.getUid());
            queryMap.put("orderId", params.getOrderId());
            queryMap.put("gameName", params.getGameName());
            queryMap.put("gameRegion", params.getRegion());
            queryMap.put("gameServer", params.getServer());
            queryMap.put("gameRace", params.getGameRace());
            queryMap.put("startCreateTime", startOrderCreate);
            queryMap.put("endCreateTime", endOrderCreate);
            queryMap.put("isDeleted", false);

            if (StringUtils.isNotBlank(params.getSubOrderId())) {
                queryMap.put("subOrderId", Long.parseLong(params.getSubOrderId()));
            }

            if (StringUtils.isNotBlank(params.getOrderState())) {
                queryMap.put("state", Integer.parseInt(params.getOrderState()));
            }
            if (StringUtils.isNotBlank(params.getIsConsignment())) {
                queryMap.put("isConsignment", Boolean.parseBoolean(params.getIsConsignment()));
            }

            GenericPage<SubOrderDetailDTO> results = subOrderDetailManager.querySellerSimpleOrders(queryMap, "CREATE_TIME",
                    true, params.getPageSize(), params.getStart());

            SellerInfo sellerInfo = sellerManager.findByAccountAndUid(userInfo.getLoginAccount(), userInfo.getUid());
            // 没有小助手权限的卖家，不显示买家信息
            boolean result = false;
            if (sellerInfo != null && (sellerInfo.getIsHelper() == null || !sellerInfo.getIsHelper())) {
                result = true;
            }
            if (results.hasData()) {
                List<SubOrderDetailDTO> datas = results.getData();
                for (int i = 0, j = datas.size(); i < j; i++) {
                    SubOrderDetailDTO dto = datas.get(i);
                    subOrderDetailManager.dealData(dto);
                    if (result) {
                        dto.setBuyerInfo(null);
                    }
                }
            }

            response.setResult(results);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询卖家订单列表发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询卖家订单列表发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 分页查询卖家订单数据, 返回少量字段 ，不需要验证登录权限，只供内部调用
     *
     * @param params
     * @return
     */
    @GET
    @Path("/simpleOrders")
    @Override
    public IServiceResponse querySimpleOrders(@QueryParam("") QuerySellerOrderRequest params) {
        QuerySellerOrderResponse response = new QuerySellerOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            Date startOrderCreate = null;
            Date endOrderCreate = null;
            String startOrder = params.getStartOrderCreate();
            String endOrder = params.getEndOrderCreate();
            if (StringUtils.isNotBlank(startOrder)) {
                startOrder += " 00:00:00";
                startOrderCreate = sdf.parse(startOrder);
            }
            if (StringUtils.isNotBlank(endOrder)) {
                endOrder += " 23:59:59";
                endOrderCreate = sdf.parse(endOrder);
            }

            if (params.getPageSize() == null) {
                params.setPageSize(20);
            }

            if (params.getStart() == null) {
                params.setStart(0);
            }

            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("orderId", params.getOrderId());
            queryMap.put("gameName", params.getGameName());
            queryMap.put("gameRegion", params.getRegion());
            queryMap.put("gameServer", params.getServer());
            queryMap.put("gameRace", params.getGameRace());
            queryMap.put("startCreateTime", startOrderCreate);
            queryMap.put("endCreateTime", endOrderCreate);
            queryMap.put("isDeleted", false);

            if (StringUtils.isNotBlank(params.getSubOrderId())) {
                queryMap.put("subOrderId", Long.parseLong(params.getSubOrderId()));
            }

            if (StringUtils.isNotBlank(params.getOrderState())) {
                queryMap.put("state", Integer.parseInt(params.getOrderState()));
            }
            if (StringUtils.isNotBlank(params.getIsConsignment())) {
                queryMap.put("isConsignment", Boolean.parseBoolean(params.getIsConsignment()));
            }

            GenericPage<SubOrderDetailDTO> results = subOrderDetailManager.querySellerSimpleOrders(queryMap, "CREATE_TIME",
                    true, params.getPageSize(), params.getStart());
            if (results.hasData()) {
                List<SubOrderDetailDTO> data = results.getData();
                for (SubOrderDetailDTO dto : data) {
                    subOrderDetailManager.dealData(dto);
                }
            }
            response.setResult(results);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询卖家订单列表发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询卖家订单列表发生未知异常:{}", ex);
        }
        return response;
    }

    /**
     * 查询订单详情
     *
     * @param orderId    主订单号
     * @param subOrderId 子订单号
     * @return
     */
    @GET
    @Path("/{orderId}/{subOrderId}")
    @Override
    public IServiceResponse queryOrderDetail(@PathParam("orderId") String orderId, @PathParam("subOrderId") Long subOrderId) {
        QuerySellerOrderResponse response = new QuerySellerOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }

            if (StringUtils.isBlank(orderId) || subOrderId == null) {
                throw new SystemException(ResponseCodes.EmptyOrderId.getCode(), ResponseCodes.EmptyOrderId.getMessage());
            } else {
                orderId = StringUtils.trim(orderId);
            }

            Map<String, Object> queryParam = new HashMap<String, Object>();
            queryParam.put("orderId", orderId);
            queryParam.put("subOrderId", subOrderId);
            queryParam.put("sellerUid", userInfo.getUid());
            queryParam.put("sellerAccount", userInfo.getLoginAccount());
            SubOrderDetailDTO subOrderDetail = subOrderDetailManager.querySellerOrderDetail(queryParam);
            if (subOrderDetail != null) {
                SellerInfo sellerInfo = sellerManager.findByAccountAndUid(userInfo.getLoginAccount(), userInfo.getUid());
                if (sellerInfo != null && (sellerInfo.getIsHelper() == null || !sellerInfo.getIsHelper())) {
                    // 没有小助手权限的卖家，不显示买家信息
                    subOrderDetail.setBuyerInfo(null);
                }
            }
            subOrderDetailManager.dealData(subOrderDetail);
            response.setSubOrderDetail(subOrderDetail);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询卖家订单详情发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询卖家订单详情发生未知异常:{}", ex);
        }

        return response;
    }

    /**
     * 移交订单
     *
     * @param orderId    主订单号
     * @param subOrderId 子订单号
     * @return
     */
    @GET
    @Path("/transfer/{orderId}/{subOrderId}")
    @Override
    public IServiceResponse transferOrder(@PathParam("orderId") String orderId, @PathParam("subOrderId") Long subOrderId) {
        TransferOrderResponse response = new TransferOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }

            orderInfoManager.transferOrderForHelper(orderId, subOrderId, userInfo);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            if (!"40014".equals(ex.getErrorCode())) {
                logger.error("小助手订单移交发生异常:{}", ex);
            }
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("小助手订单移交发生未知异常:{}", ex);
        }

        return response;
    }

    /**
     * 查询卖家是否有订单移交权限
     *
     * @return
     */
    @GET
    @Path("/hasTransferOrderPerm")
    @Override
    public IServiceResponse hasTransferOrderPerm() {
        QueryTransferPermResponse response = new QueryTransferPermResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(), ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }
            SellerInfo sellerInfo = sellerManager.findByAccountAndUid(userInfo.getLoginAccount(), userInfo.getUid());
            if (sellerInfo != null) {
                if (sellerInfo.getIsHelper() == null || !sellerInfo.getIsHelper()) {
                    response.setHasTransferOrderPerm(false);
                } else {
                    response.setHasTransferOrderPerm(true);
                }
            } else {
                response.setHasTransferOrderPerm(false);
            }
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询是否有订单移交权限发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询是否有订单移交权限发生未知异常:{}", ex);
        }
        return response;
    }
}
