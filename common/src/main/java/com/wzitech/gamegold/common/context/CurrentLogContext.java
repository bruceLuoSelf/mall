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

import org.springframework.core.NamedThreadLocal;

import com.wzitech.gamegold.common.utils.UUIDUtils;

/**
 * 当前线程日志ID信息类
 * @author SunChengfei
 *
 */
public class CurrentLogContext {
	private static final ThreadLocal<String> currentThreadId = new NamedThreadLocal<String>("jgm-currentthreadid");
	
	/**
	 * 获取当前线程日志ID
	 * @return
	 */
	public static String getThreadId() {
		String threadId = currentThreadId.get();
		if(threadId == null){
			threadId = UUIDUtils.getUUID();
			currentThreadId.set(threadId);
		}
		return threadId; 
	}

	/**
	 * 清楚当前线程日志ID
	 */
	public static void clean() {
		currentThreadId.set(null);
	}
}
