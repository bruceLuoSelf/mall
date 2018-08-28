package com.wzitech.gamegold.common.main;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chengXY on 2017/8/29.
 */
public class SRRequsestTokenResponse {
    @JsonProperty("BizResult")
    public SRRequestTokenEO bizResult;

    @JsonProperty("Status")
    public  String status;

    @JsonProperty("FeedBackResult")
    public  String feedBackResult;

    @JsonProperty("Key")
    public  String key;

    public SRRequestTokenEO getBizResult() {
        return bizResult;
    }

    public void setBizResult(SRRequestTokenEO bizResult) {
        this.bizResult = bizResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedBackResult() {
        return feedBackResult;
    }

    public void setFeedBackResult(String feedBackResult) {
        this.feedBackResult = feedBackResult;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
