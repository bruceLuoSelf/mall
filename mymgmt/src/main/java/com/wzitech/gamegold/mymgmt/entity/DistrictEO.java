/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DistrictEO
 *	包	名：		com.wzitech.gamegold.mymgmt.entity
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-13
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-13 下午1:48:41
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 区
 * @author HeJian
 *
 */
public class DistrictEO extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 行政区名称
	 */
	private String disName;
	
	/**
	 * 所属城市Id
	 */
	private int cityId;
	
	/**
	 * 区排名
	 */
	private int disSort;

	/**
	 * @return the disName
	 */
	public String getDisName() {
		return disName;
	}

	/**
	 * @param disName the disName to set
	 */
	public void setDisName(String disName) {
		this.disName = disName;
	}

	/**
	 * @return the cityId
	 */
	public int getCityId() {
		return cityId;
	}

	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	/**
	 * @return the disSort
	 */
	public int getDisSort() {
		return disSort;
	}

	/**
	 * @param disSort the disSort to set
	 */
	public void setDisSort(int disSort) {
		this.disSort = disSort;
	}
	
}
