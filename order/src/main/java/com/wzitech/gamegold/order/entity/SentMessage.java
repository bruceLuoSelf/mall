package com.wzitech.gamegold.order.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * 已发送的短信
 *
 * @author yemq
 */
public class SentMessage extends BaseEntity {
    /**
     * 关联的规则ID
     */
    private Long messageRuleId;
    /**
     * 关联的订单
     */
    private OrderInfoEO order;
    /**
     * 触发的订单状态
     */
    private Integer orderStatus;
    /**
     * 短信内容
     */
    private String content;
    /**
     * 发送时间
     */
    private Date createTime;

    public SentMessage() {
    }

    public Long getMessageRuleId() {
        return messageRuleId;
    }

    public void setMessageRuleId(Long messageRuleId) {
        this.messageRuleId = messageRuleId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderInfoEO getOrder() {
        return order;
    }

    public void setOrder(OrderInfoEO order) {
        this.order = order;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
