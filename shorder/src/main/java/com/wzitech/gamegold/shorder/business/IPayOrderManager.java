package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.PayOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 收货商支付单管理
 */
public interface IPayOrderManager {
    /**
     * 分页查询订单
     *
     * @param paramMap
     * @param pageSize
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<PayOrder> queryPayOrders(Map<String, Object> paramMap, int start, int pageSize,
                                         String orderBy, boolean isAsc);

    /**
     * 创建支付单
     *
     * @param amount 支付金额
     * @return 返回生成的订单号
     */
    String createOrder(BigDecimal amount);

    /**
     * 创建7bao支付单
     *
     * @param amount 支付金额
     * @return 返回生成的订单号
     */
    String create7BaoOrder(BigDecimal amount,String account,String orderId);
/*
    *//**
     * 支付成功
     *
     * @param orderId 订单号
     * @param amount  支付金额
     *//*
    void paid(String orderId, BigDecimal amount);*/

    /**
     * 根据订单号查询
     *
     * @param orderId
     * @param lockMode
     * @return
     */
    PayOrder queryByOrderId(String orderId, Boolean lockMode);

    /**
     * 查询可用的支付单(已经支付且有余额为可用的支付单)
     *
     * @param buyerAccount 买家账号
     * @return
     */
    List<PayOrder> queryAvailablePayOrders(String buyerAccount, Boolean... isLocked);

    /**
     * 查询新资金收货商可用的支付单
     * */
    List<PayOrder> queryAvailablePayOrdersZBao(String buyerAccount, Boolean... isLocked);

    /**
     * 自动退款超过三个月的充值订单
     *
     * @return
     */
    List<PayOrder> queryTimeoutPayOrders(int days);

    /**
     * 批量更新
     * @param list
     */
    public void batchUpdate(List<PayOrder> list);

    /**
     * 根据id 批量更新
     * @param list
     */
    public void batchUpdateByIds(List<PayOrder> list);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    public void batchInsert(List<PayOrder> list);

    /**
     * 因为提现 还要扣除payorder
     * @param loginAccount 登录账户
     */
   void deletePayOrderCaseWithdraw(String loginAccount,BigDecimal changeAmount);
}
