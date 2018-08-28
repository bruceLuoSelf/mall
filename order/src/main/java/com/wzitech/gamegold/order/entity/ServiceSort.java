package com.wzitech.gamegold.order.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.Date;

/**
 * 客服分单排序
 * @author yemq
 */
public class ServiceSort extends BaseEntity {
    /**
     * 客服
     */
    private UserInfoEO service;
    /**
     * 等待发货数量
     */
    private Long waitDelivery;
    /**
     * 最后分配时间
     */
    private Date lastUpdateTime;

    public ServiceSort(){}

    public UserInfoEO getService() {
        return service;
    }

    public void setService(UserInfoEO service) {
        this.service = service;
    }

    public Long getWaitDelivery() {
        return waitDelivery;
    }

    public void setWaitDelivery(Long waitDelivery) {
        this.waitDelivery = waitDelivery;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
