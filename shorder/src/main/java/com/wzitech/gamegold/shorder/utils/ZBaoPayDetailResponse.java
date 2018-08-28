package com.wzitech.gamegold.shorder.utils;

import com.wzitech.chaos.framework.server.common.ResponseStatus;

/**
 * Created by 汪俊杰 on 2018/7/17.
 */
public class ZBaoPayDetailResponse {
    private ResponseStatus responseStatus;

    private ZbaoPayDetail payDetail;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public ZbaoPayDetail getPayDetail() {
        return payDetail;
    }

    public void setPayDetail(ZbaoPayDetail payDetail) {
        this.payDetail = payDetail;
    }
}
