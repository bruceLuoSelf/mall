/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ShippingAddressDBDAOImpl
 *	包	名：		com.wzitech.gamegold.mymgmt.dao.impl
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-12
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-12 下午5:42:16
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.mymgmt.dao.IShippingAddressDBDAO;
import com.wzitech.gamegold.mymgmt.entity.ShippingAddressEO;

/**
 * 收货地址DBDAO实现
 * @author HeJian
 *
 */
@Repository("shippingAddressDBDAOImpl")
public class ShippingAddressDBDAOImpl extends AbstractMyBatisDAO<ShippingAddressEO, Long> implements IShippingAddressDBDAO {
	
	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.dao.IShippingAddressDBDAO#setDefaultAddress(java.lang.String, java.lang.Long)
	 */
	@Override
	public int setDefaultAddress(String addressId, Long uid) {
		/**
		 * 设置默认收货地址，这里的isDefault字段不放入mapper配置batchUpdate中，单独作为一个方法。
		 * 因为放入batchUpdate中时，当更新其他字段时，isDefault默认为false，
		 * 但实际在数据库中可能为true，有可能被默认更改。
		 */
		// 先清空其他的默认地址
		ShippingAddressEO addressEO = new ShippingAddressEO();
		addressEO.setDefault(false);
		addressEO.setUid(uid);
		
		List<ShippingAddressEO> list = new ArrayList<ShippingAddressEO>();
		list.add(addressEO);
		
		// 设置默认地址
		ShippingAddressEO addressEO2 = new ShippingAddressEO();
		addressEO2.setDefault(true);
		addressEO2.setId(Long.valueOf(addressId));
		list.add(addressEO2);
		
		return this.getSqlSession().update(
				this.mapperNamespace + "." + "setDefalutAddress", list);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.dao.IShippingAddressDBDAO#queryAddress(java.util.Map)
	 */
	@Override
	public List<ShippingAddressEO> queryAddress(Map<String, Object> queryMap) {
		if(queryMap == null){
			queryMap = new HashMap<String, Object>();
		}
		queryMap.put("isDeleted", false);
		
		return selectByMap(queryMap);
	}
}