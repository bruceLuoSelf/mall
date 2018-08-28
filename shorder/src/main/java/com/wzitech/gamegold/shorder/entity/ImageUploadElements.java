package com.wzitech.gamegold.shorder.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * Created by 339928 on 2018/4/19.
 */
public class ImageUploadElements extends BaseEntity {

    @JsonProperty("IsSucceed")
    private boolean IsSucceed;

    @JsonProperty("ErrorMessage")
    private String ErrorMessage;

    @JsonProperty("OriginalUrl")
    private String OriginalUrl;

    @JsonProperty("ThumbnailUrl")
    private String ThumbnailUrl;

    public String getOriginalUrl() {
        return OriginalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        OriginalUrl = originalUrl;
    }

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        ThumbnailUrl = thumbnailUrl;
    }

    public boolean isSucceed() {
        return IsSucceed;
    }

    public void setSucceed(boolean succeed) {
        IsSucceed = succeed;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
