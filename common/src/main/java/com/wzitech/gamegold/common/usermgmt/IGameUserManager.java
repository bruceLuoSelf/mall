/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IGameUserManager
 *	包	名：		com.wzitech.gamegold.common.usermgmt
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午4:51:20
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.usermgmt;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.wzitech.gamegold.common.usermgmt.entity.GameUserInfo;

/**
 * 5173注册用户管理
 * @author SunChengfei
 *
 */
public interface IGameUserManager {
	/**
	 * 解析5173网站cookie，返回用户Id及账户名
	 * @param cookie
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public GameUserInfo analysisCookie(String cookie) throws ClientProtocolException, IOException;
	
	/**
	 * 根据用户Id获取5173用户详细信息
	 * @param uid
	 * @return
	 */
	public GameUserInfo getUserById(Long uid);
}
