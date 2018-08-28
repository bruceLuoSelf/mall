/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameArea
 *	包	名：		com.wzitech.gamegold.common.game.entity
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 上午11:32:44
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 游戏区信息
 * @author SunChengfei
 *
 */
@XmlRootElement(name="GameArea")
public class GameArea {
	/**
	 * 区Id
	 */
	private String id;
	
	/**
	 * 区名称
	 */
	private String name;
	
	/**
	 * 对应服务器列表
	 */
	private List<GameServer> gameServerList;

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
	 * @return the gameServerList
	 */
	@XmlElementWrapper(name="GameServers")
	@XmlElement(name="GameServer")
	public List<GameServer> getGameServerList() {
		return gameServerList;
	}

	/**
	 * @param gameServerList the gameServerList to set
	 */
	public void setGameServerList(List<GameServer> gameServerList) {
		this.gameServerList = gameServerList;
	}

}
