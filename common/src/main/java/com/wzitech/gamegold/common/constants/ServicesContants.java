/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ServicesContants
 *	包	名：		com.wzitech.gamegold.common.constants
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-13
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-13 上午10:57:14
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.constants;

/**
 * 服务使用常量定义类
 * 
 * @author SunChengfei
 * 
 */
public class ServicesContants {
	/**
	 * 用户验证码-请求认证HTTP HEAD标签
	 */
	public static final String SERVICE_REQUEST_AUTHKEY = "gamegold-authkey";
	
	/**
	 * 5173用户auth cookie-请求认证HTTP HEAD标签
	 */
	public static final String SERVICE_REQUEST_COOKIE = "5173_authkey";

	/**
	 * 5173后台用户auth cookie-请求认证HTTP HEAD标签
	 */
	public static final String BACK_SERVICE_REQUEST_COOKIE = "5173_back_authkey";

	/**
	 * 商品类型-游戏币
	 */
	public static final String GOODS_TYPE_GOLD = "游戏币";

	/**
	 * 商品类型-游戏币-ID
	 */
	public static final Long GOODS_TYPE_GOLD_ID = 1L;

	/**
	 * 商品类型-挑战书
	 */
	public static final String GOODS_TYPE_TZS = "挑战书";

	/**
	 * 类型-全部
	 */
	public static final String TYPE_ALL = "全部";
	
}
