package com.wzitech.gamegold.shrobot.service.repository.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 写分仓日志请求
 * @author yemq
 */
public class WriteRepositoryLogRequest extends AbstractServiceRequest {
    /**
     * 分仓订单号
     */
    private String orderId;
    /**
     * 截图
     */
    private String img;
    /**
     * 日志内容
     */
    private String log;

    /**
     * 签名
     */
    private String sign;

    public WriteRepositoryLogRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
