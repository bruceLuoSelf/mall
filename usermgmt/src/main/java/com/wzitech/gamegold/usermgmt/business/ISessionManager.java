/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ISessionManager
 *	包	名：		com.wzitech.gamegold.usermgmt.business
 *	项目名称：	gamegold-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午4:34:43
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.business;

import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * Session管理
 * @author SunChengfei
 *
 */
public interface ISessionManager {
	/**
	 * 创建用户Session
	 * @param uid
	 * @return
	 */
	public String createSession(String uid);

    /**
     * 创建用户Session
     * @param uid
     * @param timeout
     * @return
     */
    public String createSession(String uid, int timeout);
	
	/**
	 * 删除Session
	 * @param uid
	 */
	public void removeSession(String uid);
	
	/**
	 * 根据认证码获取Session中的用户信息
	 * @param authkey
	 * @return
	 */
	public UserInfoEO getUserByAuthkey(String authkey);
}
