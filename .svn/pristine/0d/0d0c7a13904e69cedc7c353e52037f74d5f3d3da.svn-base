package com.wzitech.gamegold.common.utils;

/**
 * Created by 汪俊杰 on 2018/7/8.
 */
public class CommonConstants {
    public static final String BUSSINESS_EXCEPTION_PREFIX = "B";
    public static final String SYSTEM_EXCEPTION_PREFIX = "S";
    public static final String HTTP_PREFIX = "http://";
    public static final String HTTP_INTERVAL = "/";
    public static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
    public static final String JSESSIONID = "JSESSIONID";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String SERVICE_REQUEST_HEADER_REAL_IP = "X-real-Ip";
    public static final String SERVICE_REQUEST_HEADER_APPID = "appId";
    public static final String SERVICE_REQUEST_HEADER_UID = "uid";
    public static final String SERVICE_REQUEST_HEADER_AUTHKEY = "authkey";

    public CommonConstants() {
    }

    public static final String getSystemErrorCode(String subSystemCode, String moduleCode, String errorCode) {
        return "S" + subSystemCode + moduleCode + errorCode;
    }

    public static final String getBusinessErrorCode(String subSystemCode, String moduleCode, String errorCode) {
        return "B" + subSystemCode + moduleCode + errorCode;
    }
}
