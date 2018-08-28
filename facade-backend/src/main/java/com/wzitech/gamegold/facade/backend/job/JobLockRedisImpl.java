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
package com.wzitech.gamegold.facade.backend.job;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.wzitech.gamegold.common.utils.RedisKeyHelper;

import java.util.concurrent.TimeUnit;

/**
 * @author SunChengfei
 *
 */
@Component("jobLock")
public class JobLockRedisImpl {
	
	/**
	 * 字符类型模板
	 */
	@Resource(name="userRedisTemplate")
	private StringRedisTemplate template;

	public Boolean lock(String jobId) {
		Boolean result = this.template.opsForValue().setIfAbsent(RedisKeyHelper.jobLock(jobId), jobId);
		template.expire(RedisKeyHelper.jobLock(jobId), 30, TimeUnit.MINUTES);
		return result;
	}
	
	public Boolean unlock(String jobId) {
		template.delete(RedisKeyHelper.jobLock(jobId));
		return true;
	}

}