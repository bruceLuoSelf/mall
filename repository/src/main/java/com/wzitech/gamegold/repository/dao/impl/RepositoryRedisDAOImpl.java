/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		RepositoryRedisDAOImpl
 *	包	名：		com.wzitech.gamegold.repository.dao.impl
 *	项目名称：	gamegold-repository
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午5:23:26
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.repository.dao.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author SunChengfei
 *
 */
@Repository
public class RepositoryRedisDAOImpl extends AbstractRedisDAO<RepositoryInfo> implements IRepositoryRedisDAO{

	JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

	@Autowired
	@Qualifier("userRedisTemplate")
	@Override
	public void setTemplate(StringRedisTemplate template) {
		super.template = template;
	};
	
	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO#saveRepositorySum(com.wzitech.gamegold.repository.entity.RepositoryInfo)
	 */
	@Override
	public void saveRepositorySum(RepositoryInfo repositoryInfo, long sum) {
		String key = RedisKeyHelper.gameRepository(repositoryInfo.getGameName(), 
				repositoryInfo.getRegion(), repositoryInfo.getServer(), repositoryInfo.getGameRace());
		zSetOps.add(key, String.valueOf(repositoryInfo.getServicerId()), sum);
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO#addRepositorySum(com.wzitech.gamegold.repository.entity.RepositoryInfo)
	 */
	@Override
	public void addRepositorySum(RepositoryInfo repositoryInfo) {
		String key = RedisKeyHelper.gameRepository(repositoryInfo.getGameName(), 
				repositoryInfo.getRegion(), repositoryInfo.getServer(), repositoryInfo.getGameRace());
		zSetOps.incrementScore(key, String.valueOf(repositoryInfo.getServicerId()), repositoryInfo.getSellableCount().doubleValue());
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO#subRepositorySum(com.wzitech.gamegold.repository.entity.RepositoryInfo)
	 */
	@Override
	public void subRepositorySum(RepositoryInfo repositoryInfo) {
		String key = RedisKeyHelper.gameRepository(repositoryInfo.getGameName(), 
				repositoryInfo.getRegion(), repositoryInfo.getServer(), repositoryInfo.getGameRace());
		zSetOps.incrementScore(key, String.valueOf(repositoryInfo.getServicerId()), 0-repositoryInfo.getSellableCount().doubleValue());
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO#deleteRepository(com.wzitech.gamegold.repository.entity.RepositoryInfo)
	 */
	@Override
	public void deleteRepository(RepositoryInfo repositoryInfo) {
		String key = RedisKeyHelper.gameRepository(repositoryInfo.getGameName(), 
				repositoryInfo.getRegion(), repositoryInfo.getServer(), repositoryInfo.getGameRace());
		zSetOps.remove(key, String.valueOf(repositoryInfo.getServicerId()));
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO#queryServicerId(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public List<String> queryServicerId(String gameName, String region,
			String server, String gameRace, int goldCount) {
		String key = RedisKeyHelper.gameRepository(gameName, region, server, gameRace);
//		listOps.size(key)>0{
//
//			listOps.range(key,0,-1);
//		}
//		else
//		{
//
//		}
		// 查询最大库存
		// score从小到大排序，返回带score信息
		Set<TypedTuple<String>> sortByScore = zSetOps.rangeWithScores(key, 0, zSetOps.size(key));
		if(sortByScore == null || sortByScore.size() == 0){
			return null;
		}
		@SuppressWarnings("rawtypes")
		TypedTuple[] sortByScoreArray = sortByScore.toArray(new TypedTuple[sortByScore.size()]);
		Double maxScore = sortByScoreArray[sortByScoreArray.length-1].getScore();
		
		// 查询满足条件的库存，并返回相应的客服id
		// 按score从大到小
		Set<String> rangByScore = zSetOps.reverseRangeByScore(key, goldCount, maxScore);
		if(rangByScore == null || rangByScore.size() == 0){
			return null;
		}
		return new ArrayList<String>(rangByScore);
	}

	@Override
	public List<RepositoryInfo> queryRepositoryPrice(String gameName, String region,String server, String gameRace,String cacheKey){
		String key=RedisKeyHelper.repositoryPrice(gameName, region, server, gameRace,cacheKey);
		if(valueOps.size(key)>0)
		{
			String value=valueOps.get(key);
			JavaType javaType=jsonMapper.createCollectionType(ArrayList.class, RepositoryInfo.class);
			List<RepositoryInfo> repositoryList= jsonMapper.fromJson(value, javaType);
			return  repositoryList;
		}
		return null;
	}

	@Override
	public void addRepositoryPrice(String gameName, String region,String server, String gameRace,List<RepositoryInfo> list,String cacheKey,long time,TimeUnit timeUnit){
		String key=RedisKeyHelper.repositoryPrice(gameName, region, server, gameRace,cacheKey);
		if(list!=null&&list.size()>0)
		{
			String json=jsonMapper.toJson(list);
			valueOps.set(key,json,time,timeUnit);
		}
	}
}