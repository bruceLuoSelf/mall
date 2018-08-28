package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

import java.math.BigDecimal;

/**
 * @author 340096
 * @date 2018/1/24.
 */
public class DealFallOrderResponse extends AbstractServiceResponse {
    /**
     * 所要补单的用户id
     * */
    private String userId;

    /**
     * 所要补的金额
     * */
    private BigDecimal realMoney;

    /**
     * 外部订单号（主站订单号 N开头）
     * */
    private String outOrderId;

    /**
     * 商城订单号（YX开头）
     * */
    private String mallOrderId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(BigDecimal realMoney) {
        this.realMoney = realMoney;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public String getMallOrderId() {
        return mallOrderId;
    }

    public void setMallOrderId(String mallOrderId) {
        this.mallOrderId = mallOrderId;
    }
}
