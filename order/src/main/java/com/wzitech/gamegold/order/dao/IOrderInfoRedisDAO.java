/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IOrderInfoRedisDAO
 *	包	名：		com.wzitech.gamegold.order.dao
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午10:38:48
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao;

import java.util.List;

import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * @author SunChengfei
 *
 */
public interface IOrderInfoRedisDAO {
	/**
	 * 保存订单信息(每个用户保存最新5笔订单)
	 * @param orderInfoEO
	 */
	public void saveOrder(OrderInfoEO orderInfoEO);
	
	/**
	 * 获取每个用户的最新5笔订单
	 * @param uid
	 * @return
	 */
	public List<OrderInfoEO> queryOrder(String uid);
}
