package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.RefundStatus;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.shorder.business.IFundPrivateManager;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.business.IRefundOrderManager;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.dao.IRefundOrderDao;
import com.wzitech.gamegold.shorder.dao.IRefundOrderIdRedisDao;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.entity.RefundOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 退款记录
 */
@Component
public class RefundOrderManagerImpl implements IRefundOrderManager {
    @Autowired
    private IRefundOrderDao refundOrderDao;

    @Autowired
    private IPayOrderManager payOrderManager;

    @Autowired
    private IRefundOrderIdRedisDao refundOrderIdRedisDao;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    private IFundManager fundManager;

    @Autowired
    private IFundPrivateManager fundPrivateManager;

    /**
     * 申请退款
     *
     * @param payOrderId   支付单号
     * @param reason       申请理由
     * @param loginAccount 当前登录者5173账号
     */
    @Transactional
    public RefundOrder applyRefund(String payOrderId, String reason, String loginAccount) {
        if (StringUtils.isEmpty(payOrderId)) {
            throw new SystemException(ResponseCodes.EmptyOrderId.getCode(),
                    ResponseCodes.EmptyOrderId.getMessage());
        }

        //根据充值单号查询充值单信息
        PayOrder payOrder = payOrderManager.queryByOrderId(payOrderId, true);
        if (payOrder == null || !payOrder.getAccount().equals(loginAccount) || payOrder.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            // 不是当前登录者的支付单，直接退出不处理
            return null;
        }

        // 判断是否有交易中的订单，如果有，则不能退款，如果没有，才可以退款
        // 1：等待交易;2：排队中;3：交易中;7：需人工介入,8:申请部分完单，9：待发货；10：已发货；11：待确认收货 参考DeliveryOrderStatus.java
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("statusList", "1,2,3,7,8,9,10,11");
        map.put("buyerAccount", loginAccount);
        map.put("isLocked", true);
        int count = deliveryOrderDao.countByMap(map);
        if (count > 0) {
            throw new SystemException(ResponseCodes.CanNotRefund.getCode(),
                    ResponseCodes.CanNotRefund.getMessage());
        }


        // 查询该支付单是否有申请退款的请求未处理
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("payOrderId", payOrderId);
        paramMap.put("status", RefundOrder.UnAudited);
        paramMap.put("lockMode", true);
        count = refundOrderDao.countByMap(paramMap);
        if (count > 0) {
            throw new SystemException(ResponseCodes.ExistsRefundOrderNotPorcess.getCode(),
                    ResponseCodes.ExistsRefundOrderNotPorcess.getMessage());
        }

        // 创建申请退款记录
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setOrderId(refundOrderIdRedisDao.getOrderId());
        refundOrder.setPayOrderId(payOrderId);
        refundOrder.setRefundAmount(payOrder.getBalance());
        refundOrder.setReason(reason);
        refundOrder.setStatus(RefundOrder.UnAudited);
        refundOrder.setCreateTime(new Date());
        refundOrderDao.insert(refundOrder);

        // 冻结申请退款的资金
        fundManager.freezeRefundFund(refundOrder);

        return refundOrder;
    }

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
    @Override
    public GenericPage<RefundOrder> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean
            isAsc) {
        if (null == map) {
            map = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        return refundOrderDao.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    /**
     * 修改
     *
     * @param refundOrder
     */
    @Override
    @Transactional
    public String updateRefundOrder(RefundOrder refundOrder) {
        //修改支付单状态，并写入资金明细日志

        //修改退款订单状态
        RefundOrder dbRefundOrder = refundOrderDao.selectByIdForUpdate(refundOrder.getId());
        if (dbRefundOrder != null) {
            //判断当前状态是否为未审核状态，不然不给于审核
            if (dbRefundOrder.getStatus() != RefundStatus.UnAudited.getCode()) {
                return "当前状态不能退款";
            }

            dbRefundOrder.setStatus(refundOrder.getStatus());
            dbRefundOrder.setAuditor(refundOrder.getAuditor());
            dbRefundOrder.setAuditTime(refundOrder.getAuditTime());
            dbRefundOrder.setFinishTime(new Date());
            refundOrderDao.update(dbRefundOrder);

            if (refundOrder.getStatus() == RefundOrder.Refund) {

                // 退款给收货商
                fundManager.refund(dbRefundOrder);

            } else if (refundOrder.getStatus() == RefundOrder.UnPassAudited) {

                // 解冻退款资金
                fundManager.releaseFreezeRefundFund(dbRefundOrder);

            }
        }
        return "";
    }

    /**
     * 修改(禁止调用)
     *
     * @param refundOrder
     */
    @Override
    @Transactional
    public String updateRefundOrderPrivate(RefundOrder refundOrder) {
        //修改支付单状态，并写入资金明细日志

        //修改退款订单状态
        RefundOrder dbRefundOrder = refundOrderDao.selectByIdForUpdate(refundOrder.getId());
        if (dbRefundOrder != null) {
            //判断当前状态是否为未审核状态，不然不给于审核
            if (dbRefundOrder.getStatus() != RefundStatus.UnAudited.getCode()) {
                return "当前状态不能退款";
            }

            dbRefundOrder.setStatus(refundOrder.getStatus());
            dbRefundOrder.setAuditor(refundOrder.getAuditor());
            dbRefundOrder.setAuditTime(refundOrder.getAuditTime());
            dbRefundOrder.setFinishTime(new Date());
            refundOrderDao.update(dbRefundOrder);

            if (refundOrder.getStatus() == RefundOrder.Refund) {

                // 退款给收货商
                fundPrivateManager.refund(dbRefundOrder);

            } else if (refundOrder.getStatus() == RefundOrder.UnPassAudited) {

                // 解冻退款资金
                fundManager.releaseFreezeRefundFund(dbRefundOrder);

            }
        }
        return "";
    }
}