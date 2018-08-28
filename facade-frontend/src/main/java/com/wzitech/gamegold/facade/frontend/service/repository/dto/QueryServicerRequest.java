/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryServicesRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.repository.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 下午4:32:36
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 根据订单查询客服请求
 * @author HeJian
 *
 */
public class QueryServicerRequest extends AbstractServiceRequest{
	/**
	 * 游戏名
	 */
	private String gameName;
	
	/**
	 * 游戏所在区
	 */
	private String region;
	
	/**
	 * 游戏所在服
	 */
	private String server;
	
	/**
	 * 所在阵营
	 */
	private String gameRace;
	
	/**
	 * 需求金币数
	 */
	private int goldCount;
	
	/**
	 * 返回客服数
	 */
	private int size;

	/**
	 * 客服类型，接单客服或入驻客服
	 */
	private int servicerType;

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the gameRace
	 */
	public String getGameRace() {
		return gameRace;
	}

	/**
	 * @param gameRace the gameRace to set
	 */
	public void setGameRace(String gameRace) {
		this.gameRace = gameRace;
	}

	/**
	 * @return the goldCount
	 */
	public int getGoldCount() {
		return goldCount;
	}

	/**
	 * @param goldCount the goldCount to set
	 */
	public void setGoldCount(int goldCount) {
		this.goldCount = goldCount;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	public int getServicerType() {
		return servicerType;
	}

	public void setServicerType(int servicerType) {
		this.servicerType = servicerType;
	}
}
