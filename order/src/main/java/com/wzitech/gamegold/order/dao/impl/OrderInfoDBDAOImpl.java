/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderInfoDBDAOImpl
 *	包	名：		com.wzitech.gamegold.order.dao.impl
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-14 下午3:11:24
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wzitech.gamegold.order.dto.SimpleOrderDTO;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * @author SunChengfei
 *
 */
@Repository
public class OrderInfoDBDAOImpl extends AbstractMyBatisDAO<OrderInfoEO, Long> implements IOrderInfoDBDAO{

	@Override
	public List<OrderInfoEO> queryOrderForAutoPlay(Map<String, Object> queryMap) {
		return this.getSqlSession().selectList(getMapperNamespace() + ".queryOrderForAutoPlay", queryMap);
	}

	@Override
	public void updateOrderState(OrderInfoEO orderInfo) {
		this.getSqlSession().update(getMapperNamespace() + ".updateOrderState", orderInfo);
	}
	@Override
	public void updateOrderCancelReson(OrderInfoEO orderInfo) {
		this.getSqlSession().update(getMapperNamespace() + ".updateOrderCancelReson", orderInfo);
	}

    /**
     * 统计保费
     *
     * @param queryMap
     * @return
     */
    @Override
    public BigDecimal statisticPremiums(Map<String, Object> queryMap) {
        BigDecimal preminums = this.getSqlSession().selectOne(getMapperNamespace() + ".statisticPremiums", queryMap);
        if (preminums == null) {
            preminums = BigDecimal.ZERO;
        }
        preminums = preminums.setScale(2, BigDecimal.ROUND_HALF_UP);
        return preminums;
    }

    @Override
	public Integer autoCancellTimeoutOrder(Map<String, Object> queryMap) {
		return this.getSqlSession().update(getMapperNamespace() + ".autoCancellTimeoutOrder", queryMap);
	}

    /**
     * 根据订单号查询订单，并锁定
     * @param orderId
     * @return
     */
    public OrderInfoEO queryOrderForUpdate(String orderId) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".queryOrderForUpdate", orderId);
    }

    /**
     * 根据订单号查询订单，并锁定，锁定失败抛异常
     * @param orderId
     * @return
     * @throws Exception
     */
    public OrderInfoEO queryOrderForUpdateNowait(String orderId) throws Exception {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".queryOrderForUpdateNowait", orderId);
    }

    /**
     * 更新订单为已评价
     *
     * @param orderId
     */
    @Override
    public void updateOrderAlreadyEvaluate(String orderId) {
        this.getSqlSession().update(getMapperNamespace() + ".updateOrderAlreadyEvaluate", orderId);
    }

    /**
     * 更新订单为已追加评价
     *
     * @param orderId
     */
    @Override
    public void updateOrderAlreadyReEvaluate(String orderId) {
        this.getSqlSession().update(getMapperNamespace() + ".updateOrderAlreadyReEvaluate", orderId);
    }

    /**
     * 查询指定订单状态下，超过4个小时未评价的订单号
     */
    @Override
    public List<String> queryAfter4HourNotEvaluateOrders(Map<String, Object> params) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryAfter4HourNotEvaluateOrders", params);
    }

    /**
     * 返回订单信息
     *
     * @param params
     * @return
     */
    @Override
    public List<SimpleOrderDTO> selectOrderByMap(Map<String, Object> params) {
        return getSqlSession().selectList(getMapperNamespace() + ".selectSimpleData", params);
    }

    /**
     * 查询客服当前待处理订单数量和最后分配的时间
     *
     * @param queryParams
     * @return
     */
    @Override
    public Map<String, Object> selectCurrWaitDeliveryData(Map<String, Object> queryParams) {
        return this.getSqlSession().selectOne(this.mapperNamespace + ".selectCurrWaitDeliveryData", queryParams);
    }

    /**
     * 根据订单号查询订单
     * @param orderId
     * @return
     */
    @Override
    public OrderInfoEO queryOrderId(String orderId) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".queryOrderId", orderId);
    }
}
