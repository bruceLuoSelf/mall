/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		IOrderInfoManager
 * 包	名：		com.wzitech.gamegold.order.business
 * 项目名称：	gamegold-order
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-17
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-17 下午3:32:36
 ************************************************************************************/
package com.wzitech.gamegold.order.business;

import com.wzitech.gamegold.common.paymgmt.dto.VaQueryDetailResponse;
import com.wzitech.gamegold.order.entity.OrderInfoEO;


/**
 * 自动打款管理接口
 *
 * @author ztjie
 */
public interface IAutoPayManager {

    /**
     * <p>打款</p>
     *
     * @param orderInfo
     * @return
     * @author Think
     * @date 2014-3-3 下午8:34:51
     * @see
     */
    boolean pay(OrderInfoEO orderInfo);

    /**
     * <p>退款</p>
     *
     * @param orderInfo
     * @return
     * @author Think
     * @date 2014-3-3 下午8:53:39
     * @see
     */
    boolean refund(OrderInfoEO orderInfo);

    /**
     * <p>查询订单是否已经支付</p>
     *
     * @param orderInfo
     * @return
     * @author Think
     * @date 2014-4-1 下午4:24:15
     * @see
     */
    Boolean queryPaymentDetail(OrderInfoEO orderInfo);

    /**
     * 查询订单是否已经支付
     *
     * @param orderId
     * @param uid
     * @return
     */
    Boolean queryPaymentDetail(String orderId, String uid);

    /**
     * 根据业务单号查询主站资金明细
     *
     * @param orderId
     * @return
     */
    VaQueryDetailResponse queryWithdrawalsDetailByBillId(String orderId);

    /**
     * 根据主站单号单号查询主站资金明细
     *
     * @param orderId
     * @return
     */
    VaQueryDetailResponse queryWithdrawalsDetailByOrderId(String orderId);

//	/**
//	 * 增加出货流程中出货商的资金
//	 * @param orderid
//	 * @return
//	 */
//	public Boolean add7BaoCHPayorder(String orderid);
}
