package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.dao.IPayOrderDao;
import com.wzitech.gamegold.shorder.dao.IPayOrderRedisDao;
import com.wzitech.gamegold.shorder.dao.ISellerDTOdao;
import com.wzitech.gamegold.shorder.dto.SellerDTO;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.enums.OrderPrefix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 收货商支付单管理
 *
 * @author yemq
 */
@Component
public class PayOrderManagerImpl extends AbstractBusinessObject implements IPayOrderManager {
    @Autowired
    private IPayOrderDao payOrderDao;
    @Autowired
    private IPayOrderRedisDao payOrderRedisDao;
    @Autowired
    private ISellerDTOdao sellerDTOdao;

    /**
     * 分页查询订单
     *
     * @param map
     * @param pageSize
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<PayOrder> queryPayOrders(Map<String, Object> map, int start, int pageSize,
                                                String orderBy, boolean isAsc) {
        if (map == null) {
            map = new HashMap<String, Object>();
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException("分页数pageSize需大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }
        return payOrderDao.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    /**
     * 创建支付单
     *
     * @param amount 支付金额
     * @return 返回生成的订单号
     */
    @Override
    @Transactional
    public String createOrder(BigDecimal amount) {
        if (amount == null || amount.doubleValue() <= 0)
            throw new SystemException(ResponseCodes.PayAmountMustGtZero.getCode(),
                    ResponseCodes.PayAmountMustGtZero.getMessage());
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal zero = BigDecimal.ZERO.setScale(2);

        String orderId = payOrderRedisDao.getOrderId(); // 生成支付单号
        PayOrder order = new PayOrder();
        order.setOrderId(orderId);
        order.setUid(CurrentUserContext.getUidStr());
        order.setAccount(CurrentUserContext.getUserLoginAccount());
        order.setAmount(amount);
        order.setUsedAmount(zero);
        order.setBalance(amount);
        order.setStatus(PayOrder.WAIT_PAYMENT);
        order.setCreateTime(new Date());
        order.setLastUpdateTime(new Date());
        payOrderDao.insert(order);

        return orderId;
    }

    @Override
    public String create7BaoOrder(BigDecimal amount, String account, String zbaoRelatedOrderId) {
        SellerDTO byAccount = sellerDTOdao.findByAccount(account);
        if (amount == null || amount.doubleValue() <= 0)
            throw new SystemException(ResponseCodes.PayAmountMustGtZero.getCode(),
                    ResponseCodes.PayAmountMustGtZero.getMessage());
        amount = amount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal zero = BigDecimal.ZERO.setScale(2);

        String orderId = payOrderRedisDao.getZBaoOrderId(); // 生成支付单号
        PayOrder order = new PayOrder();
        order.setOrderId(orderId);
        order.setUid(byAccount.getUid());
        order.setAccount(account);
        order.setAmount(amount);
        order.setUsedAmount(zero);
        order.setBalance(amount);
        order.setStatus(PayOrder.PAID);
        order.setCreateTime(new Date());
        order.setPayTime(new Date());
        order.setLastUpdateTime(new Date());
        order.setZbaoRelatedOrderId(zbaoRelatedOrderId);
        payOrderDao.insert(order);

        return orderId;
    }

    /**
     * 根据订单号查询
     *
     * @param orderId
     * @param lockMode
     * @return
     */
    @Override
    @Transactional
    public PayOrder queryByOrderId(String orderId, Boolean lockMode) {
        return payOrderDao.selectByOrderId(orderId, lockMode);
    }

    /**
     * 查询可用的支付单(已经支付且有余额为可用的支付单)
     *
     * @param buyerAccount
     * @param isLocked
     * @return
     */
    @Override
    @Transactional
    public List<PayOrder> queryAvailablePayOrders(String buyerAccount, Boolean... isLocked) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("account", buyerAccount);
        paramMap.put("status", PayOrder.PAID);
        paramMap.put("orderIdFund", OrderPrefix.OLD_PAY_ID.getName());
        paramMap.put("minBalance", 0);
        if (isLocked != null) {
            paramMap.put("isLocked", isLocked[0]);
        }
        return payOrderDao.selectByMap(paramMap, "pay_time", true);
    }

    /**
     * 查询新的可用的支付单
     */
    @Override
    @Transactional
    public List<PayOrder> queryAvailablePayOrdersZBao(String buyerAccount, Boolean... isLocked) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("account", buyerAccount);
        paramMap.put("status", PayOrder.PAID);
        paramMap.put("orderIdFund", OrderPrefix.NEW_PAY_ID.getName());
        paramMap.put("minBalance", 0);
        if (isLocked != null) {
            paramMap.put("isLocked", isLocked[0]);
        }
        return payOrderDao.selectByMap(paramMap, "pay_time", true);
    }


    /**
     * 自动退款超过三个月的充值订单
     *
     * @param days
     * @return
     */
    @Override
    @Transactional
    public List<PayOrder> queryTimeoutPayOrders(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - days);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("status", PayOrder.PAID);
        paramMap.put("minBalance", 0);
        paramMap.put("endPayTime", c.getTime());
        paramMap.put("orderIdFund", OrderPrefix.OLD_PAY_ID.getName());
        paramMap.put("isLocked", true);
        return payOrderDao.selectByMap(paramMap, "pay_time", true);
    }

    /**
     * 自动退款超过三个月的充值订单
     *
     * @param list
     * @return
     */
    @Override
    @Transactional
    public void batchUpdate(List<PayOrder> list) {
        if (list != null && list.size() != 0) {
            payOrderDao.batchUpdate(list);
        }
    }

    /**
     * 更加id更新操作
     *
     * @param list
     * @return
     */
    @Override
    @Transactional
    public void batchUpdateByIds(List<PayOrder> list) {
        if (list != null && list.size() != 0) {
            payOrderDao.batchUpdateByIds(list);
        }
    }

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    @Override
    @Transactional
    public void batchInsert(List<PayOrder> list) {
        if (list != null && list.size() != 0) {
            for (PayOrder l : list) {
                payOrderDao.insert(l);
            }
        }
    }


    @Override
    @Transactional
    public void deletePayOrderCaseWithdraw(String loginAccount, BigDecimal changeAmount) {
        //查询出当前账号下 所有可用金额大于0的充值单 且是已支付的
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("account", loginAccount);
        queryMap.put("minBalance", BigDecimal.ZERO);
        queryMap.put("status", PayOrder.PAID);
        queryMap.put("orderIdFund", "SHRE");
        List<PayOrder> list = payOrderDao.selectByMap(queryMap, "balance", true);
        if (null == list) {
            throw new SystemException(ResponseCodes.FailToDeletePayOrder.getCode(), ResponseCodes.FailToDeletePayOrder.getMessage());
        }
        //初始的剩余需要扣的金额为变动金额（负数）
        BigDecimal remain = changeAmount;
        logger.info("因提现成功扣除采购单开始,对应账号:" + loginAccount);
        for (PayOrder payOrder : list) {
            //以剩余金额加上剩余需要变动金额为最新的所剩需要变动金额
            remain = payOrder.getBalance().add(remain).setScale(2, BigDecimal.ROUND_DOWN);
            if (remain.compareTo(BigDecimal.ZERO) < 0) {
                //当可用金额不足以抵扣提现金额时,可用资金为0 已使用金额等于总金额
                payOrder.setUsedAmount(payOrder.getAmount());
                payOrder.setBalance(BigDecimal.ZERO);
                payOrder.setLastUpdateTime(new Date());
                payOrderDao.update(payOrder);
            }
            if (remain.compareTo(BigDecimal.ZERO) >= 0) {
                //当可用金额足以抵扣提现金额甚至有盈余时  已用资金等于旧资金加上上次仍未被扣除的余额
                //可用资金为旧可用资金扣除仍未被扣除的余额
                payOrder.setUsedAmount(payOrder.getBalance().subtract(remain).add(payOrder.getUsedAmount()));
                payOrder.setBalance(remain);
                payOrder.setLastUpdateTime(new Date());
                payOrderDao.update(payOrder);
                return;
            }
        }
        //整个循环完 还没有扣完就要抛出异常了
        if (remain.compareTo(BigDecimal.ZERO) < 0) {
            throw new SystemException(ResponseCodes.FailToDeletePayOrder.getCode(), ResponseCodes.FailToDeletePayOrder.getMessage());
        }
    }
}
