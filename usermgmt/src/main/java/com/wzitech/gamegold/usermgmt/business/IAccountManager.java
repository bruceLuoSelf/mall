/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IAccountManager
 *	包	名：		com.wzitech.gamegold.usermgmt.business
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午4:28:37
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.business;

import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 *  帐号管理
 * @author SunChengfei
 *
 */
public interface IAccountManager {
	/**
	 * 注册
	 * @return
	 * @param userInfoEO
	 */
	public UserInfoEO register(UserInfoEO userInfoEO);
	
	/**
	 * 修改密码
	 * @param userInfoEO
	 * @param newPassword
	 */
	public void modifyPassword(UserInfoEO userInfoEO, String newPassword);
}
