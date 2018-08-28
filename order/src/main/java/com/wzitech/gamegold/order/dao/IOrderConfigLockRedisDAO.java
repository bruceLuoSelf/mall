/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IOrderIdRedisDAO
 *	包	名：		com.wzitech.gamegold.order.dao
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-14 下午3:49:03
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao;

public interface IOrderConfigLockRedisDAO {
	
	public Boolean lock(String configResultId);
	
	public Boolean unlock(String configResultId);
}
