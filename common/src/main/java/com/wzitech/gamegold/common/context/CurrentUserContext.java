/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		CurrentUserSession
 *	包	名：		com.wzitech.chinabeauty.facade.frontend
 *	项目名称：	chinabeauty-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2013-9-27
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2013-9-27 上午10:30:22
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NamedThreadLocal;

import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.expection.UserNotLoginException;

/**
 * 当前线程用户信息类
 * @author SunChengfei
 *
 */
public class CurrentUserContext {
	private static final ThreadLocal<IUser> currentUser = new NamedThreadLocal<IUser>("jgm-currentuser");
	
	/**
	 * 获取当前登录用户名
	 * @return
	 */
	public static String getUserLoginAccount() {
		IUser userInfo = CurrentUserContext.getUser();
		return (userInfo != null ? userInfo.getLoginAccount() : null);
	}

	/**
	 * 获取当前登录Uid
	 * @return
	 */
	public static Long getUid() {
		IUser userInfo = CurrentUserContext.getUser();
		return (userInfo != null ? Long.valueOf(userInfo.getId().toString()).longValue() : null);
	}
	
	/**
	 * 获取当前登陆5173用户
	 * @return
	 */
	public static String getUidStr(){
		IUser userInfo = CurrentUserContext.getUser();
		return (userInfo != null ? userInfo.getUid(): null);
	}
	
	/**
	 * 获取当前登录用户类型
	 * @return
	 */
	public static int getUserType() {
		IUser userInfo = CurrentUserContext.getUser();
		return (userInfo != null ? userInfo.getUserType() : -1);
	}

	/**
	 * 设置当前登录用户
	 * @param userInfo
	 */
	public static void setUser(IUser userInfo) {
		currentUser.set(userInfo);
	}
	
	/**
	 * 返回当前用户
	 * @return
	 */
	public static IUser getUser(){
		IUser userInfo = currentUser.get();
		return userInfo;
	}
	
	/**
	 * 返回当前用户
	 * @return
	 */
	public static IUser getUserForAction(){
		IUser userInfo = currentUser.get();
		if(userInfo==null){
			throw new UserNotLoginException();
		}
		return userInfo;
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
