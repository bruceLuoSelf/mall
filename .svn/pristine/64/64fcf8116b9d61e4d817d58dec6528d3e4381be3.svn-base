/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AuthenticationImpl
 *	包	名：		com.wzitech.gamegold.usermgmt.business.impl
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午4:33:12
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.business.impl;

import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.security.Base64;
import com.wzitech.chaos.framework.server.common.security.Digests;
import com.wzitech.gamegold.usermgmt.business.IAuthentication;

/**
 * @author SunChengfei
 *
 */
@Component("authenticationImpl")
public class AuthenticationImpl extends AbstractBusinessObject implements IAuthentication{

	@Override
	public boolean authenticate(String loginAccount, String userDBPassword,
			String requestPassword) {
		// 先对请求密码做散列，用户登录名做散列Salt值
		// 然后使用Base64编码
		String passwordWithCrypt = Base64.encodeBase64Binrary(
				Digests.sha1(requestPassword.getBytes(), loginAccount.getBytes()));
		return passwordWithCrypt.compareToIgnoreCase(userDBPassword) == 0;
	}

}
