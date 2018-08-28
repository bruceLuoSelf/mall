package com.wzitech.gamegold.facade.frontend.service.repository.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by 339928 on 2017/7/5.
 */
public class CheckRepositoryManageResponse extends AbstractServiceResponse {
    private String firmsAccountSecret;

    private String cookie;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String tokken) {
        this.token = tokken;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getFirmsAccountSecret() {
        return firmsAccountSecret;
    }

    public void setFirmsAccountSecret(String firmsAccountSecret) {
        this.firmsAccountSecret = firmsAccountSecret;
    }
}
