package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;

/**
 * 验证优惠券返回
 */
public class ValidateDiscountCouponResponse extends AbstractServiceResponse {

    /**
     * 是否有效
     */
    private boolean isValid;

    /**
     * 价值
     */
    private Double price;

    public ValidateDiscountCouponResponse() {}

    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
