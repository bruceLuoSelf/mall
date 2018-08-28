package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * 出货订单日志
 */
public class OrderLog extends BaseEntity {
    /**
     * 日志类型：普通，给用户看的
     */
    public static final int TYPE_NORMAL = 1;
    /**
     * 日志类型：内部日志，不给用户看的
     */
    public static final int TYPE_INNER = 2;
    /**
     * 关联的出货订单orderId
     */
    private String orderId;
    /**
     * 日志类型
     * 1：普通日志，给用户看的，默认值
     * 2：内部日志，不给用户看的
     */
    private Integer type = TYPE_NORMAL;
    /**
     * 日志内容
     */
    private String log;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 操作者
     */
    private String operator;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 子订单id
     * @return
     */
    private Long subId;

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public OrderLog() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
