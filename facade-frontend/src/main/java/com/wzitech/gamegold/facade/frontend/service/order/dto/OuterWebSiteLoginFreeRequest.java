package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.gamegold.repository.entity.RepositoryInfo;

import java.util.List;

/**
 * Created by 339928 on 2018/5/30.
 */
public class OuterWebSiteLoginFreeRequest {

    private String firmSecret;

    private String appid;

    private String loginAccount;

    private String sign;

    private String orderId;

    private String subOrderId;

    private String token;

    private List<RepositoryInfo> repositories;

    public List<RepositoryInfo> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<RepositoryInfo> repositories) {
        this.repositories = repositories;
    }

    public String getFirmSecret() {
        return firmSecret;
    }

    public void setFirmSecret(String firmSecret) {
        this.firmSecret = firmSecret;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
