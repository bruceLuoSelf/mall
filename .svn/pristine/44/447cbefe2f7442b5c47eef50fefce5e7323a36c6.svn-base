package com.wzitech.gamegold.order.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.Date;

/**
 * 短信发送规则
 *
 * @author yemq
 */
public class MessageRule extends BaseEntity {
    /**
     * 规则名称
     */
    private String name;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 触发的订单状态
     */
    private Integer orderStatus;

    /**
     * 延时(秒)
     */
    private Integer delay;

    /**
     * 周期
     */
    private Short period;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 操作员
     */
    private UserInfoEO operator;

    /**
     * 操作时间
     */
    private Date lastUpdateTime;

    /**
     * 是否启用
     */
    private Boolean enabled;

    public MessageRule() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Short getPeriod() {
        return period;
    }

    public void setPeriod(Short period) {
        this.period = period;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserInfoEO getOperator() {
        return operator;
    }

    public void setOperator(UserInfoEO operator) {
        this.operator = operator;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
