/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameInfoRedioDAOImpl
 *	包	名：		com.wzitech.gamegold.common.game.dao.impl
 *	项目名称：	gamegold-common
 *	作	者：		Wengwei
 *	创建时间：	2014-3-7
 *	描	述：		
 *	更新纪录：	1. Wengwei 创建于 2014-3-7 下午6:05:37
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.dao.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.game.dao.IGameInfoRedisDAO;
import com.wzitech.gamegold.common.game.entity.AnalysisUrlResponse;
import com.wzitech.gamegold.common.game.entity.GameArea;
import com.wzitech.gamegold.common.game.entity.GameCompanyInfo;
import com.wzitech.gamegold.common.game.entity.GameDetailInfo;
import com.wzitech.gamegold.common.game.entity.GameInfo;
import com.wzitech.gamegold.common.game.entity.GameInfos;
import com.wzitech.gamegold.common.game.entity.GameMoneyInfo;
import com.wzitech.gamegold.common.game.entity.GameNameAndId;
import com.wzitech.gamegold.common.game.entity.GameRaceAndMoney;
import com.wzitech.gamegold.common.game.entity.GameRaceInfo;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;

/**
 * 游戏信息类目管理
 * @author Wengwei
 *
 * Update History
 * DATE           NAME       REASON FOR UPDATE
 * ---------------------------------------------
 * 2017.5.12     339928     ZW_C_JB_00008 商城增加通货
 *
 */
@Repository
public class GameInfoRedisDAOImpl extends AbstractRedisDAO<BaseEntity> implements IGameInfoRedisDAO{
	@Autowired
	@Qualifier("userRedisTemplate")
	@Override
	public void setTemplate(StringRedisTemplate template) {
		super.template = template;
	}

	JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
	
	/**
	 * 获取所有游戏
	 */
	@Override
	public GameInfos getGameAll() {	
		String json = valueOps.get(RedisKeyHelper.gameAll());
		if(StringUtils.isEmpty(json)){
			return null;
		}
		GameInfos game = new GameInfos();
		//将保存在json中的值转换成List<GameInfo>类型
		List<GameInfo> gameList = jsonMapper.fromJson(json, 
				jsonMapper.createCollectionType(List.class, GameInfo.class));
		game.setGameList(gameList);
		return game;
	}
	
	/**
	 * 保存所有游戏
	 */
	@Override
	public void saveGameAll(GameInfos gameInfos) {
		if (gameInfos.getGameList() != null && gameInfos.getGameList().size() > 0) {
			String gameList = jsonMapper.toJson(gameInfos.getGameList());
			valueOps.set(RedisKeyHelper.gameAll(), gameList);
			
			// 设置过期时间
			this.template.expire(RedisKeyHelper.gameAll(), 1, TimeUnit.DAYS);
		}	
	}
	
	/**
	 * 获取对应游戏区服id
	 */
	@Override
	public GameNameAndId getIds(String gameName, String anyName, int type) {
		String json = valueOps.get(RedisKeyHelper.gameAllIds(gameName,anyName,type));
		if(StringUtils.isEmpty(json)){
			return null;
		}
		//将保存在json中的值转换成GameNameAndId类型
		GameNameAndId gameNameIdsList = jsonMapper.fromJson(json, 
				jsonMapper.createCollectionType(GameNameAndId.class));
		return gameNameIdsList;
	}
	
	/**
	 * 保存对于区服游戏
	 */
	@Override
	public void saveIds(GameNameAndId gameNameId) {
		if (gameNameId != null) {
			String gameNameIdStr = jsonMapper.toJson(gameNameId);
			valueOps.set(RedisKeyHelper.gameAllIds(gameNameId.getGameName(),gameNameId.getAnyName(),gameNameId.getType()), gameNameIdStr);
			// 设置过期时间
			this.template.expire(RedisKeyHelper.gameAllIds(gameNameId.getGameName(),gameNameId.getAnyName(),gameNameId.getType()), 1, TimeUnit.DAYS);
		}	
	}
	
	/**
	 * 根据游戏厂商名称获取游戏
	 */
	@Override
	public GameInfos getGameByCompany(String companyName) {
		String json = valueOps.get(RedisKeyHelper.companyGame(companyName));
		if (json == null) {
			return null;
		}
		GameInfos gameInfos = new GameInfos();
		//将存在json中的值转换成List<GameInfo>类型
		List<GameInfo> gameInfosList = jsonMapper.fromJson(json, 
				jsonMapper.createCollectionType(List.class, GameInfo.class));
		gameInfos.setGameList(gameInfosList);
		return gameInfos;
	}
	
	/**
	 * 根据游戏厂商保存游戏
	 */
	@Override
	public void saveGameByCompany(String companyName, GameInfos gameInfos) {
		if (StringUtils.isEmpty(companyName)) {
			return;
		}
		if (gameInfos.getGameList() != null && gameInfos.getGameList().size() > 0) {
			String gameList = jsonMapper.toJson(gameInfos.getGameList());
			valueOps.set(RedisKeyHelper.companyGame(companyName), gameList);
			
			// 设置过期时间
			this.template.expire(RedisKeyHelper.companyGame(companyName), 1, TimeUnit.DAYS);
		}	
	}

	
	/**
	 * 获取所有游戏厂商
	 */
	@Override
	public GameCompanyInfo getAllComoany() {
		String json = valueOps.get(RedisKeyHelper.gameAllCompany());
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		GameCompanyInfo gameCompany = new GameCompanyInfo();
		//将存在json中的值转换成List<String>类型
		List<String> gameCompanyList = jsonMapper.fromJson(json, 
				jsonMapper.createCollectionType(List.class, String.class));
		gameCompany.setCompanyNameList(gameCompanyList);
		return gameCompany;
	}

	/**
	 * 保存所有的游戏厂商
	 */
	@Override
	public void saveGameCompany(GameCompanyInfo gameCompanyInfo) {
		if (gameCompanyInfo.getCompanyNameList() != null && gameCompanyInfo.getCompanyNameList().size() > 0) {
			String gameCompanyList = jsonMapper.toJson(gameCompanyInfo.getCompanyNameList());
			valueOps.set(RedisKeyHelper.gameAllCompany(), gameCompanyList);
			
			// 设置过期时间
			this.template.expire(RedisKeyHelper.gameAllCompany(), 1, TimeUnit.DAYS);
		}
	}
	
	/**
	 * 根据游戏Id获取游戏信息
	 */
	@Override
	public GameDetailInfo getGameById(String id) {
		//从redis中获取对应的游戏信息
		BoundHashOperations<String, String, String> gameOps = 
				template.boundHashOps(RedisKeyHelper.gameId(id));
		
		if(StringUtils.isEmpty(gameOps.get("id"))){
			return null;
		}
		GameDetailInfo responseInfo = new GameDetailInfo();
		responseInfo.setId(gameOps.get("id"));
		responseInfo.setName(gameOps.get("name"));
		responseInfo.setSpell(gameOps.get("spell"));
		//将存在json中的值转换成List<GameArea>类型
		List<GameArea> gameAreaList = jsonMapper.fromJson(gameOps.get("gameAreaList"), 
				jsonMapper.createCollectionType(List.class, GameArea.class));
		responseInfo.setGameAreaList(gameAreaList);
		return responseInfo;
		
	}

	/**
	 * 保存游戏信息
	 */
	@Override
	public void saveGameDetailInfo(GameDetailInfo gameInfo) {
		BoundHashOperations<String, String, String> gameOps = 
				template.boundHashOps(RedisKeyHelper.gameId(gameInfo.getId()));
		//将游戏信息保存至redis中
		if(!StringUtils.isEmpty(gameInfo.getId())){
			gameOps.put("id", gameInfo.getId());
		}
		if(!StringUtils.isEmpty(gameInfo.getName())){
			gameOps.put("name", gameInfo.getName());
		}
		if (!StringUtils.isEmpty(gameInfo.getSpell())) {
			gameOps.put("spell", gameInfo.getSpell());
		}
		if(gameInfo.getGameAreaList() != null && gameInfo.getGameAreaList().size() > 0){
			String gameAreaList = jsonMapper.toJson(gameInfo.getGameAreaList());
			gameOps.put("gameAreaList", gameAreaList);
		}
		
		// 设置过期时间
		this.template.expire(RedisKeyHelper.gameId(gameInfo.getId()), 1, TimeUnit.DAYS);
	}

	/**
	 * 根据游戏Id获取阵营，游戏币信息
	 */
	@Override
	public GameRaceAndMoney getRaceAndMoney(String gameId) {
		//从redis中获取对应的阵营，游戏币信息
		BoundHashOperations<String, String, String> grmOps = 
				template.boundHashOps(RedisKeyHelper.gameId(gameId));		
		
		if(StringUtils.isEmpty(grmOps.get("id")) || StringUtils.isEmpty(grmOps.get("gameMoneyList"))){
			return null;
		}
		
		GameRaceAndMoney responseGRM = new GameRaceAndMoney();
		responseGRM.setId(grmOps.get("id"));
		//将存在json中的值转换成List<GameRaceInfo>类型
		List<GameRaceInfo> gameRaceList = jsonMapper.fromJson(grmOps.get("gameRaceList"), 
				jsonMapper.createCollectionType(List.class, GameRaceInfo.class));
		List<GameMoneyInfo> gameMoneyList = jsonMapper.fromJson(grmOps.get("gameMoneyList"), 
				jsonMapper.createCollectionType(List.class, GameMoneyInfo.class));
		responseGRM.setGameRaceList(gameRaceList);
		responseGRM.setGameMoneyList(gameMoneyList);
		return responseGRM;
	}

	/**
	 * 保存阵营，游戏币信息至
	 */
	@Override
	public void svaeGameRaceAndMoney(GameRaceAndMoney gameRaceAndMoney) {
		BoundHashOperations<String, String, String> grmOps = 
				template.boundHashOps(RedisKeyHelper.gameId(gameRaceAndMoney.getId()));
		//保存阵营，游戏币信息至redis
		if(!StringUtils.isEmpty(gameRaceAndMoney.getId())){
			grmOps.put("id", gameRaceAndMoney.getId());
		}
		if(gameRaceAndMoney.getGameRaceList() != null && gameRaceAndMoney.getGameRaceList().size() > 0){
			String gameRaceList = jsonMapper.toJson(gameRaceAndMoney.getGameRaceList());
			grmOps.put("gameRaceList", gameRaceList);
		}
		if(gameRaceAndMoney.getGameMoneyList() != null && gameRaceAndMoney.getGameMoneyList().size() > 0){
			String gameMoneyList = jsonMapper.toJson(gameRaceAndMoney.getGameMoneyList());
			grmOps.put("gameMoneyList", gameMoneyList);
		}
		
		// 设置过期时间
		this.template.expire(RedisKeyHelper.gameId(gameRaceAndMoney.getId()), 1, TimeUnit.DAYS);
	}

	/**
	 * 根据currentUrl获取游戏页面信息
	 */
	@Override
	public AnalysisUrlResponse getGameUrl(String currentUrl) {
		// 从redis中获取对应currentUrl的AnalysisUrlResponse
		BoundHashOperations<String, String, String> urlOps = 
				template.boundHashOps(RedisKeyHelper.urlGameInfo(currentUrl));
		
		if(StringUtils.isEmpty(urlOps.get("gameName"))){
			return null;
		}
		
		AnalysisUrlResponse response = new AnalysisUrlResponse();
		response.setGameName(urlOps.get("gameName"));
		response.setGameRegion(urlOps.get("gameRegion"));
		response.setGameServer(urlOps.get("gameServer"));
		response.setGameRace(urlOps.get("gameRace"));
		//如果没有找到通货类型就返回null重新去获取
		if(StringUtils.isBlank(urlOps.get("goodsTypeName"))){
			return null;
		}
		response.setGoodsTypeName(urlOps.get("goodsTypeName"));
		return response;
	};

	/**
	 * 根据currentUrl保存游戏页面信息
	 */
	@Override
	public void saveUrl(String currentUrl, AnalysisUrlResponse analysisUrl) {
		if (currentUrl == null) {
			return;
		}
		// 将currentUrl所对应的页面信息保存至Redis
		BoundHashOperations<String, String, String> urlOps = 
				template.boundHashOps(RedisKeyHelper.urlGameInfo(currentUrl));
		// 先判断是否为空然后再存
		if (!StringUtils.isEmpty(analysisUrl.getGameName())) {
			urlOps.put("gameName", analysisUrl.getGameName());
		}
		if (!StringUtils.isEmpty(analysisUrl.getGameRegion())) {
			urlOps.put("gameRegion", analysisUrl.getGameRegion());
		}
		if (!StringUtils.isEmpty(analysisUrl.getGameServer())) {
			urlOps.put("gameServer", analysisUrl.getGameServer());
		}
		if (!StringUtils.isEmpty(analysisUrl.getGameRace())) {
			urlOps.put("gameRace", analysisUrl.getGameRace());
		}
		//增加通货类型
		if(!StringUtils.isEmpty(analysisUrl.getGoodsTypeName())){
			urlOps.put("goodsTypeName",analysisUrl.getGoodsTypeName());
		}
		
		// 设置过期时间
		this.template.expire(RedisKeyHelper.urlGameInfo(currentUrl), 1, TimeUnit.DAYS);
	}
	
}
