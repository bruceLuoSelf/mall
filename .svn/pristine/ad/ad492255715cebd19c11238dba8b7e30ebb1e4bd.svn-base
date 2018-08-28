/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TradeOrderManager
 *	包	名：		com.wzitech.gamegold.common.tradeorder
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-4-9
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-4-9 下午4:06:30
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.tradeorder;

import java.math.BigDecimal;

import com.wzitech.gamegold.common.tradeorder.client.GeneBizOffer;

/**
 * 5173寄售、担保发布单接口
 * 
 * @author SunChengfei
 * 
 */
public interface ITradeOrderManager {
	public GeneBizOffer queryTradeOrder(String gameName, String region,
			String server, String gameRace, int pageIndex, int pageSize,
			BigDecimal minPrice, BigDecimal maxPrice, int serviceType);
}
