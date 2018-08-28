package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.DeliveryOrder;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by chengXY on 2017/8/22.
 */
public interface IAmoutHttp {
    //请求主站

    Boolean conToMain(DeliveryOrder deliveryOrder,BigDecimal totalFee,int orderSubfix) ;

    //请求7bao减少出货单收货商的采购款
    Boolean conToZBao(DeliveryOrder deliveryOrder, BigDecimal bigDecimal,int orderSubfix) throws IOException;


    //请求7bao减少出货单收货商的采购款
    Boolean mulitCreatTransferDeduction(String uid, String transferList, String loginAccount, BigDecimal amount, int yesOrNo, String orderId, BigDecimal totalAmount, BigDecimal availableAmount, BigDecimal freezeAmount) throws IOException;

    /**
     * 查询资金明细
     * @param orderId
     * @param loginAccount
     * @param amount
     * @return
     * @throws IOException
     */
    boolean queryFundDetail(String orderId, String loginAccount, BigDecimal amount) throws IOException;
}
