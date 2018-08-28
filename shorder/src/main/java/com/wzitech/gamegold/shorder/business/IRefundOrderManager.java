package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.RefundOrder;

import java.util.Map;

/**
 * 退款记录
 */
public interface IRefundOrderManager {
    /**
     * 申请退款
     *
     * @param payOrderId   支付单号
     * @param reason       申请理由
     * @param loginAccount 当前登录者5173账号
     */
    RefundOrder applyRefund(String payOrderId, String reason, String loginAccount);

    /**
     * 分页查找订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<RefundOrder> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean
            isAsc);

    /**
     * 修改
     *
     * @param refundOrder
     */
    String updateRefundOrder(RefundOrder refundOrder);

    /**
     * 修改(禁止调用)
     *
     * @param refundOrder
     */
     String updateRefundOrderPrivate(RefundOrder refundOrder);
}
