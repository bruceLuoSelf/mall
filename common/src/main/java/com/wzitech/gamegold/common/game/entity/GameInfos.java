/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameInfos
 *	包	名：		com.wzitech.gamegold.common.game.entity
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 上午11:36:14
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 游戏信息列表
 * @author SunChengfei
 *
 */
@XmlRootElement(name="Games")
public class GameInfos {
	/**
	 * 游戏列表
	 */
	private List<GameInfo> gameList;

	/**
	 * @return the gameList
	 */
	@XmlElement(name="Game")
	public List<GameInfo> getGameList() {
		return gameList;
	}

	/**
	 * @param gameList the gameList to set
	 */
	public void setGameList(List<GameInfo> gameList) {
		this.gameList = gameList;
	}
	
}
