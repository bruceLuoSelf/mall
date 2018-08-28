/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameInfo
 *	包	名：		com.wzitech.gamegold.common.game.entity
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午2:22:49
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 游戏详细信息
 * @author SunChengfei
 *
 */
@XmlRootElement(name="Game")
public class GameDetailInfo{
	/**
	 * 游戏Id
	 */
	private String id;
	
	/**
	 * 游戏名称
	 */
	private String name;
	
	/**
	 * 游戏名全拼
	 */
	private String spell;
	
	/**
	 * 区、服
	 */
	private List<GameArea> gameAreaList;

	/**
	 * @return the id
	 */
	@XmlElement(name="id")
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the spell
	 */
	@XmlElement(name="Spell")
	public String getSpell() {
		return spell;
	}

	/**
	 * @param spell the spell to set
	 */
	public void setSpell(String spell) {
		this.spell = spell;
	}

	/**
	 * @return the gameAreaList
	 */
	@XmlElementWrapper(name = "GameAreas")
	@XmlElement(name="GameArea")
	public List<GameArea> getGameAreaList() {
		return gameAreaList;
	}

	/**
	 * @param gameAreaList the gameAreaList to set
	 */
	public void setGameAreaList(List<GameArea> gameAreaList) {
		this.gameAreaList = gameAreaList;
	}

}
