/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ISHippingAddressDBDAO
 *	包	名：		com.wzitech.gamegold.mymgmt.dao
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-12
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-12 下午5:34:48
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.dao;

import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.mymgmt.entity.ShippingAddressEO;

/**
 * 收货地址DB DAO
 * @author HeJian
 *
 */
public interface IShippingAddressDBDAO extends IMyBatisBaseDAO<ShippingAddressEO, Long> {
	/**
	 * 设置默认地址
	 * @param addressId
	 * @param uid
	 * @return
	 */
	public int setDefaultAddress(String addressId, Long uid);
	
	/**
	 * 查询收获地址
	 * @return
	 */
	public List<ShippingAddressEO> queryAddress(Map<String, Object> queryMap);
	
}
