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

import java.util.List;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.common.game.entity.GameDetailInfo;
import com.wzitech.gamegold.common.game.entity.GameInfo;
import com.wzitech.gamegold.common.game.entity.GameRaceInfo;

/**
 * @author Administrator
 *
 */
public class QueryGameInfoResponse extends AbstractServiceResponse{
	/**
	 * 运营商列表
	 */
	private  List<String> companielist;
	/**
	 * 游戏列表
	 */
	private List<GameInfo> gameList;
	/**
	 * 
	 */
	private GameDetailInfo gameDetailInfo;

	/**
	 * @return the gameRaceInfoList ADD
	 */
	private List<GameRaceInfo> gameRaceInfoList;


	public List<String> getCompanielist() {
		return companielist;
	}

	/**
	 * @param companielist the companielist to set
	 */
	public void setCompanielist(List<String> companielist) {
		this.companielist = companielist;
	}

	/**
	 * @return the gameList
	 */
	public List<GameInfo> getGameList() {
		return gameList;
	}

	/**
	 * @param gameList the gameList to set
	 */
	public void setGameList(List<GameInfo> gameList) {
		this.gameList = gameList;
	}

	/**
	 * @return the gameDetailInfo
	 */
	public GameDetailInfo getGameDetailInfo() {
		return gameDetailInfo;
	}

	/**
	 * @param gameDetailInfo the gameDetailInfo to set
	 */
	public void setGameDetailInfo(GameDetailInfo gameDetailInfo) {
		this.gameDetailInfo = gameDetailInfo;
	}

	public List<GameRaceInfo> getGameRaceInfoList() {
		return gameRaceInfoList;
	}

	public void setGameRaceInfoList(List<GameRaceInfo> gameRaceInfoList) {
		this.gameRaceInfoList = gameRaceInfoList;
	}
}
