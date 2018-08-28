/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ISellerDBDAO
 *	包	名：		com.wzitech.gamegold.repository.dao
 *	项目名称：	gamegold-repository
 *	作	者：		HeJian
 *	创建时间：	2014-2-22
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-22 下午9:55:21
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.log.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.common.log.entity.LogInfo;

/**
 * 日志DB
 * @author ztjie
 *
 */
public interface ILogDBDAO extends IMyBatisBaseDAO<LogInfo, Long>{

}
