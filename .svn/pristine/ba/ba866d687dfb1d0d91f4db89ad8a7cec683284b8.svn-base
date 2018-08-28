package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.accessLimit.AccessLimit;
import com.wzitech.gamegold.facade.frontend.service.shorder.IPurchaseOrderService;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISellerData;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PurchaseOrderResponse;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import com.wzitech.gamegold.shorder.business.IPurchaseOrderManager;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.business.IPurchaserGameTradeManager;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购单
 * Created by 335854 on 2016/3/29.
 */
@Service("PurchaseOrderService")
@Path("purchaseOrder")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class PurchaseOrderServiceImpl extends AbstractBaseService implements IPurchaseOrderService {
    @Autowired
    IPurchaseOrderManager purchaseOrderManager;
    @Autowired
    ISellerManager sellerManager;
    @Autowired
    ISellerData sellerData;
    @Autowired
    IGameAccountManager gameAccountManager;
    @Autowired
    IPurchaserDataManager purchaserDataManager;
    @Autowired
    private IPurchaserGameTradeManager purchaserGameTradeManager;

    /**
     * 查询该采购方的分页采购单
     *
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    @Path("queryPurchaseOrderList")
    @GET
    @Override
    public IServiceResponse queryPurchaseOrderList(@QueryParam("") PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {
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

            // 检查卖家信息
            if (!sellerData.checkSeller(response)) {
                return response;
            }

            //分页
            int page = purchaseOrderRequest.getPage();
            int pageSize = purchaseOrderRequest.getPageSize();
            if (page <= 0)
                page = 1;
            if (pageSize <= 0)
                pageSize = 25;
            int start = (page - 1) * pageSize;

            //分页获取数据库中的采购单数据
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", purchaseOrderRequest.getGameName());
            paramMap.put("region", purchaseOrderRequest.getRegion());
            paramMap.put("server", purchaseOrderRequest.getServer());
            paramMap.put("buyerAccount", userInfo.getLoginAccount());
            paramMap.put("deliveryType", purchaseOrderRequest.getDeliveryType());

            GenericPage<PurchaseOrder> genericPage = purchaseOrderManager.queryPurchaseOrder(paramMap, pageSize, start, "", true);
            List<PurchaseOrder> data = genericPage.getData();
            //防止单位为空时页面单位显示异常
            if (!CollectionUtils.isEmpty(data)) {
                for (PurchaseOrder entity : data) {
                    entity.setMoneyName(entity.getMoneyName() == null ? "" : entity.getMoneyName());
                }
            }
            Long totalCount = genericPage.getTotalCount();

            //返回数据
            response.setPurchaseOrderList(data);
            response.setTotalCount(totalCount);
            response.setPageSize(pageSize);
            response.setCurrPage(start);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取采购单发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取采购单发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * 上下架
     */
    @Path("onlineAll")
    @POST
    @Override
    public IServiceResponse onlineAll(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {

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

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", purchaseOrderRequest.getGameName());
            paramMap.put("region", purchaseOrderRequest.getRegion());
            paramMap.put("server", purchaseOrderRequest.getServer());
            paramMap.put("buyerAccount", userInfo.getLoginAccount());
            paramMap.put("deliveryType", purchaseOrderRequest.getDeliveryType());
            paramMap.put("isOnline", purchaseOrderRequest.getIsOnline());

            purchaseOrderManager.onlineAll(paramMap);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
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
     * 批量设置采购单的上下架
     *
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    @Path("setPurchaseOrderOnline")
    @POST
    @Override
    public IServiceResponse setPurchaseOrderOnline(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {
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
            // 检查卖家信息
            if (!sellerData.checkSeller(response)) {
                return response;
            }
            // 判断采购数量
//            if(purchaseOrderRequest.getIsOnline()){
//            Long count=purchaseOrderRequest.getCount();
//            if(count==null||count<=0){
//                responseStatus.setStatus(ResponseCodes.EmptyShGamecount.getCode(),
//                        ResponseCodes.EmptyShGamecount.getMessage());
//                return response;
//            }}
            //根据id批量设置采购单的上下架
            purchaseOrderManager.setPurchaseOrderOnline(purchaseOrderRequest.getIds(), purchaseOrderRequest
                    .getIsOnline(), userInfo.getLoginAccount());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
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
     * 修改当前采购单的采购单价和采购量
     *
     * @param purchaseOrderRequest
     * @param request
     * @return
     */
    @Path("updatePurchaseOrderPriceAndCount")
    @POST
    @Override
    @AccessLimit
    public IServiceResponse updatePurchaseOrderPriceAndCount(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {
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

            // 检查卖家信息
            if (!sellerData.checkSeller(response)) {
                return response;
            }

            //根据采购单的id更新采购单中的采购价格和采购数量，以及更新该区服下所有账号角色的采购价格
            Long id = purchaseOrderRequest.getId();
            BigDecimal price = purchaseOrderRequest.getPrice();
            Long count = purchaseOrderRequest.getCount();
            boolean f = purchaseOrderManager.updatePurchaseOrderPriceAndCount(id, price, count, purchaseOrderRequest
                    .getGameAccountMap());

            if (f) {
                responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            } else {
                responseStatus.setStatus(ResponseCodes.SaveFaile.getCode(), ResponseCodes.SaveFaile.getMessage());
            }
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("修改采购单的采购价格和最小采购量发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("修改采购单的采购价格和最小采购量发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * 前台分页获取采购单列表
     *
     * @param purchaseOrder
     * @param page
     * @param pageSize
     * @param fieldName
     * @param sortName
     * @param request
     * @return
     */
    @GET
    @Path("/page")
    @Override
    public PurchaseOrderResponse queryPurchaseOrder(@QueryParam("") PurchaseOrder purchaseOrder,
                                                    @QueryParam("page") Integer page,
                                                    @QueryParam("pageSize") Integer pageSize,
                                                    @QueryParam("fieldName") String fieldName,
                                                    @QueryParam("sort") String sortName,
                                                    @Context HttpServletRequest request) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);

        try {
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

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("gameName", purchaseOrder.getGameName());
            paramMap.put("region", purchaseOrder.getRegion());
            paramMap.put("server", purchaseOrder.getServer());
            paramMap.put("gameRace", purchaseOrder.getGameRace());
            paramMap.put("count", 0);

            List<SortField> sortFields = Lists.newArrayList();
            if (StringUtils.isNotBlank(fieldName) && StringUtils.isNotBlank(sortName)) {
                sortFields.add(new SortField(fieldName, sortName));
            }
            sortFields.add(new SortField("game_name", SortField.ASC));
            sortFields.add(new SortField("region", SortField.ASC));
            sortFields.add(new SortField("server", SortField.ASC));

            GenericPage<PurchaseOrder> genericPage = purchaseOrderManager.selectOrderList(paramMap, sortFields, start, pageSize);
            List<PurchaseOrder> data = genericPage.getData();
            if (!CollectionUtils.isEmpty(data)) {
                for (PurchaseOrder purchase : data) {
                    if (purchaserGameTradeManager.setParameter(purchase)) {
                        continue;
                    }
                }
            }
            Long totalCount = genericPage.getTotalCount();

            response.setPurchaseOrderList(data);
            response.setTotalCount(totalCount);
            response.setPageSize(pageSize);
            response.setCurrPage(page);
            response.setTotalPage(genericPage.getTotalPageCount());
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("获取前台采购单发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("获取前台采购单发生未知异常:{}", e);
        }

        return response;
    }

    /**
     * 批量删除采购单
     * ZW_C_JB_00004 mj
     *
     * @param purchaseOrderRequest
     * @return
     */
    @Path("deleteAll")
    @POST
    @Override
    public IServiceResponse deleteAll(PurchaseOrderRequest purchaseOrderRequest, @Context HttpServletRequest request) {
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

            // 检查卖家信息
            if (!sellerData.checkSeller(response)) {
                return response;
            }

            if (purchaseOrderRequest.getIds() == null || purchaseOrderRequest.getIds().size() < 1) {
                responseStatus.setStatus(ResponseCodes.EmptyPurchaseOrder.getCode(),
                        ResponseCodes.EmptyPurchaseOrder.getMessage());
                return response;
            }
            //删除采购单和对应的所有收货角色
            String message = gameAccountManager.deleteGameAccountByPurchaseOrderId(purchaseOrderRequest.getIds(), userInfo.getLoginAccount());

            responseStatus.setStatus(ResponseCodes.Success.getCode(), message);
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.info("采购单删除发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.info("采购单删除发生未知异常:{}", e);
        }
        return response;
    }
}