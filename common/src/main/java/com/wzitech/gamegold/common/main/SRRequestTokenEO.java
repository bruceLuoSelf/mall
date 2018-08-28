package com.wzitech.gamegold.common.main;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chengXY on 2017/8/29.
 */
public class SRRequestTokenEO {
    @JsonProperty("Appid")
    public String appid;
    @JsonProperty("TimeStamp")
    public String timeStamp;
    //1.	获取授权
    @JsonProperty("RequestToken")
    public String requestToken;
    @JsonProperty("RequestSecret")
    public String requestSecret;

    //2.	获取令牌 Access
    @JsonProperty("AccessToken")
    public String accessToken;

    //  密钥
    @JsonProperty("AccessSecret")
    public String accessSecret;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public String getRequestSecret() {
        return requestSecret;
    }

    public void setRequestSecret(String requestSecret) {
        this.requestSecret = requestSecret;
    }

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
