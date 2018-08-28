package com.wzitech.gamegold.repository.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提供用来给7bao记录资金明细
 * Created by 340082 on 2017/9/21.
 */
public class JBPayOrderTo7BaoEO {
    //老订单号
    private String oldOrderId;
    //新订单
    private String newOrderId;
    //金额
    private BigDecimal balance;
    //时间
    private Date updateTime;

    public String getOldOrderId() {
        return oldOrderId;
    }

    public void setOldOrderId(String oldOrderId) {
        this.oldOrderId = oldOrderId;
    }

    public String getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(String newOrderId) {
        this.newOrderId = newOrderId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
