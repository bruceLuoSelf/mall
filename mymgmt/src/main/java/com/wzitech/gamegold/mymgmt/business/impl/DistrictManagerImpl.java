/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DistrictManagerImpl
 *	包	名：		com.wzitech.gamegold.mymgmt.business.impl
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午12:13:33
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.mymgmt.business.IDistrictManager;
import com.wzitech.gamegold.mymgmt.dao.ICityDBDAO;
import com.wzitech.gamegold.mymgmt.dao.IDistrictDBDAO;
import com.wzitech.gamegold.mymgmt.dao.IProvinceDBDAO;
import com.wzitech.gamegold.mymgmt.entity.CityEO;
import com.wzitech.gamegold.mymgmt.entity.DistrictEO;
import com.wzitech.gamegold.mymgmt.entity.ProvinceEO;

/**
 * 全国省市区查询
 * @author HeJian
 *
 */
@Component("districtManagerImpl")
public class DistrictManagerImpl extends AbstractBusinessObject implements IDistrictManager {
	@Autowired
	IProvinceDBDAO provinceDBDAO;
	
	@Autowired
	ICityDBDAO cityDBDAO;
	
	@Autowired
	IDistrictDBDAO districtDBDAO;
	
	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IDistrictManager#queryProvince()
	 */
	@Override
	public List<ProvinceEO> queryProvince() {
		return provinceDBDAO.queryProvince();
	}

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IDistrictManager#queryCity(int)
	 */
	@Override
	public List<CityEO> queryCity(int proId) {
		return cityDBDAO.queryCity(proId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IDistrictManager#queryDis(int)
	 */
	@Override
	public List<DistrictEO> queryDis(int cityId) {
		return districtDBDAO.queryDis(cityId);
	}

}
