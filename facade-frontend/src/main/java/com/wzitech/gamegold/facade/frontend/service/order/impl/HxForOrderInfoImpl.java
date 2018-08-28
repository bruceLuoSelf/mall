package com.wzitech.gamegold.facade.frontend.service.order.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.facade.frontend.service.order.IHxForOrderInfo;
import com.wzitech.gamegold.facade.frontend.service.order.dto.HxForOrderInfoRequest;
import com.wzitech.gamegold.order.business.IHxDataManager;
import com.wzitech.gamegold.order.entity.HxDataEO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by wangmin
 * Date:2017/9/25
 */
@Service("HxOrderService")
@Path("orderdata")
@Produces("application/json;charset=UTF-8")
@Consumes("application/json;charset=UTF-8")
public class HxForOrderInfoImpl extends AbstractBaseService implements IHxForOrderInfo {

    @Autowired
    IHxDataManager hxDataManager;

    /**
     * 获取订单环信信息
     *
     * @return
     */
    @Override
    @GET
    @Path("getOrderIm")
    public String getOrderIm(@QueryParam("") HxForOrderInfoRequest orderrequest) {
        if (StringUtils.isEmpty(orderrequest.getOrderId())) {
            return "";
        }
        if (orderrequest.getUserType() == null) {
            return "";
        }
        String json = "";
        try {
            HxDataEO hx = hxDataManager.selectBuyerHxDataByOrderId(orderrequest.getOrderId(), orderrequest.getUserType());
            if (hx == null) {
                return "";
            }

            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(hx);

            String callback = orderrequest.getCallback();
            if (StringUtils.isBlank(callback)) {
                callback = "jsonp";
            }
            json = callback + "(" + json + ")";
        } catch (SystemException ex) {
            // 捕获系统异常
            logger.error("获取订单环信信息发生异常:{}", ex);
            return "{" + ex.getErrorCode() + ":" + ex.getArgs()[0].toString() + "}";
        } catch (Exception ex) {
            logger.error("获取订单环信信息发生未知异常{}", ex);
            return "{" + ex.getMessage() + ":" + ex.toString() + "}";
        }
        return json;
    }
}
