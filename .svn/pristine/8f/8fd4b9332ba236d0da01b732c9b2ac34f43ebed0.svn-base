package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;

import java.util.List;
import java.util.Map;

/**
 * 出货子订单dao
 */
public interface IDeliverySubOrderDao extends IMyBatisBaseDAO<DeliverySubOrder, Long> {
    DeliverySubOrder selectByIdForUpdate(Long id);

    DeliverySubOrder selectByUniqueId(Long id);

    /**
     * 查询交易中的订单
     *
     * @param chId   出货单ID
     * @param locked
     * @return
     */
    List<DeliverySubOrder> queryWaitForTradeOrders(Long chId, Boolean locked);

    /**
     * 获取分组的游戏账号信息
     *
     * @param chId
     * @return
     */
    List<DeliverySubOrder> findGameAccountGroupData(Long chId);


    List<DeliverySubOrder> queryUniqueMessageOrdersCount(String orderId);
    /**
     * 根据订单号查询所有的子订单
     *
     * @param orderId
     * @return
     */
    List<DeliverySubOrder> queryAllByOrderId(String orderId);

    /**
     * 根据订单号查询所有的子订单
     *
     * @param orderId
     * @return
     */
    List<DeliverySubOrder> queryAllByOrderIdForUpdate(String orderId);

    /**
     * 根据主订单号 收货商账号 游戏账号
     */
    List<DeliverySubOrder> queryAllByOrderForUpdate(String orderId,String buyerAccount,String gameAccount);

    /**
     * 更加主订单查找所有子订单
     *
     * @param chId 出货单ID
     * @return
     */
    List<DeliverySubOrder> querySubOrdersForUpdate(Long chId);

    /**
     * 异常转人工订单列表
     *
     * @param queryMap
     * @return
     */
    List<DeliverySubOrder> queryMachineAbnormalTurnManualOrderList(Map<String, Object> queryMap);
    boolean selectByAccountInqueue(DeliveryOrder order);

    /**
     * 一直未点上游戏交易或是一直在排队的订单，超过30分钟后将视为交易超时
     * @return
     */
    List<Long> queryWithdrawalTradeTimeoutOrders();

    /**
     * 根据订单号查询订单信息
     * @param id
     * @param orderId
     * @return
     */
    DeliverySubOrder selectByRobotId(Long id,String orderId);

    /**
     * 根据子订单id查询子订单信息
     * */
    DeliverySubOrder findSubOrderById(Long id);

    /**
     * 子订单号更新子订单申诉单状态
     * @param id
     * @param status
     */
    void updateDeliverySubOrderAppealOrderStatusById(Long id,Integer status);

    /**
     * according to appealOrder to find subOrder details
     * */
    DeliverySubOrder findSubOrderByAppealOrder(String appealOrder);

    DeliverySubOrder findSubOrderByOrderId(String orderId);

    /**
     * 邮寄收货自动化响应超时订单
     */
    List<Long> queryMachineSellerDeliveryTimeOutOrders(int tradeTimeOut);

    void updateById(DeliverySubOrder subOrder);

    void updateSplited(DeliverySubOrder subOrder);

}
