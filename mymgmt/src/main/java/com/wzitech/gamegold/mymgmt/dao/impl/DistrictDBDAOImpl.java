/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DistrictDBDAOImpl
 *	包	名：		com.wzitech.gamegold.mymgmt.dao.impl
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午12:19:01
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.mymgmt.dao.IDistrictDBDAO;
import com.wzitech.gamegold.mymgmt.entity.DistrictEO;

/**
 * 区
 * @author HeJian
 *
 */
@Repository("districtDBDAOImpl")
public class DistrictDBDAOImpl extends AbstractMyBatisDAO<DistrictEO, Long> implements IDistrictDBDAO{

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.dao.IDistrictDBDAO#queryDis(int)
	 */
	@Override
	public List<DistrictEO> queryDis(int cityId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cityId", cityId);
		
		return selectByMap(map);
	}

}
