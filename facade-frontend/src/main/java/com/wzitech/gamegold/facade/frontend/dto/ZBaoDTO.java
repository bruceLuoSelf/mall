package com.wzitech.gamegold.facade.frontend.dto;

import org.springframework.stereotype.Component;

/**
 * Created by chengXY on 2017/8/21.
 */
@Component
public class ZBaoDTO {
    /**
     * 登录账号
     * */
    private String loginAccount;


    /**
     * 电话号码
     */
    private String phoneNumber;

    /**
     * qq
     * */
    private String qq;


    /**
     * 姓名
     * */
    private  String name;

    /**
     * 金币用户绑定
     */
    private Boolean isUserBind;


    /**
     * 7bao账户Id
     * @return
     */
    private String uid;
    /**
     * sign
     * */
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUserBind() {
        return isUserBind;
    }

    public void setUserBind(Boolean userBind) {
        isUserBind = userBind;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}

