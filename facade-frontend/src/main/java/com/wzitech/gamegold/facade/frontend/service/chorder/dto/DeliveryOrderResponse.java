package com.wzitech.gamegold.facade.frontend.service.chorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.*;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.List;

/**
 * 下单
 * Created by 335854 on 2016/4/6.
 */
public class DeliveryOrderResponse extends AbstractServiceResponse {
    /**
     * 订单号
     */
    private String orderId;
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
     * 订单列表
     */
    private List<DeliveryOrder> deliveryOrderList;

    /**
     * 子订单列表
     */
    private List<DeliverySubOrder> deliverySubOrderList;

    private PurchaserData purchaserData;

    /**
     * 日志列表
     */
    private List<OrderLog> orderLogList;

    /**
     * 订单
     */
    private DeliveryOrder deliveryOrder;


    /**
     * 当前登录的用户
     */
    private UserInfoEO userInfo;


    /**
     * 采购单以及采购方信息
     */
    private PurchaseOrder purchaseOrder;

    /**
     * 用户提醒信息上方
     */
    private String warningInfoUP;

    /**
     * 用户提醒信息下方
     */
    private String warningInfoDOWN;

    /**
     * 获取交易信息
     */
    private PurchaseConfig purchaseConfig;

    private DeliveryConfig deliveryConfig;

    /**
     * if it is a appealOrder,it has this property
     * */
    private Integer appealOrderStatus;

    /**
     * 售后结单原因
     * */
    private String endReason;

    /**
     * 子订单对应的申诉单的订单id
     * */
    private String subOrderCorrespondingAppealOrderId;

    /**
     * 被申诉单(子订单)对应的主订单订单号
     * */
    private String mainOrderId;

    public String getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(String mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getEndReason() {
        return endReason;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    public Integer getAppealOrderStatus() {
        return appealOrderStatus;
    }

    public void setAppealOrderStatus(Integer appealOrderStatus) {
        this.appealOrderStatus = appealOrderStatus;
    }

    public String getWarningInfoUP() {
        return warningInfoUP;
    }

    public void setWarningInfoUP(String warningInfoUP) {
        this.warningInfoUP = warningInfoUP;
    }

    public String getWarningInfoDOWN() {
        return warningInfoDOWN;
    }

    public void setWarningInfoDOWN(String warningInfoDOWN) {
        this.warningInfoDOWN = warningInfoDOWN;
    }

    public PurchaserData getPurchaserData() {
        return purchaserData;
    }

    public void setPurchaserData(PurchaserData purchaserData) {
        this.purchaserData = purchaserData;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public List<DeliveryOrder> getDeliveryOrderList() {
        return deliveryOrderList;
    }

    public void setDeliveryOrderList(List<DeliveryOrder> deliveryOrderList) {
        this.deliveryOrderList = deliveryOrderList;
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

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public UserInfoEO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoEO userInfo) {
        this.userInfo = userInfo;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public List<OrderLog> getOrderLogList() {
        return orderLogList;
    }

    public void setOrderLogList(List<OrderLog> orderLogList) {
        this.orderLogList = orderLogList;
    }

    public List<DeliverySubOrder> getDeliverySubOrderList() {
        return deliverySubOrderList;
    }

    public void setDeliverySubOrderList(List<DeliverySubOrder> deliverySubOrderList) {
        this.deliverySubOrderList = deliverySubOrderList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public PurchaseConfig getPurchaseConfig() {
        return purchaseConfig;
    }

    public void setPurchaseConfig(PurchaseConfig purchaseConfig) {
        this.purchaseConfig = purchaseConfig;
    }

    public DeliveryConfig getDeliveryConfig() {
        return deliveryConfig;
    }

    public void setDeliveryConfig(DeliveryConfig deliveryConfig) {
        this.deliveryConfig = deliveryConfig;
    }
}
