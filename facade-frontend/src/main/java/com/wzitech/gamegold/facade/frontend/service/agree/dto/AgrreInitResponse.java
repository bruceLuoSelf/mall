package com.wzitech.gamegold.facade.frontend.service.agree.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.repository.entity.SellerInfo;

/**
 * Created by 340032 on 2017/8/19.
 */
public class AgrreInitResponse extends AbstractServiceResponse {

    /**
     * 消息
     */
    private String msg;


    private SellerInfo isAgree;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public SellerInfo getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(SellerInfo isAgree) {
        this.isAgree = isAgree;
    }


}
