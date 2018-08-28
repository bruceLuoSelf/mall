package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * Created by 339928 on 2018/4/4.
 */
public class UploadPicsResponse extends AbstractServiceResponse {

    private String status;

    private String message;

    private String url;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
