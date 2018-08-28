/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		CurrentUserSession
 *	包	名：		com.wzitech.gamegold.facade.frontend.service
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-1-13
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-13 下午2:54:20
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;

import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 当前线程用户信息类
 * @author HeJian
 *
 */
public class CurrentUserSession {
	private static final ThreadLocal<UserInfoEO> currentUser = new NamedThreadLocal<UserInfoEO>("jgm-currentuser");
	
	/**
	 * 获取当前登录用户名
	 * @return
	 */
	public static String getUserLoginAccount() {
		UserInfoEO userInfo = currentUser.get();
		return (userInfo != null ? userInfo.getLoginAccount() : null);
	}

	/**
	 * 获取当前登录Uid
	 * @return
	 */
	public static long getUid() {
		UserInfoEO userInfo = currentUser.get();
		return (userInfo != null ? Long.valueOf(userInfo.getId().toString()).longValue() : null);
	}
	
	/**
	 * 获取当前登录用户类型
	 * @return
	 */
	public static int getUserType() {
		UserInfoEO userInfo = currentUser.get();
		return (userInfo != null ? userInfo.getUserType() : -1);
	}

	/**
	 * 设置当前登录用户
	 * @param userInfo
	 */
	public static void setUser(UserInfoEO userInfo) {
		currentUser.set(userInfo);
	}
	
	/**
	 * 返回当前用户
	 * @return
	 */
	public static UserInfoEO getUser(){
		return currentUser.get();
	}

	/**
	 * 判断当前线程是否登录
	 * @return
	 */
	public static boolean isSignedIn() {
		return StringUtils.isNotBlank(getUserLoginAccount());
	}

	/**
	 * 清楚当前线程用户信息
	 */
	public static void clean() {
		currentUser.set(null);
	}
}
