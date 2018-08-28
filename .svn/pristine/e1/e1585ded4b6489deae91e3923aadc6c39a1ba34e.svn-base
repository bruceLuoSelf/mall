/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		VerifyCodeRedisDAOImpl
 *	包	名：		com.wzitech.gamegold.usermgmt.dao.redis.impl
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:46:53
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.dao.redis.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.usermgmt.dao.redis.IVerifyCodeRedisDAO;

/**
 * 手机验证码DAO
 * @author SunChengfei
 *
 */
@Repository("verifyCodeRedisDAOImpl")
public class VerifyCodeRedisDAOImpl extends AbstractRedisDAO<BaseEntity> implements IVerifyCodeRedisDAO {
	@Autowired
	@Qualifier("userRedisTemplate")
	@Override
	public void setTemplate(StringRedisTemplate template) {
		super.template = template;
	};
	
	@Override
	public void saveMobileRegisVfCode(String mobilePhone, String verifyCode) {
		valueOps.set(RedisKeyHelper.verifyCode(MOBILE_REGISTER, mobilePhone), verifyCode);
		
		// 设置过期时间为30分钟
		this.template.expire(RedisKeyHelper.verifyCode(MOBILE_REGISTER, mobilePhone), 30, TimeUnit.MINUTES);
	}

	@Override
	public String getMobileRegisVfCode(String mobilePhone) {
		String verifyCode = valueOps.get(RedisKeyHelper.verifyCode(MOBILE_REGISTER, mobilePhone));
//		this.template.delete(RedisKeyHelper.verifyCode(MOBILE_REGISTER, mobilePhone));
		return verifyCode;
	}

	@Override
	public void saveMobileBoundVfCode(String mobilePhone, String verifyCode) {
		valueOps.set(RedisKeyHelper.verifyCode(MOBILE_BOUND, mobilePhone), verifyCode);
		
		// 设置过期时间为30分钟
		this.template.expire(RedisKeyHelper.verifyCode(MOBILE_BOUND, mobilePhone), 30, TimeUnit.MINUTES);
	}

	@Override
	public String getMobileBoundVfCode(String mobilePhone) {
		String verifyCode = valueOps.get(RedisKeyHelper.verifyCode(MOBILE_BOUND, mobilePhone));
//		this.template.delete(RedisKeyHelper.verifyCode(MOBILE_BOUND, mobilePhone));
		return verifyCode;
	}
	
	@Override
	public void saveMobileBackPdToken(String mobilePhone, String verifyCode) {
		valueOps.set(RedisKeyHelper.verifyCode(MOBILE_BACK_PD, mobilePhone), verifyCode);
		
		// 修改密码页面，根据链接中的验证码获取账户信息
		valueOps.set(RedisKeyHelper.tokenAccount(verifyCode), mobilePhone);
		
		// 设置过期时间为30分钟
		this.template.expire(RedisKeyHelper.verifyCode(MOBILE_BACK_PD, mobilePhone), 30, TimeUnit.MINUTES);
		this.template.expire(RedisKeyHelper.tokenAccount(verifyCode), 30, TimeUnit.MINUTES);
	}

	@Override
	public String getMobileBackPdToken(String mobilePhone) {
		String token = valueOps.get(RedisKeyHelper.verifyCode(MOBILE_BACK_PD, mobilePhone));
		this.template.delete(RedisKeyHelper.verifyCode(MOBILE_BACK_PD, mobilePhone));
		return token;
	}

	@Override
	public void saveEmailBackPdToken(String tokenId, String loginAccount) {
		valueOps.set(RedisKeyHelper.tokenAccount(tokenId), loginAccount);
		
		// 设置过期时间为30分钟
		this.template.expire(RedisKeyHelper.tokenAccount(tokenId), 30, TimeUnit.MINUTES);
	}
	
	@Override
	public String getAccountByToken(String tokenId) {
		String account =  valueOps.get(RedisKeyHelper.tokenAccount(tokenId));
		//this.template.delete(RedisKeyHelper.tokenAccount(tokenId));
		return account;
	}

}
