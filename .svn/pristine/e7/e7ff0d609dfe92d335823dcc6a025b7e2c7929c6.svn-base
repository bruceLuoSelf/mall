package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderLogManager;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * 收货日志
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ShOrderLogAction extends AbstractAction {
    /**
     * 订单号
     */
    private String orderId;
    private String deliveryType;
    private String sellerAccount;

    private List<OrderLog> orderLogList;

    @Autowired
    IDeliveryOrderLogManager deliveryOrderLogManager;

    /**
     * 根据订单号查询收货日志
     *
     * @return
     */
    public String queryOrderLogByOrderId() {
        if (new Integer(deliveryType) == ShDeliveryTypeEnum.Robot.getCode()) {
            orderLogList = deliveryOrderLogManager.getByOrderId(orderId);
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderId", orderId);
            map.put("userAccount", sellerAccount);
            List<OrderLog> logs = new ArrayList<OrderLog>();
            logs = deliveryOrderLogManager.queryAllByMap(map);
            orderLogList = new ArrayList<OrderLog>();
            String[] logContent = {"", "收货商：", "出货商：", "系统消息："};
            String content = "";
            for (OrderLog log : logs) {
                if (log == null || log.getUserType() == null || log.getUserType() >= logContent.length || StringUtils.isBlank(log.getLog()) ||
                        log.getLog().contains(logContent[log.getUserType()])) {
                    continue;
                }
                content = logContent[log.getUserType()] + log.getLog();
                log.setLog(content);
                orderLogList.add(log);
            }
        }
        return this.returnSuccess();
    }

    public List<OrderLog> getOrderLogList() {
        return orderLogList;
    }

    public void setOrderLogList(List<OrderLog> orderLogList) {
        this.orderLogList = orderLogList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }
}
