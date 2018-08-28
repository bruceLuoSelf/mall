package com.wzitech.gamegold.goods.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.util.Date;

/**
 * Created by 339928 on 2017/7/5.
* 厂商对应的游戏卖家账号表
 */
public class FirmsAccount extends BaseEntity {
    //厂商名称
    private String firmsName;

    //厂商秘钥
    private String firmsSecret;

    //创建时间
   private Date createTime;

    //更新时间
    private Date updateTime;

    //对应卖家账号
    private String loginAccount;

    //用户对应UID
    private String uid;

    public FirmsAccount() {
    }
    //启用/禁用
    private Boolean enabled;

    /**
     * 用户密钥
     */
    private String userSecretKey;

    public String getUserSecretKey() {
        return userSecretKey;
    }

    public void setUserSecretKey(String userSecretKey) {
        this.userSecretKey = userSecretKey;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirmsName() {
        return firmsName;
    }

    public void setFirmsName(String firmsName) {
        this.firmsName = firmsName;
    }

    public String getFirmsSecret() {
        return firmsSecret;
    }

    public void setFirmsSecret(String firmsSecret) {
        this.firmsSecret = firmsSecret;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getUid() {return uid;}

    public void setUid(String uid) {this.uid = uid;}
}
