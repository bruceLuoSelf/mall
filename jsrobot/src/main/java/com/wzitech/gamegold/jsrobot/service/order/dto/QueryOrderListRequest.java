package com.wzitech.gamegold.jsrobot.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 查询订单列表请求
 * @author yemq
 */
public class QueryOrderListRequest extends AbstractServiceRequest {
    private Integer pageSize;
    private Integer page;
    private String sign;

    public QueryOrderListRequest() {
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
