/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderInfoRedisDAOImpl
 *	包	名：		com.wzitech.gamegold.order.dao.impl
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午10:39:04
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.order.dao.IOrderInfoRedisDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * @author SunChengfei
 *
 */
@Repository
public class OrderInfoRedisDAOImpl extends AbstractRedisDAO<OrderInfoEO> implements IOrderInfoRedisDAO{
	@Autowired
	@Qualifier("userRedisTemplate")
	@Override
	public void setTemplate(StringRedisTemplate template) {
		super.template = template;
	};
	
	JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
	
	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.order.dao.IOrderInfoRedisDAO#saveOrder(com.wzitech.gamegold.order.entity.OrderInfoEO)
	 */
	@Override
	public void saveOrder(OrderInfoEO orderInfoEO) {
		String orderJson = jsonMapper.toJson(orderInfoEO);
		zSetOps.add(RedisKeyHelper.orderList(orderInfoEO.getUid()), orderJson, orderInfoEO.getCreateTime().getTime());
		
		if(zSetOps.size(RedisKeyHelper.orderList(orderInfoEO.getUid())) > 5){
			zSetOps.removeRange(RedisKeyHelper.orderList(orderInfoEO.getUid()), 0, 0);
		}

		// 保存3个月
		template.expire(RedisKeyHelper.orderList(orderInfoEO.getUid()), 90, TimeUnit.DAYS);
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.order.dao.IOrderInfoRedisDAO#queryOrder(java.lang.String)
	 */
	@Override
	public List<OrderInfoEO> queryOrder(String uid) {
		Set<String> jsonSet = zSetOps.reverseRange(RedisKeyHelper.orderList(uid), 0, zSetOps.size(RedisKeyHelper.orderList(uid)));
		if(jsonSet == null || jsonSet.size() == 0){
			return null;
		}
		
		List<OrderInfoEO> orderList = new ArrayList<OrderInfoEO>();
		List<String> jsonList = new ArrayList<String>(jsonSet);
		for (String string : jsonList) {
			OrderInfoEO orderInfoEO = jsonMapper.fromJson(string, OrderInfoEO.class);
			orderList.add(orderInfoEO);
		}
		return orderList;
	}

}
