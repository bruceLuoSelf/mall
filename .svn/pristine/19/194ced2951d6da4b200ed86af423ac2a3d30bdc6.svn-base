/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameRaceAndMoneys
 *	包	名：		com.wzitech.gamegold.common.game.entity
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-24 下午9:24:50
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Games")
public class GameRaceAndMoneys {
	/**
	 * 阵营、游戏币信息列表
	 */
	private List<GameRaceAndMoney> raceMoneyList;

	@XmlElement(name="Game")
	public List<GameRaceAndMoney> getRaceMoneyList() {
		return raceMoneyList;
	}

	public void setRaceMoneyList(List<GameRaceAndMoney> raceMoneyList) {
		this.raceMoneyList = raceMoneyList;
	}
	
}
