/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderIdRedisDAOImpl
 *	包	名：		com.wzitech.gamegold.order.dao.impl
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-14 下午3:50:46
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.order.dao.IOrderConfigLockRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * @author SunChengfei
 *
 */
@Repository
public class OrderConfigLockRedisDAOImpl extends AbstractRedisDAO<UserInfoEO> implements IOrderConfigLockRedisDAO {
	
	//业务锁只锁定十秒
	private static long TIME_OUT = 10L;
	
	@Autowired
	@Qualifier("userRedisTemplate")
	@Override
	public void setTemplate(StringRedisTemplate template) {
		super.template = template;
	};

	@Override
	public Boolean lock(String configResultId) {
		Boolean result = valueOps.setIfAbsent(RedisKeyHelper.orderConfigLock(configResultId), configResultId);
		template.expire(RedisKeyHelper.orderConfigLock(configResultId), TIME_OUT, TimeUnit.SECONDS);
		return result;
	}
	@Override
	public Boolean unlock(String configResultId) {
		template.delete(RedisKeyHelper.orderConfigLock(configResultId));
		return true;
	}

}
