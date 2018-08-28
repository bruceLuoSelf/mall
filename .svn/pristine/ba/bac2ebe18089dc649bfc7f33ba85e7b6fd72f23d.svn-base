package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;

/**
 * Created by 339928 on 2018/4/8.
 */
public class UploadPicsRequest extends AbstractServiceRequest {
    /**
     * 图片内容/文件流
     */
    @JsonProperty("FileBytes")
    private byte[] FileBytes;

    /**
     * 交易方式：担保：1，寄售：2，帐号：5
     */
    @JsonProperty("ServiceType")
    private Integer ServiceType;


    /**
     * 上传类型：商品图片0、身份证图片1、密保卡图片2等
     */
    @JsonProperty("UType")
    private String UType;


    /**
     * 0、首次上传商品图片可不传 1、密保卡ID 新上传密保卡时请生成一个GUID，
     * 如果为空，系统自动生成一个新的（如：Guid.NewGuid().ToString("N")）
     * 修改密保卡时请传入现有密保卡ID 2、修改商品时传发布单号 3、订单上传时传订单号
     */
    @JsonProperty("Id")
    private String Id;

    @JsonProperty("Token")
    private String Token;

    @JsonProperty("AppNo")
    private String AppNo;

    @JsonProperty("RequestSource")
    private String RequestSource;

    @JsonProperty("VersionNumber")
    private Integer VersionNumber;

    @JsonIgnore
    public byte[] getFileBytes() {
        return FileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        FileBytes = fileBytes;
    }

    @JsonIgnore
    public Integer getServiceType() {
        return ServiceType;
    }

    public void setServiceType(Integer serviceType) {
        ServiceType = serviceType;
    }

    @JsonIgnore
    public String getUType() {
        return UType;
    }

    public void setUType(String UType) {
        this.UType = UType;
    }

    @JsonIgnore
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @JsonIgnore
    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    @JsonIgnore
    public String getAppNo() {
        return AppNo;
    }

    public void setAppNo(String appNo) {
        AppNo = appNo;
    }

    @JsonIgnore
    public String getRequestSource() {
        return RequestSource;
    }

    public void setRequestSource(String requestSource) {
        RequestSource = requestSource;
    }

    @JsonIgnore
    public Integer getVersionNumber() {
        return VersionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        VersionNumber = versionNumber;
    }

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }

}
