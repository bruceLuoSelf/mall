package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * Created by jhlcitadmin on 2017/3/3.
 * 记录对用户表进行操作的日志
 */

public class UserModifiActionLog extends BaseEntity{

    private Long id;
    //当前登录的人的id
    private Long UserId;

    private Date updateTime;

    private int userType;

    private String log;

    private Long modifiUserId;

    private String userAccount;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Long getModifiUserId() {
        return modifiUserId;
    }

    public void setModifiUserId(Long modifiUserId) {
        this.modifiUserId = modifiUserId;
    }
}
