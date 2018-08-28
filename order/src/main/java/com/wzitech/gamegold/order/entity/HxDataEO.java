package com.wzitech.gamegold.order.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 环信订单信息
 * Created by 汪俊杰 on 2017/9/25.
 */
public class HxDataEO{
    /**
     * 订单号
     */
    @JsonProperty("OrderId")
    private String orderId;

    /**
     * 用户类型
     */
    @JsonProperty("UserType")
    private Integer userType;

    /**
     * 客服登录名
     */
    @JsonProperty("CustomServiceLoginId")
    private String customServiceLoginId;

    /**
     * 	前台显示昵称
     */
    @JsonProperty("CustomServiceName")
    private String customServiceName;

    /**
     * 客服QQ
     */
    @JsonProperty("CustomServiceQQ")
    private String customServiceQQ;

    /**
     * 是否开启环信
     */
    @JsonProperty("IsOpenHX")
    private Boolean isOpenHX;

    /**
     * 客服环信帐号
     */
    @JsonProperty("HXCustomService")
    private String hxCustomService;

    //zhulf  start
    /**
     * 客服云信帐号
     */
    @JsonProperty("WYCustomService")
    private String wyCustomService;

    /**
     * 系统默认通讯方式
     * 0：QQ  1：环信  2：云信
     */
    @JsonProperty("DefaultIM")
    private Integer defaultIM;
    //end

    /**
     * 客服环信是否在线
     */
    @JsonProperty("CustomServiceStatus")
    private Integer customServiceStatus;

    /**
     * 订单信息
     */
    @JsonProperty("OrderInfo")
    private List<HxOrderInfoEO> orderInfo;

    public String getCustomServiceLoginId() {
        return customServiceLoginId;
    }

    public void setCustomServiceLoginId(String customServiceLoginId) {
        this.customServiceLoginId = customServiceLoginId;
    }

    public String getCustomServiceName() {
        return customServiceName;
    }

    public void setCustomServiceName(String customServiceName) {
        this.customServiceName = customServiceName;
    }

    public String getCustomServiceQQ() {
        return customServiceQQ;
    }

    public void setCustomServiceQQ(String customServiceQQ) {
        this.customServiceQQ = customServiceQQ;
    }

    public Boolean getIsOpenHX() {
        return isOpenHX;
    }

    public void setIsOpenHX(Boolean isOpenHX) {
        this.isOpenHX = isOpenHX;
    }

    public String getHxCustomService() {
        return hxCustomService;
    }

    public void setHxCustomService(String hxCustomService) {
        this.hxCustomService = hxCustomService;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public List<HxOrderInfoEO> getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(List<HxOrderInfoEO> orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Integer getCustomServiceStatus() {
        return customServiceStatus;
    }

    public void setCustomServiceStatus(Integer customServiceStatus) {
        this.customServiceStatus = customServiceStatus;
    }

    public String getWyCustomService() {
        return wyCustomService;
    }

    public void setWyCustomService(String wyCustomService) {
        this.wyCustomService = wyCustomService;
    }

    public Integer getDefaultIM() {
        return defaultIM;
    }

    public void setDefaultIM(Integer defaultIM) {
        this.defaultIM = defaultIM;
    }
}
