package com.wzitech.gamegold.common.entity;

import com.wzitech.chaos.framework.server.common.ResponseStatus;

/**
 * Created by 汪俊杰 on 2018/4/16.
 */
public class BaseResponse {
    private ResponseStatus responseStatus;

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
