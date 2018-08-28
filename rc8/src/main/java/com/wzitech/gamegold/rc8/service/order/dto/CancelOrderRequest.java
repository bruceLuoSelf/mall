package com.wzitech.gamegold.rc8.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 取消订单请求
 *
 * @author yemq
 */
public class CancelOrderRequest extends AbstractServiceRequest {
    /**
     * 责任方-买家
     */
    public static final int DUTY_TYPE_BUYER = 1;
    /**
     * 责任方-卖家
     */
    public static final int DUTY_TYPE_SELLER = 2;
    /**
     * 责任方-第三方责任
     */
    public static final int DUTY_TYPE_THIRD = 3;

    private String name;
    private String pwd;
    /**
     * 责任方分类
     * <li>1.买家责任</li>
     * <li>2.卖家责任</li>
     * <li>3.第三方责任</li>
     */
    private Integer dutytype;
    /**
     * 撤单原因
     */
    private String reasontext;
    /**
     * Type Value
     */
    private String reasondetail;
    /**
     * 备注
     */
    private String reasonremark;
    private String sign;

    public CancelOrderRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getDutytype() {
        return dutytype;
    }

    public void setDutytype(Integer dutytype) {
        this.dutytype = dutytype;
    }

    public String getReasontext() {
        return reasontext;
    }

    public void setReasontext(String reasontext) {
        this.reasontext = reasontext;
    }

    public String getReasondetail() {
        return reasondetail;
    }

    public void setReasondetail(String reasondetail) {
        this.reasondetail = reasondetail;
    }

    public String getReasonremark() {
        return reasonremark;
    }

    public void setReasonremark(String reasonremark) {
        this.reasonremark = reasonremark;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
