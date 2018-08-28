package com.wzitech.gamegold.order.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * 保单信息（只存放发送失败的保单）
 * @author yemq
 */
public class InsuranceOrder extends BaseEntity {
    /**
     * 类型：待创建保单
     */
    public static final int TYPE_CREATE_ORDER = 0;
    /**
     * 类型：待结单
     */
    public static final int TYPE_MODIFY_TRANSFER_TIME = 1;
    /**
     * 类型：已结单
     */
    public static final int TYPE_ALREADY_MODIFY_TRANSFER_TIME = 2;
    /**
     * 订单号
     */
    private String orderId;
    private OrderInfoEO order;
    /**
     * 详情
     */
    private String detail;
    /**
     * 类型
     * <li>0-待创建保单</li>
     * <li>1-待结单</li>
     * <li>2-已结单</li>
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    public InsuranceOrder() {}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public OrderInfoEO getOrder() {
        return order;
    }

    public void setOrder(OrderInfoEO order) {
        this.order = order;
    }
}
