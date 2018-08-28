package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserData;

import java.util.List;

/**
 * 支付单操作响应
 */
public class PayOrderResponse extends AbstractServiceResponse {
    /**
     * 支付单
     */
    private List<PayOrder> payOrderList;
    /**
     * 采购商统计数据
     */
    private PurchaserData purchaserData;
    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 当前是第几页
     */
    private Integer currPage;
    /**
     * 总记录数
     */
    private Long totalCount;
    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 单号
     */
    private String orderId;

    private PayOrder payOrder;

    public List<PayOrder> getPayOrderList() {
        return payOrderList;
    }

    public void setPayOrderList(List<PayOrder> payOrderList) {
        this.payOrderList = payOrderList;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public PurchaserData getPurchaserData() {
        return purchaserData;
    }

    public void setPurchaserData(PurchaserData purchaserData) {
        this.purchaserData = purchaserData;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public PayOrder getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(PayOrder payOrder) {
        this.payOrder = payOrder;
    }
}
