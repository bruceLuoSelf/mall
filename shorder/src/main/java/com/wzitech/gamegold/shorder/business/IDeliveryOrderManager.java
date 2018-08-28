package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.DeliveryOrderGTRType;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 出货单管理
 */
public interface IDeliveryOrderManager {
    /**
     * 创建出货单
     *
     * @param order
     */
    DeliveryOrder createOrder(DeliveryOrder order);


    /**
     * 开始交易(我已登录游戏)
     * 没有订单号使用
     *
     * @param order 订单对象
     */
    void startTradingFormOrder(DeliveryOrder order);

    /**
     * 创建子订单
     *
     * @param order
     * @return
     */
    DeliveryOrder createAppealOrder(DeliveryOrder order);


    /**
     * 根据uid查该用户数据
     *
     * @param
     */
    int queryFund(String loginAccount);

    /**
     * 查询等待排队的订单
     */
    List<Long> queryInQueueOrderIds();

    /**
     * 根据ID查询出货单
     */
    DeliveryOrder getById(Long id);

    /**
     * 分页查找订单
     *
     * @param map
     * @param orderBy
     * @param start
     * @param pageSize
     * @return
     */
    GenericPage<DeliveryOrder> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy,
                                               boolean isAsc);

    /**
     * 统计金额和数量
     *
     * @param paramMap
     * @return
     */
    DeliveryOrder statisAmountAndCount(Map<String, Object> paramMap);

    /**
     * 查找所有符合条件的数据
     *
     * @param paramMap
     * @return
     */
    List<DeliveryOrder> selectAllOrder(Map<String, Object> paramMap);

    /**
     * 查找所有符合条件的数据不分页
     *
     * @param paramMap
     * @return
     */
    List<DeliveryOrder> selectAllorderByMap(Map<String, Object> paramMap);

    /**
     * 根据ID查询出货单并锁住待修改
     *
     * @param id
     * @return
     */
    DeliveryOrder getByIdForUpdate(Long id);

    /**
     * 更新订单
     *
     * @param order
     */
    void update(DeliveryOrder order);

    /**
     * 增加出货单实际出货数量，并计算实际出货金额
     *
     * @param id
     * @param count
     */
    void incrRealCount(long id, long count);

    /**
     * 修改收到的数量
     *
     * @param mainOrderId
     * @param subOrderId
     * @param receiveCount
     */
    @Transactional
    void changeReceiveCount(String mainOrderId, long subOrderId, long receiveCount);

    /**
     * 取消所有的子订单
     *
     * @param mainOrderId
     * @param subOrderId
     * @param type
     */
    void cancelAllSubOrder(String mainOrderId, Long subOrderId, DeliveryOrderGTRType type);

    /**
     * 更新游戏账号数据
     *
     * @param dbSubOrder
     * @param type
     */
    void updateGameAccountInfo(DeliverySubOrder dbSubOrder, DeliveryOrderGTRType type, Boolean offline);

    /**
     * 撤单
     *
     * @param id
     * @param reason
     * @param remark
     */
    void cancelOrder(long id, int reason, String remark);


    /**
     * rc物服撤单
     *
     * @param id
     * @param reason
     * @param remark
     */
    void cancelOrder(String orderId, int reason, String remark);

    /**
     * 申请部分完单
     *
     * @param id
     */
    void applyForCompletePart(long id);

    /**
     * 根据订单号查询订单
     *
     * @param orderId
     * @return
     */
    DeliveryOrder getByOrderId(String orderId);

    /**
     * 查询交易超时的订单(一直未点上游戏交易或是一直在排队的订单，超过30分钟后将被视为交易超时)
     *
     * @return
     */
    List<Long> queryTradeTimeoutOrders();

    /**
     * 取消超时的订单(一直未点上游戏交易或是一直在排队的订单，超过30分钟后将被视为交易超时)
     *
     * @param id
     * @return
     */
    void cancelTimeoutOrder(Long id);

    /**
     * 人工完单
     *
     * @param mainOrderId             出货主订单号
     * @param subOrderReceiveCountMap 每个子订单收到的实际数量
     *                                <p>格式：</p>
     *                                <li>key=子订单ID</li>
     *                                <li>value=实际收到的数量</li>
     */
    List<GameAccount> manualFinishOrder(String mainOrderId, Map<Long, Long> subOrderReceiveCountMap, Boolean status) throws IOException;

    /**
     * 查询待转账的订单ID集合
     *
     * @return
     */
    List<String> queryWaitTransferOrderIds();

    /**
     * 出货单结算
     *
     * @param deliveryOrderId
     */
    void settlement(String deliveryOrderId) throws IOException;

    /**
     * 出货单转账
     *
     * @param orderId
     */
    void transfer(String orderId);

    /**
     * 超时订单，收货商长时间未分配角色/出货商长时间未点击我已发货
     *
     * @return
     */
    List<Long> queryTimeoutOrders();

    /**
     * 机器收货超时订单
     */
    List<String> queryMachineDeliveryTimeoutOrders();


    /**
     * 设置超时
     *
     * @param id
     * @return
     */
    void setTimeoutOrder(Long id);

    /**
     * 统计金额汇总
     *
     * @param paramMap
     * @return
     */
    DeliveryOrder statisAmount(Map<String, Object> paramMap);

    //获取出货商
    DeliveryOrder queryDeliveryOrderByOrderId(String orderId);

    //向表中插入出货商和收货商的环信账号和密码
    void updateOrder(DeliveryOrder deliveryOrder);

    /**
     * 全自动异常订单分配寄售客服(手动)
     *
     * @param orderId
     */
    public void distributionService(String orderId);

    //查询超时的自动化订单
    List<Long> selectByAutometaTimeout(int minute);

//    public Boolean createSubOrderByAutoMateError(Long deliveryOrder, Integer minute);

    public void logisticsSheet(String orderId, Long goldCount) throws IOException;

    void confirmShipment(Map<String, Integer> map);


    /**
     * 获取订单状态
     */
    int getOrderStatus(String orderId);

    /**
     * 根据订单id获取订单并锁表
     *
     * @param orderId
     * @return
     */
    DeliveryOrder selectByOrderIdForUpdate(String orderId);


    /**
     * 修改状态
     *
     * @param orderId
     */
    void setStartByOrderForUpdata(String orderId, Integer type);

    /**
     * 存库不足撤单（配单逻辑调用）
     *
     * @param orderId
     * @param reason
     * @param remark
     */
    void cancelOrderByNotEnoughRepertory(String orderId, int reason, String remark);


    /**
     * 邮寄收货完单逻辑
     *
     * @param subOrdersInfos key是对应子订单的id  value是实际收货数量 如果撤单 请将value设置为0
     *                       子订单必须拥有同一且唯一的主订单编号
     * @param appealReason   如子订单是撤单的需要 设置key为对应子订单id value为自定义取消原因  如果不存在撤单的子订单  此处传NULL 不要填map进去
     * @param reason         子订单给定撤单原因  以int表示 设置key为对应子订单id value为取消原因  如果不存在撤单的子订单  此处传Null 不要传递map进去
     * @param mainOrderId    即是map中子订单的对应主订单号
     * @param remark         备注信息  如果有 数据格式同appealReason没有请填null
     * @throws IOException
     */
    void handleOrderForMailDeliveryOrderMax(Map<Long, Long> subOrdersInfos,
                                            String mainOrderId,
                                            Map<Long, String> appealReason,
                                            Map<Long, Integer> reason,
                                            Map<Long, String> remark) throws IOException;

    /**
     * 订单出现异常，自动分配物服
     */
    void autoDistributionManager(String orderId,int source,Long id);

    /**
     * 拍卖订单出现异常,自动分配物服
     *
     */
//    void auctionDistributionManager(Long id,int source);

    /**
     * 子订单转人工  参数：1.子订单id， 2.是否是用户点击转人工
     */
    void subOrderAutoDistributionManager(Long subOrderId, Integer clickFromUser,String otherReason);

    /**
     * 申诉单完单
     */
    void appealFinishDeliveryOrder(String orderId, Long realCount, String otherReason);

    /**
     * 根据订单号查询订单(不锁表)
     */
    DeliveryOrder selectByOrderId(String orderId);

    /**
     * 根据申诉单号查询订单详情
     */
    DeliveryOrder selectByAppealOrder(String appealOrder);

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
    GenericPage<DeliveryOrder> queryAppealOrderInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc);
}
