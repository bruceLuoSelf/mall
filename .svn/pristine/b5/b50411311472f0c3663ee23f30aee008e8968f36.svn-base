/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IOrderInfoManager
 *	包	名：		com.wzitech.gamegold.order.business
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-17
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-17 下午3:32:36
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.order.entity.OrderInfoEO;

/**
 * 订单状态管理接口
 * @author ztjie
 *
 */
public interface IOrderStateManager {
	
	/**
	 * 
	 * <p>修改订单状态</p> 
	 * @author Think
	 * @date 2014-3-9 上午7:57:29
	 * @param orderId
	 * @param orderState
	 * @return
	 * @see
	 */
	public OrderInfoEO changeOrderState(String orderId, OrderState orderState) throws SystemException;

	/**
	 * 
	 * <p>修改订单配置信息的状态</p> 
	 * @author Think
	 * @date 2014-3-16 下午10:25:23
	 * @param id
	 * @param waitdelivery
	 * @return 
	 * @see
	 */
	public void changeConfigState(Long id, OrderState waitdelivery) throws SystemException;


}
