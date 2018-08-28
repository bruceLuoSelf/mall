/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ProvinceDBDAOImpl
 *	包	名：		com.wzitech.gamegold.mymgmt.dao.impl
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午12:16:41
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.mymgmt.dao.IProvinceDBDAO;
import com.wzitech.gamegold.mymgmt.entity.ProvinceEO;

/**
 * 省DBDAO实现
 * @author HeJian
 *
 */
@Repository("provinceDBDAOImpl")
public class ProvinceDBDAOImpl extends AbstractMyBatisDAO<ProvinceEO, Long> implements IProvinceDBDAO{

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.dao.IProvinceDBDAO#queryProvince()
	 */
	@Override
	public List<ProvinceEO> queryProvince() {
		return selectByNativeSql("SELECT * FROM \"TBL_PROVINCE\"");
	}

}
