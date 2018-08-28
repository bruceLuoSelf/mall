/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryGameInfoResponse
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.game.dto
 *	项目名称：	    gamegold-facade-frontend
 *	作	者：		yubaihai
 *	创建时间：	    2014-6-28
 *	描	述：		
 *	更新纪录：	1. yubaihai 创建于 2014-6-28 下午3:43:33
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.game.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 查询友情提示
 * @author liuchanghua
 *
 */
public class QueryWarningRequest extends AbstractServiceRequest{

	/**
	 * 游戏名
	 */
	private String gameName;

	private String goodsTypeName;

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}
}
