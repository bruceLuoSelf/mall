/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IQueryServiceManager
 *	包	名：		com.wzitech.gamegold.repository.business
 *	项目名称：	gamegold-repository
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-19
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-19 下午2:44:24
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.repository.business;

import java.util.List;

import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 订单页查询客服
 * @author SunChengfei
 *
 */
public interface IQueryServiceManager {
	/**
	 * 根据订单，查询符合条件的客服
	 * @param gameName 游戏名
	 * @param region   游戏所在区
	 * @param server   游戏所在服
	 * @param gameRace 所在阵营
	 * @param goldCount 需求金币数
	 * @param size     返回客服数
	 * @return
	 */
	public List<UserInfoEO> randomServicer(String gameName, String region, 
			String server, String gameRace, int goldCount, int size);
}
