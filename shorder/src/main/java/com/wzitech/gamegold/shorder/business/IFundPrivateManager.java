package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.RefundOrder;

/**
 * Created by 汪俊杰 on 2018/3/8.
 */
public interface IFundPrivateManager {
    /**
     * 退款给收货方
     *
     * @param refundOrder
     */
    void refund(RefundOrder refundOrder);
}
