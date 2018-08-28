package com.wzitech.gamegold.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chengXY on 2017/10/24.
 */
public class OrderOpData {
    /**
     * 订单客服ID
     */
    @JsonProperty("KefuLoginId")
    private String kefuLoginId;

    /**
     * 订单客服名称
     */
    @JsonProperty("KefuName")
    private String kefuName;

    /**
     * 订单客服QQ
     */
    @JsonProperty("KefuQQ")
    private String kefuQQ;

    public String getKefuLoginId() {
        return kefuLoginId;
    }

    public void setKefuLoginId(String kefuLoginId) {
        this.kefuLoginId = kefuLoginId;
    }

    public String getKefuName() {
        return kefuName;
    }

    public void setKefuName(String kefuName) {
        this.kefuName = kefuName;
    }

    public String getKefuQQ() {
        return kefuQQ;
    }

    public void setKefuQQ(String kefuQQ) {
        this.kefuQQ = kefuQQ;
    }
}
