package com.wzitech.gamegold.order.dto;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单数据
 * @author yemq
 */
public class SimpleOrderDTO extends BaseEntity {
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 发布单名称
     */
    private String title;
    /**
     * 游戏名
     */
    private String gameName;
    /**
     * 区
     */
    private String region;
    /**
     * 服
     */
    private String server;
    /**
     * 阵营
     */
    private String gameRace;
    /**
     * 买家收货角色名
     */
    private String receiver;
    /**
     * 购买游戏币数量
     */
    private Long goldCount;
    /**
     * 游戏币单位
     */
    private String moneyName;
    /**
     * 购买单价
     */
    private BigDecimal unitPrice;
    /**
     * 总金额
     */
    private BigDecimal totalPrice;
    /**
     * 买家手机
     */
    private String mobileNumber;
    /**
     * 买家QQ
     */
    private String qq;
    /**
     * 买家账号
     */
    private String buyerAccount;
    /**
     * 卖家账号
     */
    private String sellerAccount;
    /**
     * 客服账号
     */
    private String serviceAccount;
    /**
     * 客服姓名
     */
    private String serviceName;
    /**
     * 订单状态
     */
    private Integer orderState;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 付款时间
     */
    private Date payTime;
    /**
     * 完成时间
     */
    private Date endTime;
    /**
     * 退款原因
     */
    private Integer refundReason;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getGameRace() {
        return gameRace;
    }

    public void setGameRace(String gameRace) {
        this.gameRace = gameRace;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Long getGoldCount() {
        return goldCount;
    }

    public void setGoldCount(Long goldCount) {
        this.goldCount = goldCount;
    }

    public String getMoneyName() {
        return moneyName;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    public String getServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(String serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(Integer refundReason) {
        this.refundReason = refundReason;
    }
}
