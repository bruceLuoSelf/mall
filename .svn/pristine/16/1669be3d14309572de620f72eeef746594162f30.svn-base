/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ServicerOrderManager
 *	包	名：		com.wzitech.gamegold.common.servicer.impl
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午11:21:23
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.servicer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;

/**
 * 客服处理订单数管理
 * @author SunChengfei
 *
 */
@Component
public class ServicerOrderManagerImpl extends AbstractRedisDAO<BaseEntity> implements IServicerOrderManager{
	@Autowired
	@Qualifier("userRedisTemplate")
	@Override
	public void setTemplate(StringRedisTemplate template) {
		super.template = template;
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#isInitOrderNum(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean isInitOrderNum(String gameName, String region,
			String server, String gameRace, Long servicerId) {
		Set<String> set = zSetOps.range(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace), 0, 
				zSetOps.size(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace)));
		
		if(set.contains(String.valueOf(servicerId))){
			return true;
		}
		
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#initOrderNum(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void initOrderNum(String gameName, String region, String server,
			String gameRace, Long servicerId) {
		zSetOps.add(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace), 
				String.valueOf(servicerId), 0);
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#initOrderNum(java.lang.Long)
	 */
	@Override
	public void initOrderNum(Long servicerId) {
		zSetOps.add(RedisKeyHelper.serviceAllOrder(), String.valueOf(servicerId), 0);
	}
	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#initOrderNum(java.lang.Long)
	 */
	@Override
	public void serviceOrderNum(Long servicerId,int orderNum) {
		zSetOps.add(RedisKeyHelper.serviceAllOrder(), String.valueOf(servicerId), orderNum);

		// 初始化客服已处理的订单数为0
		initOrderProcessedCount(servicerId);
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#getOrderNum(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public Double getOrderNum(String gameName, String region, String server,
			String gameRace, Long servicerId) {
		Set<String> set = zSetOps.range(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace), 0, 
				zSetOps.size(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace)));
		
		if(set.contains(String.valueOf(servicerId))){
			return zSetOps.score(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace), String.valueOf(servicerId));
		}
		
		return (double) 0;
	};
	
	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#addOrderNum(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void addOrderNum(String gameName, String region, String server,
			String gameRace, Long servicerId) {
		zSetOps.incrementScore(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace), 
				String.valueOf(servicerId), 1);
		
		zSetOps.incrementScore(RedisKeyHelper.serviceAllOrder(), String.valueOf(servicerId), 1);
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#subOrderNum(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void subOrderNum(String gameName, String region, String server,
			String gameRace, Long servicerId) {
		zSetOps.incrementScore(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace), 
				String.valueOf(servicerId), -1);
		
		zSetOps.incrementScore(RedisKeyHelper.serviceAllOrder(), String.valueOf(servicerId), -1);
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#sortServicer(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> sortServicer(String gameName, String region,
			String server, String gameRace) {
		// 按照处理订单数，从小到大排序
		Set<String> set = zSetOps.range(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace), 
				0, zSetOps.size(RedisKeyHelper.servicerOrder(gameName,region,server,gameRace)));
		if(set == null || set.size() == 0){
			return null;
		}
		return new ArrayList<String>(set);
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.servicer.IServicerOrderManager#sortServicer()
	 */
	@Override
	public List<String> sortServicer() {
		// 按照处理订单数，从小到大排序
		Set<String> set = zSetOps.range(RedisKeyHelper.serviceAllOrder(), 
				0, zSetOps.size(RedisKeyHelper.serviceAllOrder()));
		if(set == null || set.size() == 0){
			return null;
		}
		return new ArrayList<String>(set);
	}


	/**
	 * 初始化客服处理卖家入驻数
	 *
	 * @param servicerId
	 */
	@Override
	public void initEnterNum(Long servicerId) {
		zSetOps.add(RedisKeyHelper.serviceAllEnter(), String.valueOf(servicerId), 0);
	}

	/**
	 * 更新卖家入驻客服当前处理的入驻数
	 *
	 * @param servicerId
	 * @param num
	 */
	@Override
	public void updateServiceEnterNum(Long servicerId, int num) {
		zSetOps.add(RedisKeyHelper.serviceAllEnter(), String.valueOf(servicerId), num);
	}

	/**
	 * 按照处理卖家入驻数从小到大排序客服Id
	 *
	 * @return
	 */
	@Override
	public List<String> sortEnterServicer() {
		// 按照处理卖家入驻数，从小到大排序
		Set<String> set = zSetOps.range(RedisKeyHelper.serviceAllEnter(),
				0, zSetOps.size(RedisKeyHelper.serviceAllEnter()));
		if(set == null || set.size() == 0){
			return null;
		}
		return new ArrayList<String>(set);
	}

	/**
	 * 客服处理卖家入驻数+1
	 * @param servicerId
	 */
	@Override
	public void addEnterNum(Long servicerId) {
		zSetOps.incrementScore(RedisKeyHelper.serviceAllEnter(), String.valueOf(servicerId), 1);
	}

	/**
	 * 客服处理卖家入驻数-1
	 */
	@Override
	public void subEnterNum(Long servicerId) {
		zSetOps.incrementScore(RedisKeyHelper.serviceAllEnter(), String.valueOf(servicerId), -1);
	}

	/**
	 * 客服已处理的订单数量+1
	 *
	 * @param servicerId
	 */
	@Override
	public void addOrderProcessedCount(Long servicerId) {
		valueOps.increment(RedisKeyHelper.serviceOrderProcessedCount(servicerId), 1);
	}

	/**
	 * 获取客服已处理的订单数量
	 * @param servicerId
	 * @return
	 */
	public int getOrderProcessedCount(Long servicerId) {
		String count = valueOps.get(RedisKeyHelper.serviceOrderProcessedCount(servicerId));
		if (count != null)
			return Integer.valueOf(count);
		return 0;
	}

	/**
	 * 初始化客服已处理的订单数量为0
	 *
	 * @param servicerId
	 */
	@Override
	public void initOrderProcessedCount(Long servicerId) {
		valueOps.set(RedisKeyHelper.serviceOrderProcessedCount(servicerId), "0");
	}

	/**
	 * 根据用户ID获取分数
	 * @param userId
	 * @return
	 */
	public double getScoreByUserId(Long userId) {
		return zSetOps.score(RedisKeyHelper.serviceAllOrder(), String.valueOf(userId));
	}
}
