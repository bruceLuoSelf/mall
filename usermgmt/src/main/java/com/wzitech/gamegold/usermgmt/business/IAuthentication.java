/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IAuthentication
 *	包	名：		com.wzitech.gamegold.usermgmt.business
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午4:31:55
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.business;

/**
 * 身份验证管理
 * @author SunChengfei
 *
 */
public interface IAuthentication {
	/**
	 * 验证用户登录信息
	 * @param loginAccount
	 * @param userDBPassword
	 * @param requestPassword
	 * @return
	 */
	public boolean authenticate(String loginAccount, String userDBPassword, String requestPassword);
	
}
