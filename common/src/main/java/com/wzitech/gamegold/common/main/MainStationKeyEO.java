package com.wzitech.gamegold.common.main;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * Created by chengXY on 2017/8/29.
 */
public class MainStationKeyEO extends BaseEntity {
    /**
     * 获取令牌 Access
     */
    private String accessToken;

    /**
     * 获取密钥 Access
     */
    private String accessSecret;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }
}

