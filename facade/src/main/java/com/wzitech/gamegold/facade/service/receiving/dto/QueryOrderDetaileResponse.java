package com.wzitech.gamegold.facade.service.receiving.dto;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**ZW_C_JB_00004 sunyang
 * Created by sunyang on 2017/5/5.
 */
@XmlRootElement(name = "Result")
public class QueryOrderDetaileResponse {

    /**
     * 返回信息
     */
    private QueryOrderDetaileDTO queryOrderDetaileDTO;


    /**
     * 状态信息
     */
    private String msg;

    /**
     * 状态
     */
    private boolean status;

    /**订单状态
     */
    private Integer orderStatus;

    @XmlElement(name = "OrderStatus")
    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    @XmlElement(name = "Order")
    public QueryOrderDetaileDTO getQueryOrderDetaileDTO() {
        return queryOrderDetaileDTO;
    }

    public void setQueryOrderDetaileDTO(QueryOrderDetaileDTO queryOrderDetaileDTO) {
        this.queryOrderDetaileDTO = queryOrderDetaileDTO;
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

}
