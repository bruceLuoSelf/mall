package com.wzitech.gamegold.shrobot.service.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 订单完成请求
 *
 * @author yemq
 */
public class OrderFinishRequest extends AbstractServiceRequest {
    /**
     * 主订单号
     */
    private String orderId;

    /**
     * 子订单号
     */
    private Long id;
    /**
     * 类型
     * <ul>
     * <li>100：交易完成</li>
     * <li>101:每次收货更新数量，不更新状态</li>
     * <li>200：需人工介入</li>
     * <li>300：其他情况</li>
     * <li>301：交易超时</li>
     * <li>302：背包已满</li>
     * <li>303：不是附魔师</li>
     * <li>304:帐号密码错</li>
     * <li>305：动态令牌</li>
     * <li>306：不在交易地点</li>
     * <li>307；出战状态</li>
     * <li>350: 异常超时撤单</li>
     * <li>351：异常超时人工介入</li>
     * </ul>
     */
    private Integer type;
    /**
     * 总共收到数量
     */
    private Long receiveCount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 签名
     */
    private String sign;

    /**
     * 操作类型
     * 1.交易成功
     * 2.部分完单
     * 3.撤单
     * 4.人工介入
     * 5.更新前台库存
     */
    private String serviceId;

    /**
     * 是否下架
     */
    private Boolean offline;

    public OrderFinishRequest() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Boolean getOffline() {
        return offline;
    }

    public void setOffline(Boolean offline) {
        this.offline = offline;
    }
}
