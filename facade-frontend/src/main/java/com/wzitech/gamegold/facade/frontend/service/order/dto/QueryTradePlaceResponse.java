/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryTradePlaceResponse
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		Wengwei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. Wengwei 创建于 2014-2-21 下午5:11:33
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.order.entity.GameConfigEO;

/**
 * 查询游戏交易地址请求DTO响应
 * @author Wengwei
 *
 */
public class QueryTradePlaceResponse extends AbstractServiceResponse{
	
	/**
	 * 返回交易地址
	 */
	private GameConfigEO place;

	/**
	 * @return the place
	 */
	public GameConfigEO getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(GameConfigEO place) {
		this.place = place;
	}

	
}
