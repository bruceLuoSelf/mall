package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.facade.frontend.service.shorder.IPayOrderService;
import com.wzitech.gamegold.facade.frontend.service.shorder.ISellerData;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PayOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.shorder.dto.PayOrderResponse;
import com.wzitech.gamegold.order.business.IZbaoFundManager;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.FundType;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 支付单
 */
@Service("PayOrderService")
@Path("payOrder")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class PayOrderServiceImpl extends AbstractBaseService implements IPayOrderService {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    IPayOrderManager payOrderManager;

    @Autowired
    ISellerData sellerData;

    @Autowired
    ISystemConfigManager systemConfigManager;

    @Autowired
    IPurchaserDataManager purchaserDataManager;

    @Autowired
    IZbaoFundManager zbaoFundManager;

    @Value("${zbaoMd5Key}")
    private String zbaoMd5Key = "";

    @Override
    @GET
    @Path("deletePayOrder")
    public IServiceResponse deletePayOrder(@QueryParam("loginAccount") String loginAccount, @QueryParam("changeAmount") BigDecimal changeAmount, @QueryParam("sign") String sign) {
        PayOrderResponse response = new PayOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            String signz = DigestUtils.md5Hex(loginAccount + changeAmount.toString() + zbaoMd5Key);
            if (!signz.equals(sign)) {
                responseStatus.setStatus(ResponseCodes.EntrycpyFail.getCode(), ResponseCodes.EntrycpyFail.getMessage());
                return response;
            }
            payOrderManager.deletePayOrderCaseWithdraw(loginAccount, changeAmount);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            logger.info("因提现成功删除采购单成功,对应账号:" + loginAccount + ",变动金额:" + changeAmount);
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("删除采购单数据异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("删除采购单数据发生未知异常:{}", e);
        }
        return response;
    }

    @Override
    @GET
    @Path("updatePurchaseDataWithZbao")
    public IServiceResponse updatePurchaseData(@QueryParam("loginAccount") String loginAccount, @QueryParam("sign") String sign, @QueryParam("changeAmount") BigDecimal changeAmount, @QueryParam("orderId") String orderId) {
        PayOrderResponse response = new PayOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            logger.info("从7Bao同步商城资金开始,登录号：" + loginAccount + ",对应7bao订单为:" + orderId);
            if (StringUtils.isBlank(sign)) {
                responseStatus.setStatus(ResponseCodes.EntrycpyFail.getCode(), ResponseCodes.EntrycpyFail.getMessage());
                return response;
            }
            if (StringUtils.isBlank(loginAccount)) {
                responseStatus.setStatus(ResponseCodes.EmptyPurchaseAccount.getCode(), ResponseCodes.EmptyPurchaseAccount.getMessage());
                return response;
            }
            if (changeAmount == null) {
                responseStatus.setStatus(ResponseCodes.EmptyAmount.getCode(), ResponseCodes.EmptyAmount.getMessage());
                return response;
            }
            if (StringUtils.isBlank(orderId)) {
                responseStatus.setStatus(ResponseCodes.NullZbaoRelatedOrderId.getCode(), ResponseCodes.NullZbaoRelatedOrderId.getMessage());
                return response;
            }
            String signz = DigestUtils.md5Hex(changeAmount.toString() + loginAccount + orderId + zbaoMd5Key);
            if (!sign.equals(signz)) {
                responseStatus.setStatus(ResponseCodes.EntrycpyFail.getCode(), ResponseCodes.EntrycpyFail.getMessage());
                return response;
            }
            zbaoFundManager.recharge7Bao(loginAccount, changeAmount, FundType.RECHARGE7Bao.getCode(), orderId);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            logger.info("7Bao同步商城资金成功,登录号:" + loginAccount + ",对应7bao订单为:" + orderId);
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("同步收货商数据异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("同步收货商数据发生未知异常:{}", e);
        }
        return response;
    }


    @Path("queryPurchaseDataFromZbao")
    @Override
    @GET
    public IServiceResponse queryPurchaseData(@QueryParam("loginAccount") String loginAccount, @QueryParam("sign") String sign) {
        PayOrderResponse response = new PayOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            logger.info("从7Bao查询商城资金开始,登录号：" + loginAccount);
            String entryc = DigestUtils.md5Hex(loginAccount + zbaoMd5Key);
            if (!entryc.equals(sign) || StringUtils.isBlank(sign)) {
                responseStatus.setStatus(ResponseCodes.EntrycpyFail.getCode(), ResponseCodes.EntrycpyFail.getMessage());
                return response;
            }
            if (StringUtils.isBlank(loginAccount)) {
                responseStatus.setStatus(ResponseCodes.EmptyPurchaseAccount.getCode(), ResponseCodes.EmptyPurchaseAccount.getMessage());
                return response;
            }
            PurchaserData purchaserData = purchaserDataManager.selectByAccount(loginAccount);
            if (purchaserData == null) {
                responseStatus.setStatus(ResponseCodes.NoPurchaseData.getCode(), ResponseCodes.NoPurchaseData.getMessage());
                return response;
            }
            response.setPurchaserData(purchaserData);
            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
            logger.info("7bao查询商城资金成功,登录号:" + loginAccount);
        } catch (SystemException e) {
            responseStatus.setStatus(e.getErrorCode(), e.getArgs()[0].toString());
            logger.error("查询收货商数据异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询收货商数据发生未知异常:{}", e);
        }
        return response;
    }

    /**
     * 查询分页支付单
     *
     * @param payOrderRequest
     * @param request
     * @return
     */
    @Path("queryPayOrderList")
    @GET
    @Override
    public IServiceResponse queryPayOrderList(@QueryParam("") PayOrderRequest payOrderRequest, @Context HttpServletRequest request) {
        PayOrderResponse response = new PayOrderResponse();
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

            //分页
            int page = payOrderRequest.getPage();
            int pageSize = payOrderRequest.getPageSize();
            if (page <= 0)
                page = 1;
            if (pageSize <= 0)
                pageSize = 25;
            int start = (page - 1) * pageSize;

            String startTime = payOrderRequest.getStartTime();
            String endTime = payOrderRequest.getEndTime();
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
            if (startOrderCreate != null && endOrderCreate != null) {
                if (daysBetween(startOrderCreate, endOrderCreate) > 30) {
                    responseStatus.setStatus(ResponseCodes.Over30Days.getCode(), ResponseCodes.Over30Days.getMessage());
                    return response;
                }
            }

            //分页获取数据库中的支付单
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("account", userInfo.getLoginAccount());
            paramMap.put("startCreateTime", startOrderCreate);
            paramMap.put("endCreateTime", endOrderCreate);
            paramMap.put("orderIdFund", "SHZF");
            if (payOrderRequest.getStatus() != null && !payOrderRequest.getStatus().equals("")) {
                paramMap.put("status", Integer.parseInt(payOrderRequest.getStatus()));
            }

            GenericPage<PayOrder> genericPage = payOrderManager.queryPayOrders(paramMap, start, pageSize,
                    "create_time", false);
            List<PayOrder> data = genericPage.getData();
            Long totalCount = genericPage.getTotalCount();

            PurchaserData purchaserData = purchaserDataManager.selectByAccount(userInfo.getLoginAccount());
            if (purchaserData != null) {
                response.setPurchaserData(purchaserData);
            }
            //返回数据
            response.setPayOrderList(data);
            response.setTotalCount(totalCount);
            response.setPageSize(pageSize);
            response.setCurrPage(start);
            response.setTotalPage(genericPage.getTotalPageCount());
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
     * 新增支付单
     *
     * @param payOrderRequest
     * @param request
     * @return
     */
    @Path("addPayOrder")
    @GET
    @Override
    public IServiceResponse addPayOrder(@QueryParam("") PayOrderRequest payOrderRequest, @Context HttpServletRequest
            request) {
        PayOrderResponse response = new PayOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        BigDecimal big = new BigDecimal("3000");
        int i = payOrderRequest.getAmount().compareTo(big);
        if (i > 0) {
            throw new IllegalArgumentException(String.valueOf(ResponseCodes.AmountTooLarge));
        }

        try {
            // 获取当前用户
            UserInfoEO userInfo = (UserInfoEO) CurrentUserContext.getUser();
            if (userInfo == null) {
                responseStatus.setStatus(ResponseCodes.InvalidAuthkey.getCode(),
                        ResponseCodes.InvalidAuthkey.getMessage());
                return response;
            }

            // 检查卖家信息
            if (!sellerData.checkNewFundSellerForRecharge(response)) {
                return response;
            }
            SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.RECHARGE_MIN.getKey());
            if (systemConfig != null) {
                if (StringUtils.isNotBlank(systemConfig.getConfigValue()) && new BigDecimal(systemConfig.getConfigValue()).compareTo(payOrderRequest.getAmount()) > 0) {
                    responseStatus.setStatus(ResponseCodes.SellerMinMoney.getCode(), ResponseCodes.SellerMinMoney.getMessage() + systemConfig.getConfigValue() + "元");
                    return response;
                }
            }

            if (payOrderRequest.getAmount().compareTo(new BigDecimal(99999999)) > 0) {
                responseStatus.setStatus(ResponseCodes.SellerMaxMoney.getCode(), ResponseCodes.SellerMaxMoney.getMessage());
                return response;
            }

            //分页获取数据库中的支付单
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("amount", payOrderRequest.getAmount());
            if (payOrderRequest.getStatus() != null && !payOrderRequest.getStatus().equals("")) {
                paramMap.put("status", Integer.parseInt(payOrderRequest.getStatus()));
            }

            String orderId = payOrderManager.createOrder(payOrderRequest.getAmount());
            response.setOrderId(orderId);
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
     * 根据单号查询支付单信息
     *
     * @param orderId
     * @return
     */
    @Path("queryPayOrderById")
    @GET
    @Override
    public IServiceResponse queryPayOrderById(@QueryParam("orderId") String orderId, @QueryParam("sign") String sign) {
        PayOrderResponse response = new PayOrderResponse();
        ResponseStatus responseStatus = new ResponseStatus();
        response.setResponseStatus(responseStatus);
        try {
            String signz = DigestUtils.md5Hex(orderId + zbaoMd5Key);
            if (!signz.equals(sign)) {
                responseStatus.setStatus(ResponseCodes.EntrycpyFail.getCode(), ResponseCodes.EntrycpyFail.getMessage());
                return response;
            }
            PayOrder payOrder = payOrderManager.queryByOrderId(orderId, false);
            response.setPayOrder(payOrder);
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
}
