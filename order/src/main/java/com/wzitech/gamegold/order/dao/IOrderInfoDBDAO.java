/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IOrderInfoDBDAO
 *	包	名：		com.wzitech.gamegold.order.dao
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-14 下午3:10:59
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.dto.SimpleOrderDTO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 订单DB
 * @author SunChengfei
 *
 */
public interface IOrderInfoDBDAO extends IMyBatisBaseDAO<OrderInfoEO, Long>{

	/**
	 * 
	 * <p>查询已付款的但是自动打款时间已经到的未结单的订单</p> 
	 * @author ztjie
	 * @date 2014-2-28 下午4:53:54
	 * @param queryMap
	 * @return
	 * @see
	 */
	List<OrderInfoEO> queryOrderForAutoPlay(Map<String, Object> queryMap);

	/**
	 * 
	 * <p>修改订单状态</p> 
	 * @author Think
	 * @date 2014-3-9 上午8:34:46
	 * @param orderInfo
	 * @see
	 */
	void updateOrderState(OrderInfoEO orderInfo);

	/**
	 * 
	 * <p>取消指定时间之前的未支付的订单</p> 
	 * @author ztjie
	 * @date 2014-3-27 下午11:48:15
	 * @param queryMap
	 * @see
	 */
	Integer autoCancellTimeoutOrder(Map<String, Object> queryMap);
	/**
	 * 保存订单取消原因
	 * @param orderInfo
	 */
	public void updateOrderCancelReson(OrderInfoEO orderInfo);

    /**
     * 统计保费
     * @param queryMap
     * @return
     */
    BigDecimal statisticPremiums(Map<String, Object> queryMap);

    /**
     * 根据订单号查询订单，并锁定
     * @param orderId
     * @return
     */
    public OrderInfoEO queryOrderForUpdate(String orderId);

    /**
     * 根据订单号查询订单，并锁定，锁定失败抛异常
     * @param orderId
     * @return
     * @throws Exception
     */
    public OrderInfoEO queryOrderForUpdateNowait(String orderId) throws Exception;

	/**
	 * 更新订单为已评价
	 *
	 * @param orderId
	 */
	void updateOrderAlreadyEvaluate(String orderId);

	/**
	 * 更新订单为已追加评价
	 *
	 * @param orderId
	 */
	void updateOrderAlreadyReEvaluate(String orderId);

	/**
	 * 查询指定订单状态下，超过4个小时未评价的订单号
	 */
	List<String> queryAfter4HourNotEvaluateOrders(Map<String, Object> params);

	/**
	 * 返回订单信息
	 * @param params
	 * @return
	 */
	List<SimpleOrderDTO> selectOrderByMap(Map<String, Object> params);

	/**
	 * 查询客服当前待发货订单数量和最后分配的时间
	 * @param queryParams
	 * @return
	 */
	Map<String, Object> selectCurrWaitDeliveryData(Map<String, Object> queryParams);



	/**
	 * 根据订单号查询订单
	 * @param orderId
	 * @return
	 */
	public OrderInfoEO queryOrderId(String orderId);
}
