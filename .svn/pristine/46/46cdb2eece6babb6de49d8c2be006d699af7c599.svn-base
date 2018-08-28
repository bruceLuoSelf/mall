package com.wzitech.gamegold.common.log.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.common.enums.UserType;

import java.util.Date;

/**
 * 日志基础信息
 * @author yemq
 */
public class BaseLogInfo extends BaseEntity {

    /**
     * 操作类型
     */
    protected LogType logType;

    /**
     * 用户类型
     */
    protected UserType userType;

    /**
     * 操作的用户ID
     */
    protected String userId;

    /**
     * 操作的用户账号
     */
    protected String userAccount;

    /**
     * 备注
     */
    protected String remark;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 操作的用户IP
     */
    protected String ip;

    /**
     * 操作线程ID
     */
    protected String threadId;

    public BaseLogInfo() {}

    public Integer getIntUserType() {
        if (userType != null)
            return userType.getCode();
        return null;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
