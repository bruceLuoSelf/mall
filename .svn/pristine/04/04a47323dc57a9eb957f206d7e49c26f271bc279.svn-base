/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		CityDBDAOImpl
 *	包	名：		com.wzitech.gamegold.mymgmt.dao.impl
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午12:20:48
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.mymgmt.dao.ICityDBDAO;
import com.wzitech.gamegold.mymgmt.entity.CityEO;

/**
 * 市
 * @author HeJian
 *
 */
@Repository("cityDBDAOImpl")
public class CityDBDAOImpl extends AbstractMyBatisDAO<CityEO, Long> implements ICityDBDAO {

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.dao.ICityDBDAO#queryCity(int)
	 */
	@Override
	public List<CityEO> queryCity(int proId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("proId", proId);
		
		return selectByMap(map);
	}

}