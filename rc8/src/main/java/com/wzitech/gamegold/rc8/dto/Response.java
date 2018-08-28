package com.wzitech.gamegold.rc8.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author yemq
 */
@XmlRootElement(name = "Result")
@XmlType(propOrder = {"status", "msg"})
public class Response {
    protected boolean status;
    protected String msg;

    public Response() {
    }

    @XmlElement(name = "Status")
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @XmlElement(name = "Msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
