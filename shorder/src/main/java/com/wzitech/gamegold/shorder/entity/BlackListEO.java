package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * Created by 340032 on 2017/12/25.
 */
public class BlackListEO extends BaseEntity {
    /**
     * 5173用户
     */
    private String loginAccount;

    /**
     * 添加时间
     */
    private Date createTime;

    /**
     * 添加人
     */
    private String addPerson;

    /**
     * 状态
     */
    private Boolean enable;


    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAddPerson() {
        return addPerson;
    }

    public void setAddPerson(String addPerson) {
        this.addPerson = addPerson;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
