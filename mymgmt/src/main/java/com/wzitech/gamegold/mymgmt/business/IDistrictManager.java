/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IDistrictManager
 *	包	名：		com.wzitech.gamegold.mymgmt.business
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午12:12:25
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.business;

import java.util.List;

import com.wzitech.gamegold.mymgmt.entity.CityEO;
import com.wzitech.gamegold.mymgmt.entity.DistrictEO;
import com.wzitech.gamegold.mymgmt.entity.ProvinceEO;

/**
 * 全国省市区查询
 * @author HeJian
 *
 */
public interface IDistrictManager {
	/**
	 * 查询省份
	 * @return
	 */
	public List<ProvinceEO> queryProvince();
	
	/**
	 * 查询城市
	 * @param proId
	 * @return
	 */
	public List<CityEO> queryCity(int proId);
	
	/**
	 * 查询行政区
	 * @param cityId
	 * @return
	 */
	public List<DistrictEO> queryDis(int cityId);
}
