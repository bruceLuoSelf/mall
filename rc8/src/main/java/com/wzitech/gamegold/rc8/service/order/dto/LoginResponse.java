package com.wzitech.gamegold.rc8.service.order.dto;

import com.wzitech.gamegold.rc8.dto.Response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 登录信息
 * Created by 汪俊杰 on 2017/5/11.
 */
@XmlRootElement(name = "Result")
@XmlType(propOrder = {"hxAppAccount", "hxAppPwd","yxAccount","yxPwd","qq","qqPwd"})
public class LoginResponse extends Response {
    /**
     * 客服app环信帐号
     */
    private String hxAppAccount;

    /**
     * 客服app环信密码
     */
    private String hxAppPwd;

    private String yxAccount;

    private String yxPwd;

    /**
     * QQ
     */
    private String qq;

    /**
     * QQ密码
     */
    private String qqPwd;
    @XmlElement(name = "qq")
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
    @XmlElement(name = "qqPwd")
    public String getQqPwd() {
        return qqPwd;
    }

    public void setQqPwd(String qqPwd) {
        this.qqPwd = qqPwd;
    }

    @XmlElement(name = "HxAppAccount")
    public String getHxAppAccount() {
        return hxAppAccount;
    }

    public void setHxAppAccount(String hxAppAccount) {
        this.hxAppAccount = hxAppAccount;
    }

    @XmlElement(name = "HxAppPwd")
    public String getHxAppPwd() {
        return hxAppPwd;
    }

    public void setHxAppPwd(String hxAppPwd) {
        this.hxAppPwd = hxAppPwd;
    }

    @XmlElement(name = "YxAccount")
    public String getYxAccount() {
        return yxAccount;
    }

    public void setYxAccount(String yxAccount) {
        this.yxAccount = yxAccount;
    }

    @XmlElement(name = "YxPwd")
    public String getYxPwd() {
        return yxPwd;
    }

    public void setYxPwd(String yxPwd) {
        this.yxPwd = yxPwd;
    }
}
