package com.wzitech.gamegold.order.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * 红包、店铺券
 * @author yemq
 */
public class DiscountCoupon extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 优惠券类型
     * 1:红包
     * 2:店铺券
     */
    private Integer couponType;
    /**
     * 号码
     */
    private String code;
    /**
     * 密码
     */
    private String pwd;
    /**
     * 价值
     */
    private Double price;
    /**
     * 是否被使用
     */
    private Boolean isUsed;
    /**
     * 被使用的订单号
     */
    private String orderId;
    /**
     * 开始期限
     */
    private Date startTime;
    /**
     * 结束期限
     */
    private Date endTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
