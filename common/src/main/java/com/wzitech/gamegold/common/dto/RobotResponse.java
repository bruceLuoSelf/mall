package com.wzitech.gamegold.common.dto;

/**
 * 机器人回复信息
 * Created by 335854 on 2016/4/5.
 */
public class RobotResponse {
    // 状态success or error
    private String status;
    //信息码
    private String code;
    //具体信息
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
