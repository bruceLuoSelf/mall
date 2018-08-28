package com.wzitech.gamegold.order.business;

import com.wzitech.gamegold.shorder.entity.PurchaserData;

import java.math.BigDecimal;

/**
 * Created by wangmin
 * Date:2017/9/7
 */
public interface IZbaoFundManager {
    /**
     * 处理充值成功通知 7bao
     *
     * @param amount 支付金额
     */
    public PurchaserData recharge7Bao(String account, BigDecimal amount, Integer type, String orderId);

    PurchaserData create7BaoPayorder(String account, BigDecimal amount, PurchaserData purchaserData, Integer type, String orderId);
}
