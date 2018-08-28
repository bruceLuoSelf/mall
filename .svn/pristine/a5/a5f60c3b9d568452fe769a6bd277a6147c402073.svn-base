/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IOrderConfigQuery
 *	包	名：		com.wzitech.gamegold.order.business
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-25
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-25 下午7:32:36
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.business;

import java.util.List;

import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;


/**
 * @author SunChengfei
 *
 */
public interface IOrderConfigManager {
	/**
	 * 根据订单号查询配置库存
	 * @param orderId
	 * @return 库存信息，及关联的订单信息
	 */
	public List<ConfigResultInfoEO> orderConfigList(String orderId);

	public List<ConfigResultInfoEO> orderConfigList(String orderId, boolean lockMode);
	
	/**
	 * 根据配置Id，查询配置库存
	 * @param configId
	 * @return
	 */
	public ConfigResultInfoEO orderConfigById(Long configId);
	
	/**
	 * 更新库存状态
	 * @param configId
	 * @param state
	 * @return
	 */
	public void updateConfigState(Long configId, Integer state);
	
	/**
	 * 删除子订单
	 * @param id
	 */
	public void deleteConfig(Long id);
}
