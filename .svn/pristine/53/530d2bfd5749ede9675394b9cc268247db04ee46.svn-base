/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IRepositoryRedisDAO
 *	包	名：		com.wzitech.gamegold.repository.dao
 *	项目名称：	gamegold-repository
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午5:23:09
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.repository.dao;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.wzitech.gamegold.repository.entity.RepositoryInfo;

/**
 * @author SunChengfei
 *
 */
public interface IRepositoryRedisDAO {
	/**
	 * 保存每个游戏区服下对应的库存量
	 * @param repositoryInfo
	 * @param sum 库存总量
	 */
	public void saveRepositorySum(RepositoryInfo repositoryInfo, long sum);
	
	/**
	 * 添加每个游戏区服下对应的库存量
	 * @param repositoryInfo
	 */
	public void addRepositorySum(RepositoryInfo repositoryInfo);
	
	/**
	 * 减去每个游戏区服下对应的库存量
	 * @param repositoryInfo
	 */
	public void subRepositorySum(RepositoryInfo repositoryInfo);
	
	/**
	 * 删除该库存
	 * @param repositoryInfo
	 */
	public void deleteRepository(RepositoryInfo repositoryInfo);
	
	/**
	 * 返回满足金币数的客服Id
	 * @param gameName
	 * @param region
	 * @param server
	 * @param goldCount
	 * @return
	 */
	public List<String> queryServicerId(String gameName, String region, String server, String gameRace, int goldCount);

	public List<RepositoryInfo> queryRepositoryPrice(String gameName, String region,String server, String gameRace,String cacheKey);

	public void addRepositoryPrice(String gameName, String region,String server, String gameRace,List<RepositoryInfo> list,String cacheKey,long time,TimeUnit timeUnit);
}
