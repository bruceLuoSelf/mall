package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.common.enums.ShDeliveryTypeEnum;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出货单DAO
 */
@Repository
public class DeliveryOrderDaoImpl extends AbstractMyBatisDAO<DeliveryOrder, Long> implements IDeliveryOrderDao {
    @Override
    public DeliveryOrder selectByIdForUpdate(long id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("locked", true);
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByIdForUpdate", params);
    }

    @Override
    public DeliveryOrder selectByOrderIdForUpdate(String orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        params.put("locked", true);
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByOrderIdForUpdate", params);
    }

    @Override
    public DeliveryOrder selectByOrderId(String orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByOrderId", params);
    }

    /**
     * 根据申诉单号查询订单详情
     */
    @Override
    public DeliveryOrder selectByAppealOrder(String appealOrder) {
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("appealOrder", appealOrder);
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByAppealOrder", params);
    }

    /**
     * 查询等待排队的订单
     *
     * @param params
     * @return
     */
    @Override
    public List<Long> queryInQueueOrderIds(Map<String, Object> params) {
        return getSqlSession().selectList(getMapperNamespace() + ".queryInQueueOrderIds", params);
    }

    /**
     * 统计金额和数量
     *
     * @param paramMap
     * @return
     */
    @Override
    public DeliveryOrder statisAmountAndCount(Map<String, Object> paramMap) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".statisAmountAndCount", paramMap);
    }

    /**
     * 根据订单号查询订单
     *
     * @param orderId
     * @return
     */
    @Override
    public DeliveryOrder getByOrderId(String orderId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        return this.getSqlSession().selectOne(getMapperNamespace() + ".getByOrderId", params);
    }

    /**
     * 一直未点上游戏交易或是一直在排队的订单，超过30分钟后将视为交易超时
     *
     * @return
     */
    @Override
    public List<Long> queryTradeTimeoutOrders() {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryTradeTimeoutOrders");
    }

    /**
     * 查询待转账的订单ID集合
     *
     * @return
     */
    @Override
    public List<String> queryWaitTransferOrderIds() {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryWaitTransferOrderIds");
    }

    /**
     * 超时订单，收货商长时间未分配角色/出货商长时间未点击我已发货
     *
     * @return
     */
    @Override
    public List<Long> queryTimeoutOrders() {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryTimeoutOrders");
    }

    /**
     * 机器收货超时订单
     */
    @Override
    public List<String> queryMachineDeliveryTimeoutOrders(int tradeTimeOut) {
        Map map = new HashMap(2);
        map.put("tradeTimeOut",tradeTimeOut+" min");
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryMachineDeliveryTimeoutOrders",map);
    }

    @Override
    public List<DeliveryOrder> selectAllOrder(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectAllOrder", paramMap);
    }

    /**
     * 统计金额汇总
     *
     * @param paramMap
     * @return
     */
    @Override
    public DeliveryOrder statisAmount(Map<String, Object> paramMap) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".summaryRelieve", paramMap);
    }

    public DeliveryOrder queryDeliveryOrderByOrderId(String OrderId) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectDeliveryOrderByOrderId", OrderId);
    }


    @Override
    public void updateByOrderId(DeliveryOrder deliveryOrder) {
        this.getSqlSession().selectOne(getMapperNamespace() + ".updateByOrderId", deliveryOrder);
    }

    public void updateOrder(DeliveryOrder deliveryOrder) {
        this.getSqlSession().update(getMapperNamespace() + ".updateOrder", deliveryOrder);
    }

    /**
     * 统计自动化超时的订单
     *
     * @param minute
     * @return
     */
    @Override
    public List<Long> selectByAutometaTimeout(int minute) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("timeout", minute + " min");
        paramMap.put("deliveryType", ShDeliveryTypeEnum.Robot.getCode());
        paramMap.put("status", DeliveryOrderStatus.INQUEUE.getCode() + "," + DeliveryOrderStatus.TRADING.getCode());
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectByAutometaTimeout", paramMap);
    }

    public int countUnEndOrder(Map map) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".countUnEndOrder", map);
    }

    /**
     * 分页查询申诉单
     *
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public GenericPage<DeliveryOrder> queryAppealOrderInPage(Map<String, Object> paramMap, List<SortField> sortFields, int start, int pageSize) {
        if (null == paramMap) {
            paramMap = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        String orderByStr = "";
        if (sortFields != null) {
            for (int i = 0, j = sortFields.size(); i < j; i++) {
                SortField field = sortFields.get(i);
                orderByStr += "\"" + field.getField() + "\" " + field.getSort();
                if (i != (j - 1)) {
                    orderByStr += ",";
                }
            }
        }

        paramMap.put("ORDERBY", orderByStr);

        int totalSize = countByMap(paramMap);

        // 如果数据Count为0，则直接返回
        if (totalSize == 0) {
            return new GenericPage<DeliveryOrder>(start, totalSize, pageSize, null);
        }

        List<DeliveryOrder> pagedData = this.getSqlSession().selectList(this.mapperNamespace + ".queryAppealOrderInPage",
                paramMap, new RowBounds(start, pageSize));

        return new GenericPage<DeliveryOrder>(start, totalSize, pageSize, pagedData);
    }
}
