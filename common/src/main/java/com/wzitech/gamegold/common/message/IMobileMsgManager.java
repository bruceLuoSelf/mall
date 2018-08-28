/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IMobileMsgManager
 *	包	名：		com.wzitech.gamegold.common.message
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-15 下午12:17:04
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.message;

/**
 * 手机短信管理
 * @author SunChengfei
 *
 */
public interface IMobileMsgManager {
	/**
	 * 向手机终端发送短信
	 * @param mobilePhoneNub
	 * @param context
	 * @throws Exception 
	 */
	public void sendMessage(String mobilePhoneNub, String context) throws Exception;
}
