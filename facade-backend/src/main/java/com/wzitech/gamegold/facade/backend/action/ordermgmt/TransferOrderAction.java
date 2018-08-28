package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.google.common.collect.Lists;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 订单移交，只能移交担保的订单
 * @author yemq
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class TransferOrderAction extends AbstractAction {
    @Autowired
    IOrderInfoManager orderInfoManager;

    /**
     * 格式：订单号_配单号,例如：YX1503130000773_2014020，如果只有一笔配单，可以只传订单号(例如：YX1503130000773)
     */
    private String id;

    /**
     * 批量订单号
     */
    private String orderIds;

    /**
     * 移交成功的订单号
     */
    private List<String> successOrders = Lists.newArrayList();

    /**
     * 移交失败的订单号
     * @return
     */
    private List<String> failureOrders = Lists.newArrayList();

    public String transferOrder() {
        try {
            orderInfoManager.transferOrder(id);
            return returnSuccess();
        } catch (SystemException e) {
            return returnError(e);
        }
    }

    /**
     * 批量移交订单
     * @return
     */
    public String batchTransferOrder() {
        SystemException emptyOrderIdsException = new SystemException(ResponseCodes.IllegalArguments.getCode(),
                "订单号不能为空，规则一行一个订单号！");

        try {
            if (StringUtils.isBlank(orderIds)) {
                throw emptyOrderIdsException;
            }

            String[] orderIdArray = orderIds.split("\n");
            if (orderIdArray == null || orderIdArray.length == 0) {
                throw emptyOrderIdsException;
            }
            logger.info("批量移交的订单号：{}", orderIdArray);
            for (String orderId : orderIdArray) {
                if (StringUtils.isBlank(orderId))
                    continue;
                else
                    orderId = orderId.trim();

                if (!orderId.startsWith("YX")) {
                    failureOrders.add(orderId);
                    continue;
                }

                try {
                    orderInfoManager.transferOrder(orderId);
                    successOrders.add(orderId);
                } catch (SystemException e) {
                    logger.error(e.getMessage(), e);
                    failureOrders.add(orderId);
                }
            }

            return returnSuccess();
        } catch (SystemException e) {
            return returnError(e);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public List<String> getSuccessOrders() {
        return successOrders;
    }

    public List<String> getFailureOrders() {
        return failureOrders;
    }
}
