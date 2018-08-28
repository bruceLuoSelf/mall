/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IShippingAddressManager
 *	包	名：		com.wzitech.gamegold.mymgmt.business
 *	项目名称：	gamegold-mymgmt
 *	作	者：		HeJian
 *	创建时间：	2014-1-12
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-12 下午5:47:20
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.mymgmt.business;

import java.util.List;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.mymgmt.entity.ShippingAddressEO;

/**
 * 收货地址管理接口
 * @author HeJian
 *
 */
public interface IShippingAddressManager {
	/**
	 * 保存收货地址
	 * @param addressEO
	 * @throws BusinessException 
	 */
	public ShippingAddressEO saveAddress(ShippingAddressEO addressEO) throws SystemException;
	
	/**
	 * 更新收货地址
	 * @param addressEO
	 */
	public void updateAddress(ShippingAddressEO addressEO);
	
	/**
	 * 删除收货地址
	 * @param addressId
	 */
	public void deleteAddress(String addressId);
	
	/**
	 * 获取所有的收货地址
	 * @param uid
	 * @return
	 */
	public List<ShippingAddressEO> getAllAddress(Long uid);
	
	/**
	 * 设置默认地址
	 * @param addressId
	 * @param uid
	 */
	public void setDefaultAddress(String addressId, Long uid);
	
	/**
	 * 获取默认地址
	 * @return
	 */
	public ShippingAddressEO getDefaultAddress(Long uid);
}
