/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IGameInfoRedioDAO
 *	包	名：		com.wzitech.gamegold.common.game.dao
 *	项目名称：	gamegold-common
 *	作	者：		Wengwei
 *	创建时间：	2014-3-7
 *	描	述：		
 *	更新纪录：	1. Wengwei 创建于 2014-3-7 下午6:04:26
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.dao;

import java.util.List;

import com.wzitech.gamegold.common.game.entity.AnalysisUrlResponse;
import com.wzitech.gamegold.common.game.entity.GameCompanyInfo;
import com.wzitech.gamegold.common.game.entity.GameDetailInfo;
import com.wzitech.gamegold.common.game.entity.GameInfos;
import com.wzitech.gamegold.common.game.entity.GameNameAndId;
import com.wzitech.gamegold.common.game.entity.GameRaceAndMoney;

/**
 * 游戏信息类目管理
 * @author Wengwei
 *
 */
public interface IGameInfoRedisDAO {
	
	/**
	 * 获取所有游戏
	 * @return
	 */
	public GameInfos getGameAll();
	
	/**
	 * 将所有游戏保存在redis中
	 * @param gameInfo
	 */
	public void saveGameAll(GameInfos gameInfo);
	
	/**
	 * 根据游戏厂商名称获取游戏
	 * @return
	 */
	public GameInfos getGameByCompany(String companyName);
	
	/**
	 * 根据游戏厂商保存游戏
	 * @param companyName
	 * @param gameInfos
	 */
	public void saveGameByCompany(String companyName, GameInfos gameInfos); 
	
	/**
	 * 获取所有游戏厂商
	 * @return
	 */
	public GameCompanyInfo getAllComoany();
	
	/**
	 * 将所有游戏厂商保存在redis中
	 */
	public void saveGameCompany (GameCompanyInfo gameCompanyInfo);
	
	/**
	 * 根据游戏Id获取游戏信息
	 * @param gameId
	 * @return
	 */
	public GameDetailInfo getGameById(String gameId);
	
	/**
	 * 保存游戏信息至redis
	 * @param gameInfo
	 */
	public void saveGameDetailInfo(GameDetailInfo gameInfo);
	
	/**
	 * 根据游戏Id获取阵营，游戏币信息
	 * @param gameId
	 * @return
	 */
	public GameRaceAndMoney getRaceAndMoney(String gameId);
	
	/**
	 * 保存阵营，游戏币信息至redis
	 * @param gameRaceAndMoney
	 */
	public void svaeGameRaceAndMoney(GameRaceAndMoney gameRaceAndMoney);
	
	/**
	 * 根据currentUrl查找对应的游戏页面
	 * @param currentUrl
	 * @return
	 */
	public AnalysisUrlResponse getGameUrl(String currentUrl);
	
	/**
	 * 保存currentUrl地址对应的  游戏页面  到Redis
	 * @param analysisUrlResponse
	 */
	public void saveUrl(String currentUrl, AnalysisUrlResponse analysisUrl);

	//得到所有游戏区服id
	public GameNameAndId getIds(String gameName,String anyName, int type);

	//保存所有游戏区服id
	public void saveIds(GameNameAndId gameNameId);	
}
