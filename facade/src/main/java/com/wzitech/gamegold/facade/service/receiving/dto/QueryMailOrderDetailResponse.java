package com.wzitech.gamegold.facade.service.receiving.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 339928 on 2018/1/2.
 */

@XmlRootElement(name = "Result")
public class QueryMailOrderDetailResponse {
    /**
     * 返回信息
     */
    private QueryOrderDetailMailDTO queryOrderDetailMailDTO;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 状态
     */
    private  boolean status;

    /**
     * 订单状态
     */
    private Integer orderStatus;


    @XmlElement(name = "Order")
    public QueryOrderDetailMailDTO getQueryOrderDetailMailDTO() {
        return queryOrderDetailMailDTO;
    }

    public void setQueryOrderDetailMailDTO(QueryOrderDetailMailDTO queryOrderDetailMailDTO) {
        this.queryOrderDetailMailDTO = queryOrderDetailMailDTO;
    }

    @XmlElement(name = "Msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    @XmlElement(name = "Status")
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    @XmlElement(name = "OrderStatus")
    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}
