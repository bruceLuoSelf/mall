package com.wzitech.gamegold.common.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 发送短信返回信息
 * @author yemq
 */
public class SendMobileMsgResponse {
    private Integer resultNo;
    private String resultDescription;

    public SendMobileMsgResponse() {

    }

    public Integer getResultNo() {
        return resultNo;
    }

    @JsonProperty("ResultNo")
    public void setResultNo(Integer resultNo) {
        this.resultNo = resultNo;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    @JsonProperty("ResultDescription")
    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }
}
