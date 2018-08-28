/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameRaceAndMoney
 *	包	名：		com.wzitech.gamegold.common.game.entity
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午9:24:50
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author SunChengfei
 *
 */
@XmlRootElement(name="Game")
public class GameRaceAndMoney {
	/**
	 * 游戏Id
	 */
	private String id;
	
	/**
	 * 游戏阵营list
	 */
	private List<GameRaceInfo> gameRaceList;
	
	/**
	 * 游戏币list
	 */
	private List<GameMoneyInfo> gameMoneyList;

	@XmlElement(name="id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the gameRaceList
	 */
	@XmlElementWrapper(name = "GameRaces", required=false)
	@XmlElement(name="GameRace")
	public List<GameRaceInfo> getGameRaceList() {
		return gameRaceList;
	}

	/**
	 * @param gameRaceList the gameRaceList to set
	 */
	public void setGameRaceList(List<GameRaceInfo> gameRaceList) {
		this.gameRaceList = gameRaceList;
	}

	/**
	 * @return the gameMoneyList
	 */
	@XmlElementWrapper(name = "GameMoneys", required=false)
	@XmlElement(name="GameMoney")
	public List<GameMoneyInfo> getGameMoneyList() {
		return gameMoneyList;
	}

	/**
	 * @param gameMoneyList the gameMoneyList to set
	 */
	public void setGameMoneyList(List<GameMoneyInfo> gameMoneyList) {
		this.gameMoneyList = gameMoneyList;
	}
	
}
