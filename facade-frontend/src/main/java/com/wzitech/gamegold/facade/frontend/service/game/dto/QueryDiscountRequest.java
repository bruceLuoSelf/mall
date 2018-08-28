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
 * 查询游戏信息
 * @author HeJian
 *
 */
public class QueryDiscountRequest extends AbstractServiceRequest{

	/**
	 * 运营商名称
	 */
    private String companie;
    
    private String gameid;

	/**
	 * @return the companie
	 */
	public String getCompanie() {
		return companie;
	}

	/**
	 * @param companie the companie to set
	 */
	public void setCompanie(String companie) {
		this.companie = companie;
	}

	/**
	 * @return the gameid
	 */
	public String getGameid() {
		return gameid;
	}

	/**
	 * @param gameid the gameid to set
	 */
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
    

	
}
