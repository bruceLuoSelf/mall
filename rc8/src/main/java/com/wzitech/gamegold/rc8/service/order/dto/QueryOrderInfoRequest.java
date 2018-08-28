package com.wzitech.gamegold.rc8.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 查询订单详情请求
 *
 * @author yemq
 */
public class QueryOrderInfoRequest extends AbstractServiceRequest {
    private String name;
    private String pwd;
    private String sign;
    private String version;

    public QueryOrderInfoRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
