/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryServiceManagerImpl
 *	包	名：		com.wzitech.gamegold.repository.business.impl
 *	项目名称：	gamegold-repository
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-19
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-19 下午2:44:52
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.repository.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.servicer.IServicerOrderManager;
import com.wzitech.gamegold.repository.business.IQueryServiceManager;
import com.wzitech.gamegold.repository.dao.IRepositoryDBDAO;
import com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 订单页查询客服实现类
 * @author SunChengfei
 *
 */
@Component
public class QueryServiceManagerImpl extends AbstractBusinessObject implements IQueryServiceManager {
	@Autowired
	IRepositoryRedisDAO repositoryRedisDAO;
	
	@Autowired
	IRepositoryDBDAO repositoryDBDAO;
	
	@Autowired
	IServicerOrderManager servicerOrderManager;
	
	@Autowired
	IUserInfoManager userInfoManager;
	
	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.repository.business.IQueryServiceManager#randomServicer(java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public List<UserInfoEO> randomServicer(String gameName, String region,
			String server, 	String gameRace, int goldCount, int size) {
		// 查询符合该游戏及游戏币的库存所属客服
		List<String> servicersIdList = repositoryRedisDAO.queryServicerId(gameName, region, server, gameRace, goldCount);
		
		// 没有符合条件的库存
		if(servicersIdList == null || servicersIdList.size() == 0){
			logger.debug("查找库存，redis未找到游戏类目为:{}/{}/{}/{}，需求游戏币数{}，符合条件的客服，从数据库中查找。", new Object[]{gameName,
					region,server,gameName,goldCount});
			
			// 从数据库中查询符合条件的客服Id
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("gameName", gameName);
			queryMap.put("region", region);
			queryMap.put("server", server);
			queryMap.put("gameRace", gameRace);
			queryMap.put("goldCount",goldCount);
			
			List<RepositoryInfo> servicersDBList = repositoryDBDAO.selectByStatement(".queryServicer",queryMap);
			
			if(servicersDBList == null || servicersDBList.size() == 0){
				logger.debug("查找库存，db中未找到游戏类目为:{}/{}/{}/{}，需求游戏币数{}，符合条件的客服。开始查找该游戏类目下，空闲的客服。", new Object[]{gameName,
						region,server,gameName,goldCount});
				// redis中根据忙碌程度从小到大
				servicersIdList = servicerOrderManager.sortServicer(gameName, region, server, gameRace);
				
				if(servicersIdList == null || servicersIdList.size() == 0){
					// 如果redis中未找到客服，从数据库中查找该游戏类目下的客服，对库存数没有限制
					logger.debug("该游戏类目下{}/{}/{}/{}，空闲的客服，redis中未找到，将游戏币数限制设为0，从数据库中查找该类目下客服。", new Object[]{gameName,
							region,server,gameName});
					queryMap.put("goldCount",0);
					servicersDBList = repositoryDBDAO.selectByStatement(".queryServicer",queryMap);
					if(servicersDBList != null && servicersDBList.size() > 0){
						servicersIdList = new ArrayList<String>();
						for (RepositoryInfo repositoryInfo : servicersDBList) {
							servicersIdList.add(String.valueOf(repositoryInfo.getServicerId()));
						}
					}
				}
			}else {
				logger.debug("查找库存，db中找到游戏类目为:{}/{}/{}/{}，需求游戏币数{}，符合条件的客服。", new Object[]{gameName,
						region,server,gameName,goldCount});
				servicersIdList = new ArrayList<String>();
				for (RepositoryInfo repositoryInfo : servicersDBList) {
					servicersIdList.add(String.valueOf(repositoryInfo.getServicerId()));
				}
			}
		}
		
		if(servicersIdList == null || servicersIdList.size() == 0){
			return null;
		}
		
		//忙碌排序，从小到大。
		List<String> servicerIds = new ArrayList<String>();
		List<String> servicersIdBusyList  =  servicerOrderManager.sortServicer();
		if(servicersIdBusyList == null || servicersIdBusyList.size() == 0){
			servicerIds = servicersIdList; 
		 }else{
		      for (int i = 0; i < servicersIdBusyList.size(); i++) {
				for (int j = 0; j < servicersIdList.size(); j++) {
					if(servicersIdBusyList.get(i).equals(servicersIdList.get(j))){
						servicerIds.add(servicersIdList.get(j));
						break;
					}
				}
			}
		 }

//		// 根据客服Id，查询客服信息
//		List<UserInfoEO> userList = userInfoManager.userInfoList(servicerIds);
//		if(userList == null || userList.size() == 0){
//			return null;
//		}
		// 客服被禁用，不显示；且类型必须为客服
		List<UserInfoEO> userList = new ArrayList<UserInfoEO>();
		for (int i=0; i<servicerIds.size(); i++) {
			UserInfoEO userInfoEO = userInfoManager.findDBUserById(servicerIds.get(i));
			if(userInfoEO == null || (userInfoEO.getIsDeleted() != null && userInfoEO.getIsDeleted() == true)
					|| (userInfoEO.getUserType() != UserType.AssureService.getCode())){
				continue;
			}
			userList.add(userInfoEO);
		}
		
		// list随机排序
//		Collections.shuffle(userList);
		
		// 返回客服数默认为4
		if(size == 0){
			size = 4;
		}
		if(size >= userList.size()){
			size = userList.size();
		}
		
		userList = userList.subList(0, size);
		
		return userList;
	}

}
