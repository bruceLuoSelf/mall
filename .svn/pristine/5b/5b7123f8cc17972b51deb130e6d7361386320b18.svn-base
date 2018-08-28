/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameInfoTest
 *	包	名：		com.wzitech.gamegold.facade.frontend
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午2:28:17
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.wzitech.gamegold.common.game.IGameInfoManager;

/**
 * 游戏类目信息测试
 * @author SunChengfei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class GameInfoTest {
	@Autowired
	IGameInfoManager gameInfoManager;
	
	@Test
	public void getAllGameTest() throws ClientProtocolException, IOException, JAXBException{
//		gameInfoManager.getGameAll();
	}
	
	@Test
	public void getGameById() throws ClientProtocolException, IOException, JAXBException{
//		gameInfoManager.getGameById("0db80df2ab83473490a0245b04c64c13");
	}
	
	@Test
	public void getCompanyTest() throws HttpException, IOException, JAXBException{
//		gameInfoManager.getAllCompany();
	}
	
	@Test
	public void getGameByCompanyTest() throws HttpException, IOException, JAXBException{
//		gameInfoManager.getGameByCompany("九城游戏");
	}
	
	@Test
	public void getRaceAndMoneyTest() throws HttpException, IOException, JAXBException{
//		gameInfoManager.getRaceAndMoney("0db80df2ab83473490a0245b04c64c13");
	}
	
	@Test
	public void analysisUrlTest() throws IOException{
//		AnalysisUrlResponse response = gameInfoManager.analysisUrl("http://s.5173.com/wow-q2jm41-890-943-0-ou5epo-0-0-0-a-a-a-a-a-0-moneyaverageprice_asc-0-0.shtml");
//		System.out.println(response);
	}
}
