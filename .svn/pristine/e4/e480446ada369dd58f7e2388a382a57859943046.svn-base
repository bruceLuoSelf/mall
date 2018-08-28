package com.wzitech.gamegold.common.userinfo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 5173用户真实姓名和身份证号码
 */
public class UserInfo extends BaseEntity {
    /**
     * 用户真实姓名
     */
    @JsonProperty("UserRealName")
    private String realName;
    /**
     * 用户身份证号码
     */
    @JsonProperty("UserIdCard")
    private String idCard;

    public UserInfo() {}

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
