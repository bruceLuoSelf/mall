package com.wzitech.gamegold.facade.service.receiving.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 339928 on 2018/6/20.
 */
@XmlRootElement(name="Result")
public class PreventAccountConflictResponse {

    private String Msg;

    private boolean Status;

    private boolean SellGameRoleBusy;

    private boolean DeliveryGameRoleBusy;


    @XmlElement(name = "DeliveryGameRoleBusy")
    public boolean getDeliveryGameRoleBusy() {
        return DeliveryGameRoleBusy;
    }

    public void setDeliveryGameRoleBusy(boolean deliveryGameRoleBusy) {
        DeliveryGameRoleBusy = deliveryGameRoleBusy;
    }

    @XmlElement(name = "Msg")
    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    @XmlElement(name = "Status")
    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        this.Status = status;
    }

    @XmlElement(name = "SellGameRoleBusy")
    public boolean getSellGameRoleBusy() {
        return SellGameRoleBusy;
    }

    public void setSellGameRoleBusy(boolean sellGameRoleBusy) {
        SellGameRoleBusy = sellGameRoleBusy;
    }
}
