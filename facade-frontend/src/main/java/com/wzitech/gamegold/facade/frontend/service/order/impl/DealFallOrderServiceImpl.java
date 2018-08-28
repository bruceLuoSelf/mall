package com.wzitech.gamegold.facade.frontend.service.order.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.paymgmt.dto.VaQueryDetailResponse;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.facade.frontend.service.order.IDealFallOrderService;
import com.wzitech.gamegold.facade.frontend.service.order.dto.DealFallOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.DealFallOrderResponse;
import com.wzitech.gamegold.order.business.IAutoPayManager;
import com.wzitech.gamegold.repository.business.ISellerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.io.IOException;


/**
 * @author 340096
 * @date 2018/1/24.
 * 掉单处理
 */
@Service("DealFallOrderService")
@Path("/dealfallorder")
@Produces("application/json;charset=UTF-8")
public class DealFallOrderServiceImpl extends AbstractBaseService implements IDealFallOrderService {

    @Autowired
    private IAutoPayManager autoPayManager;

    @Value("${7Bao.fund.serKey}")
    private String encrypt;

    /**
     * 订单类型：业务订单
     */
    private final static int OUR_ORDER = 1;

    /**
     * 订单类型：主站订单
     */
    private final static int MAIN_ORDER = 2;

    @POST
    @Path("/malldeal")
    public DealFallOrderResponse mallDealFallOrder(DealFallOrderRequest dealFallOrderRequest, @Context HttpServletRequest request) {
        ResponseStatus responseStatus = new ResponseStatus();
        DealFallOrderResponse response = new DealFallOrderResponse();
        response.setResponseStatus(responseStatus);

        //获取传过来的参数
        String orderIdFromZBao = dealFallOrderRequest.getOrderId();
        int orderType = dealFallOrderRequest.getOrderType();

        try {
            //验证签名
            String format = String.format("%s_%s_%s", orderIdFromZBao, orderType, encrypt);
            logger.info("补单商城接收参数：{}", format);
            String toEncrypt = EncryptHelper.md5(format);

            //签名不匹配直接返回
            if (!toEncrypt.equals(dealFallOrderRequest.getSign())) {
                responseStatus.setStatus(ResponseCodes.InvalidSign.getCode(), ResponseCodes.InvalidSign.getMessage());
                return response;
            }

            //判断参数是否为空
            if (orderIdFromZBao == null || orderType == 0) {
                responseStatus.setStatus(ResponseCodes.NullData.getCode(), ResponseCodes.NullData.getMessage());
                return response;
            }

            VaQueryDetailResponse vaQueryDetailResponse = null;
            // 1.如果传过来的是商城订单
            if (orderType == OUR_ORDER) {
                //根据商城订单查出主站订单号
                vaQueryDetailResponse = autoPayManager.queryWithdrawalsDetailByBillId(orderIdFromZBao);
            }
            // 2.如果传过来的是主站订单号
            else if (orderType == MAIN_ORDER) {
                //查出商城订单号
                vaQueryDetailResponse = autoPayManager.queryWithdrawalsDetailByOrderId(orderIdFromZBao);
            } else {
                responseStatus.setStatus(ResponseCodes.UnknowType.getCode(), ResponseCodes.UnknowType.getMessage());
                return response;
            }
            logger.info("查询资金明细，vaQueryDetailResponse:{}", vaQueryDetailResponse);

            if (vaQueryDetailResponse == null) {
                responseStatus.setStatus(ResponseCodes.WrongfulOrder.getCode(), ResponseCodes.WrongfulOrder.getMessage());
                return response;
            }

            //获取从主站取过来的要补单的数据，返回给7bao
            response.setUserId(vaQueryDetailResponse.getUserId());
            response.setOutOrderId(vaQueryDetailResponse.getOrderId());
            response.setMallOrderId(vaQueryDetailResponse.getBillId());
            response.setRealMoney(vaQueryDetailResponse.getMoneyRequest());

            responseStatus.setStatus(ResponseCodes.Success.getCode(), ResponseCodes.Success.getMessage());
        } catch (SystemException ex) {
            // 捕获系统异常
            responseStatus.setStatus(ex.getErrorCode(), ex.getArgs()[0].toString());
            logger.error("查询资金明细发生异常:{}", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            responseStatus.setStatus(ResponseCodes.UnKnownError.getCode(), ResponseCodes.UnKnownError.getMessage());
            logger.error("查询资金明细发生未知异常:{}", ex);
        }
        logger.info("查询资金明细响应信息:{}", response);
        return response;
    }
}
