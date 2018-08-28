package com.wzitech.gamegold.jsrobot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author yemq
 */
// 解决Spring Data Redis使用Jason1.X，与Jason2.X不兼容的问题
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class Response {
    protected boolean status;
    protected String msg;

    public Response() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
