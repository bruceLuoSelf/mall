/************************************************************************************
 *  Copyright 2012 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AccountServiceImpl
 *	包	名：		com.wzitech.chinabeauty.facade.service.usermgmt.impl
 *	项目名称：	chinabeauty-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2013-9-26 下午2:51:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.backend.action.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.gamegold.common.game.IGameInfoManager;
import com.wzitech.gamegold.common.game.entity.GameArea;
import com.wzitech.gamegold.common.game.entity.GameCompanyInfo;
import com.wzitech.gamegold.common.game.entity.GameDetailInfo;
import com.wzitech.gamegold.common.game.entity.GameInfo;
import com.wzitech.gamegold.common.game.entity.GameInfos;
import com.wzitech.gamegold.common.game.entity.GameRaceAndMoney;
import com.wzitech.gamegold.common.game.entity.GameRaceInfo;
import com.wzitech.gamegold.facade.backend.extjs.AbstractFileUploadAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class CommonAction extends AbstractFileUploadAction {

	private static final long serialVersionUID = 2212456161675686700L;
	
	protected static final Log log = LogFactory.getLog(CommonAction.class);

	@Autowired
	IGameInfoManager gameInfoManager;

	private List<GameRaceInfo> gameRaceList;
	
	private List<GameArea> gameAreaList;
	
	private List<GameInfo> gameInfoList;
	
	private List<Map<String,String>> gameCompanyNameList;
	
	private String gameId;
	
	private String gameCompanyName;
	
	public String queryGameCompanyList() {
		boolean flag = false;
		try {
			GameCompanyInfo gameCompanyInfos = gameInfoManager.getAllCompany();
			gameCompanyNameList = new ArrayList<Map<String, String>>();
			for(String companyName : gameCompanyInfos.getCompanyNameList()){
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", companyName);
				gameCompanyNameList.add(map);
			}
		} catch (ClientProtocolException e) {
			log.info("查询游戏公司信息异常", e);
			flag = true;
		} catch (IOException e) {
			log.info("查询游戏公司信息异常", e);
			flag = true;
		} catch (JAXBException e) {
			log.info("游戏信息XML转换异常", e);
			flag = true;
		}
		if(flag){
			return this.returnError("查询游戏公司信息失败");
		}
		return this.returnSuccess();
	}

	/**
	 * 查询游戏信息列表
	 * 
	 * @return
	 */
	public String queryGameInfoList() {
		boolean flag = false;
		try {
			GameInfos gameInfos = null;
			if(StringUtils.isBlank(gameCompanyName)){
				gameInfos = gameInfoManager.getGameAll();				
			}else{
				gameInfos = gameInfoManager.getGameByCompany(gameCompanyName);	
			}
			gameInfoList = gameInfos.getGameList();
		} catch (ClientProtocolException e) {
			log.info("查询游戏信息异常", e);
			flag = true;
		} catch (IOException e) {
			log.info("查询游戏信息异常", e);
			flag = true;
		} catch (JAXBException e) {
			log.info("查询游戏信息XML转换异常", e);
			flag = true;
		}
		if(flag){
			return this.returnError("查询游戏信息失败");
		}
		return this.returnSuccess();
	}
	
	/**
	 * 查询游戏区服列表
	 * 
	 * @return
	 */
	public String queryGameAreaList() {
		boolean flag = false;
		try {
			GameDetailInfo gameDetailInfo = gameInfoManager.getGameById(gameId);
			gameAreaList = gameDetailInfo.getGameAreaList();
		} catch (ClientProtocolException e) {
			log.info("查询游戏信息异常", e);
			flag = true;
		} catch (IOException e) {
			log.info("查询游戏信息异常", e);
			flag = true;
		} catch (JAXBException e) {
			log.info("查询游戏信息XML转换异常", e);
			flag = true;
		}
		if(flag){
			return this.returnError("查询游戏区服信息失败");
		}
		return this.returnSuccess();
	}
	
	/**
	 * 查询游戏阵营列表
	 * 
	 * @return
	 */
	public String queryGameRaceList() {
		boolean flag = false;
		try {
			GameRaceAndMoney gameRaceAndMoney = gameInfoManager.getRaceAndMoney(gameId);
			gameRaceList = new ArrayList<GameRaceInfo>();
			if(gameRaceAndMoney!=null){
				gameRaceList = gameRaceAndMoney.getGameRaceList();				
			}
		} catch (ClientProtocolException e) {
			log.info("查询游戏阵营信息异常", e);
			flag = true;
		} catch (IOException e) {
			log.info("查询游戏阵营信息异常", e);
			flag = true;
		} catch (JAXBException e) {
			log.info("查询游戏阵营信息XML转换异常", e);
			flag = true;
		}
		if(flag){
			return this.returnError("查询游戏阵营信息失败");
		}
		return this.returnSuccess();
	}

	public List<GameRaceInfo> getGameRaceList() {
		return gameRaceList;
	}

	public List<GameInfo> getGameInfoList() {
		return gameInfoList;
	}

	public List<GameArea> getGameAreaList() {
		return gameAreaList;
	}

	public List<Map<String, String>> getGameCompanyNameList() {
		return gameCompanyNameList;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setGameCompanyName(String gameCompanyName) {
		this.gameCompanyName = gameCompanyName;
	}
}