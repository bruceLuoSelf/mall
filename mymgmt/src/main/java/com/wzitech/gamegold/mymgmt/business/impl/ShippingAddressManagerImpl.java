/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ShippingAddressManagerImpl
 *	包	名：		com.wzitech.gamegold.mymgmt.business.impl
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-12
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-12 下午5:48:52
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.mymgmt.business.IShippingAddressManager;
import com.wzitech.gamegold.mymgmt.dao.IShippingAddressDBDAO;
import com.wzitech.gamegold.mymgmt.entity.ShippingAddressEO;

/**
 * 收货地址管理实现
 * @author HeJian
 *
 */
@Component("shippingAddressManagerImpl")
public class ShippingAddressManagerImpl extends AbstractBusinessObject implements IShippingAddressManager{
	@Autowired
	IShippingAddressDBDAO addressDBDAO;
	
	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IShippingAddressManager#saveAddress(com.wzitech.gamegold.mymgmt.entity.ShippingAddressEO)
	 */
	@Override
	@Transactional
	public ShippingAddressEO saveAddress(ShippingAddressEO addressEO) throws SystemException {
		if (StringUtils.isBlank(addressEO.getProvince())){
			throw new SystemException(
					ResponseCodes.EmptyProvince.getCode(),ResponseCodes.EmptyProvince.getMessage());
		}
		if (StringUtils.isBlank(addressEO.getCity())){
			throw new SystemException(
					ResponseCodes.EmptyCity.getCode(),ResponseCodes.EmptyCity.getMessage());
		}
		if (StringUtils.isBlank(addressEO.getDistrict())){
			throw new SystemException(
					ResponseCodes.EmptyDistrict.getCode(),ResponseCodes.EmptyDistrict.getMessage());
		}
		if (StringUtils.isBlank(addressEO.getAddress())){
			throw new SystemException(
					ResponseCodes.EmptyAddress.getCode(),ResponseCodes.EmptyAddress.getMessage());
		}
		if (StringUtils.isBlank(addressEO.getReceiver())){
			throw new SystemException(
					ResponseCodes.EmptyReceiver.getCode(),ResponseCodes.EmptyReceiver.getMessage());
		}
		if (StringUtils.isBlank(addressEO.getMobileNumber())){
			throw new SystemException(
					ResponseCodes.EmptyPhone.getCode(),ResponseCodes.EmptyPhone.getMessage());
		}
		if(addressEO.getId() != null){
			addressDBDAO.update(addressEO);
		}else {
			addressDBDAO.insert(addressEO);
		}
		return addressEO;
	}

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IShippingAddressManager#updateAddress(com.wzitech.gamegold.mymgmt.entity.ShippingAddressEO)
	 */
	@Override
	@Transactional
	public void updateAddress(ShippingAddressEO addressEO) {
		addressDBDAO.update(addressEO);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IShippingAddressManager#deleteAddress(java.lang.String)
	 */
	@Override
	@Transactional
	public void deleteAddress(String addressId) {
		ShippingAddressEO addressEO = new ShippingAddressEO();
		addressEO.setId(Long.valueOf(addressId));
		addressEO.setDeleted(true);
		
		addressDBDAO.update(addressEO);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IShippingAddressManager#getAllAddress(java.lang.Long)
	 */
	@Override
	public List<ShippingAddressEO> getAllAddress(Long uid) {
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("uid", uid);
		return addressDBDAO.queryAddress(queryMap);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IShippingAddressManager#setDefaultAddress(java.lang.String, java.lang.Long)
	 */
	@Override
	@Transactional
	public void setDefaultAddress(String addressId, Long uid) {
		// 设置默认地址
		addressDBDAO.setDefaultAddress(addressId, uid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.mymgmt.business.IShippingAddressManager#getDefaultAddress(java.lang.Long)
	 */
	@Override
	public ShippingAddressEO getDefaultAddress(Long uid) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("uid", uid);
		queryMap.put("isDefault", true);
		List<ShippingAddressEO> addressList = addressDBDAO.queryAddress(queryMap);
		if(addressList != null && addressList.size() > 0){
			return  addressList.get(0);
		}
		return null;
	}
}