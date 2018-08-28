/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IGameInfoManager
 *	包	名：		com.wzitech.gamegold.common.game
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午2:16:59
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.ClientProtocolException;

import com.wzitech.gamegold.common.game.entity.AnalysisUrlResponse;
import com.wzitech.gamegold.common.game.entity.GameCompanyInfo;
import com.wzitech.gamegold.common.game.entity.GameDetailInfo;
import com.wzitech.gamegold.common.game.entity.GameInfos;
import com.wzitech.gamegold.common.game.entity.GameNameAndId;
import com.wzitech.gamegold.common.game.entity.GameRaceAndMoney;

/**
 * 游戏信息类目管理
 * @author SunChengfei
 *
 */
public interface IGameInfoManager {
	/**
	 * 获取所有游戏
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JAXBException 
	 */
	public GameInfos getGameAll() throws ClientProtocolException, IOException, JAXBException;
	
	/**
	 * 获取所有厂商
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws JAXBException 
	 */
	public GameCompanyInfo getAllCompany() throws HttpException, IOException, JAXBException;
	
	/**
	 * 根据游戏厂商名称获取游戏
	 * @param companyName
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws JAXBException 
	 */
	public GameInfos getGameByCompany(String companyName) throws HttpException, IOException, JAXBException;
	
	/**
	 * 根据游戏ID获取游戏信息
	 * @param gameId
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws JAXBException 
	 */
	public GameDetailInfo getGameById(String gameId) throws ClientProtocolException, IOException, JAXBException;
	
	/**
	 * 根据游戏ID获取阵营、游戏币信息
	 * @param gameId
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws JAXBException 
	 */
	public GameRaceAndMoney getRaceAndMoney(String gameId) throws HttpException, IOException, JAXBException;
	
	/**
	 * 解析页面url
	 * @param currentUrl
	 * @return
	 * @throws IOException 
	 */
	public AnalysisUrlResponse analysisUrl(String currentUrl) throws IOException;

	//得到对应游戏区服阵营 信息
	GameNameAndId getIdByProp(String gameName,String anyName, Integer type)
			throws Exception;
}
