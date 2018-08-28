package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;

import java.util.List;
import java.util.Map;

/**
 * 出货单DAO
 */
public interface IDeliveryOrderDao extends IMyBatisBaseDAO<DeliveryOrder,Long> {

    DeliveryOrder selectByIdForUpdate(long id);

    DeliveryOrder selectByOrderIdForUpdate(String orderId);

    /**
     * 查询等待排队的订单
     * @param params
     * @return
     */
    List<Long> queryInQueueOrderIds(Map<String, Object> params);

    /**
     * 统计金额和数量
     * @param paramMap
     * @return
     */
    DeliveryOrder statisAmountAndCount(Map<String, Object> paramMap);

    /**
     * 根据订单号查询订单
     * @param orderId
     * @return
     */
    DeliveryOrder getByOrderId(String orderId);

    /**
     * 一直未点上游戏交易或是一直在排队的订单，超过30分钟后将视为交易超时
     * @return
     */
    List<Long> queryTradeTimeoutOrders();

    /**
     * 查询待转账的订单ID集合
     * @return
     */
    List<String> queryWaitTransferOrderIds();

    /**
     * 超时订单，收货商长时间未分配角色/出货商长时间未点击我已发货
     *
     * @return
     */
    List<Long> queryTimeoutOrders();

    /**
     * 机器收货超时订单
     * */
    List<String> queryMachineDeliveryTimeoutOrders(int tradeTimeOut);

    /**
     * 统计金额汇总
     * @param paramMap
     * @return
     */
    DeliveryOrder statisAmount(Map<String, Object> paramMap);
    /**
     * 查询所有符合条件的实体
     * @param paramMap
     * @return
     */
    List<DeliveryOrder> selectAllOrder(Map<String, Object> paramMap);

    void updateByOrderId(DeliveryOrder deliveryOrder);

    /**
     * 根据订单号查询订单(不锁表)
     * */
    DeliveryOrder selectByOrderId(String orderId);

   DeliveryOrder queryDeliveryOrderByOrderId(String orderId);

    void updateOrder(DeliveryOrder deliveryOrder);

    List<Long> selectByAutometaTimeout(int minute);

    int countUnEndOrder(Map map);

    /**
     * 根据申诉单号查询订单详情
     */
    DeliveryOrder selectByAppealOrder(String appealOrder);

    /**
     * 分页查询申诉单
     *
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
     GenericPage<DeliveryOrder> queryAppealOrderInPage(Map<String, Object> paramMap, List<SortField> sortFields, int start, int pageSize);
}
