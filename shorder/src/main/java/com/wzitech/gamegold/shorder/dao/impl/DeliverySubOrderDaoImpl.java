package com.wzitech.gamegold.shorder.dao.impl;

import com.google.common.collect.Maps;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出货子订单dao
 */
@Repository
public class DeliverySubOrderDaoImpl extends AbstractMyBatisDAO<DeliverySubOrder, Long> implements IDeliverySubOrderDao {
    @Override
    public DeliverySubOrder selectByIdForUpdate(Long id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("locked", true);
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByIdForUpdate", params);
    }

    @Override
    public DeliverySubOrder selectByUniqueId(Long id) {
        return getSqlSession().selectOne(getMapperNamespace()+".selectById",id);
    }

    /**
     * 查询交易中的订单
     * @param chId 出货单ID
     * @param locked
     * @return
     */
    @Override
    public List<DeliverySubOrder> queryWaitForTradeOrders(Long chId, Boolean locked) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("chId", chId);
        params.put("status", DeliveryOrderStatus.TRADING.getCode());
        params.put("locked", locked);
        return selectByMap(params);
    }

    /**
     * 获取分组的游戏账号信息
     *
     * @param chId
     * @return
     */
    @Override
    public List<DeliverySubOrder> findGameAccountGroupData(Long chId) {
        return getSqlSession().selectList(getMapperNamespace() + ".findGameAccountGroupData", chId);
    }

    /**
     * 根据订单号查询所有的子订单
     *
     * @param orderId
     * @return
     */
    @Override
    public List<DeliverySubOrder> queryAllByOrderId(String orderId) {
        return getSqlSession().selectList(getMapperNamespace() + ".selectAllByOrderId", orderId);
    }

    /**
     * 根据订单号查询所有的子订单
     *
     * @param orderId
     * @return
     */
    @Override
    public List<DeliverySubOrder> queryAllByOrderIdForUpdate(String orderId) {
        return getSqlSession().selectList(getMapperNamespace() + ".queryAllByOrderIdForUpdate", orderId);
    }

    /**
     * 根据订单号,收货商账号,游戏账号,查询所有的子订单
     *
     * @param orderId
     * @return
     */
    @Override
    public List<DeliverySubOrder> queryAllByOrderForUpdate(String orderId,String buyerAccount,String gameAccount) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("orderId",orderId);
        map.put("buyerAccount",buyerAccount);
        map.put("gameAccount",gameAccount);
        return getSqlSession().selectList(getMapperNamespace() + ".queryAllByOrderForUpdate", map);
    }

    /**
     * 更加主订单查找所有子订单
     * @param chId 出货单ID
     * @return
     */
    public List<DeliverySubOrder> querySubOrdersForUpdate(Long chId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("chId", chId);
        params.put("locked", true);
        return selectByMap(params);
    }

    /**
     * 异常转人工的订单列表查找
     * @param queryMap
     * @return
     */
    @Override
    public List<DeliverySubOrder> queryMachineAbnormalTurnManualOrderList(Map<String,Object> queryMap) {
        queryMap.put("status", 3);
        return getSqlSession().selectList(getMapperNamespace() + ".queryMachineAbnormalTurnManualOrderList", queryMap);
    }

    @Override
    public boolean selectByAccountInqueue(DeliveryOrder order) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("buyerAccount", order.getBuyerAccount());
        params.put("gameName", order.getGameName());
        params.put("region", order.getRegion());
        params.put("server", order.getServer());
        params.put("gameRace", order.getGameRace());
        params.put("status",DeliveryOrderStatus.INQUEUE.getCode());
        Long count = getSqlSession().selectOne(getMapperNamespace() + ".selectByAccountInqueue", params);
        if(count == null){
            count = 0L;
        }
        return  count > 0;

    }

    /**
     * 一直未点上游戏交易或是一直在排队的订单，超过30分钟后将视为交易超时
     *
     * @return
     */
    @Override
    public List<Long> queryWithdrawalTradeTimeoutOrders() {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryWithdrawalTradeTimeoutOrders");
    }

    @Override
    public DeliverySubOrder selectByRobotId(Long id, String orderId) {
        Map<String,Object> map =new HashMap<String, Object>();
        map.put("id",id);
        if(StringUtils.isNotBlank(orderId)){
            map.put("orderId",orderId);
        }
        return getSqlSession().selectOne(getMapperNamespace()+".selectByRobotId",map);
    }

    /**
     * 根据子订单id查询子订单信息
     * */
    @Override
    public DeliverySubOrder findSubOrderById(Long id){
        Map<String,Object> map =new HashMap<String, Object>(2);
        map.put("id",id);
        return getSqlSession().selectOne(getMapperNamespace()+".findSubOrderById",map);
    }

    /**
     * 根据子订单id查询子订单信息
     * */
    @Override
    public void updateDeliverySubOrderAppealOrderStatusById(Long id,Integer status){
        Map<String,Object> map =new HashMap<String, Object>(2);
        map.put("id",id);
        map.put("appealOrderStatus",status);
        getSqlSession().update(getMapperNamespace()+".updateDeliverySubOrderAppealOrderStatusById",map);
    }

    /**
     * according to appealOrder to find subOrder details
     * */
    @Override
    public DeliverySubOrder findSubOrderByAppealOrder(String appealOrder){
        Map<String,Object> map =new HashMap<String, Object>(2);
        map.put("appealOrder",appealOrder);
        return getSqlSession().selectOne(getMapperNamespace()+".findSubOrderByAppealOrder",map);
    }


    @Override
    public DeliverySubOrder findSubOrderByOrderId(String orderId){
        Map<String,Object> map =new HashMap<String, Object>(2);
        map.put("orderId",orderId);
        return getSqlSession().selectOne(getMapperNamespace()+".findSubOrderByOrderId",map);
    }

    /**
     * 邮寄收货自动化响应超时订单
     */
    @Override
    public List<Long> queryMachineSellerDeliveryTimeOutOrders(int tradeTimeOut) {
        Map map = new HashMap(2);
        map.put("tradeTimeOut",tradeTimeOut+" min");
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryMachineSellerDeliveryTimeOutOrders",map);
    }

    @Override
    public void updateById(DeliverySubOrder subOrder) {
        getSqlSession().update(getMapperNamespace()+".updateById", subOrder);
    }

    @Override
    public List<DeliverySubOrder> queryUniqueMessageOrdersCount(String orderId) {
        return this.getSqlSession().selectList(getMapperNamespace()+".queryUniqueMessageOrdersCount", orderId);
    }

    /**
     * 更新是否已分仓
     * @param subOrder
     */
    @Override
    public void updateSplited(DeliverySubOrder subOrder) {
        this.getSqlSession().update(getMapperNamespace()+".updateSplited",subOrder);
    }
}
