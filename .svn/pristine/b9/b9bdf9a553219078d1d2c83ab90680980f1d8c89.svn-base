/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ISellerManager
 *	包	名：		com.wzitech.gamegold.repository.business
 *	项目名称：	gamegold-repository
 *	作	者：		HeJian
 *	创建时间：	2014-2-22
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-22 下午9:58:04
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.log.business;

import java.util.Map;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ModuleType;
import com.wzitech.gamegold.common.log.entity.LogInfo;

/**
 * 日志管理接口
 * @author ztjie
 *
 */
public interface ILogManager {
	/**
	 * 添加日志
	 * @param module
	 * @param operateInfo
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	LogInfo add(ModuleType module, String operateInfo, IUser user) throws SystemException;
	
	/**
	 * 根据条件分页查询卖家信息
	 * @param queryMap
	 * @param limit
	 * @param start
	 * @param sortBy
	 * @param isAsc
	 * @return
	 * @throws SystemException
	 */
	GenericPage<LogInfo> queryLog(Map<String, Object> queryMap, int limit, int start,
			String sortBy, boolean isAsc)throws SystemException;

}
