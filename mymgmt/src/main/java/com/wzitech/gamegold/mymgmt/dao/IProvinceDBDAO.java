/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IProvinceDBDAO
 *	包	名：		com.wzitech.gamegold.mymgmt.dao
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午12:15:18
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.dao;

import java.util.List;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.mymgmt.entity.ProvinceEO;

/**
 * 省DBDAO
 * @author HeJian
 *
 */
public interface IProvinceDBDAO extends IMyBatisBaseDAO<ProvinceEO, Long> {
	/**
	 * 获取所有省份
	 * @return
	 */
	public List<ProvinceEO> queryProvince();
}
