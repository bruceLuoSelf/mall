/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TradePlaceServiceTest
 *	包	名：		com.wzitech.gamegold.facade.frontend
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		Wengwei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. Wengwei 创建于 2014-2-21 下午6:46:26
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

import org.junit.Assert;
import org.junit.Before;
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

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.facade.frontend.service.order.ITradePlaceService;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QueryTradePlaceRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.QueryTradePlaceResponse;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * @author Wengwei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class TradePlaceServiceTest {
	
	@Autowired
	ITradePlaceService tradePlaceService;
	
	/**
	 * 初始化用户session
	 */
	@Before
	public void initDB(){
		UserInfoEO loginUser = new UserInfoEO();
		loginUser.setUid("US14022163863213-0315");
		loginUser.setLoginAccount("GeiliTest");
		CurrentUserContext.setUser(loginUser);
	}
	/**
	 * 根据游戏名称查询交易地点
	 */
	@Test
	public void selecePlaceTest(){
		QueryTradePlaceRequest request = new QueryTradePlaceRequest();
		request.setGameName("DNF");
		
		QueryTradePlaceResponse response = (QueryTradePlaceResponse) tradePlaceService.queryTradePlaceByGameName(request, null);
		System.out.println("code：" + response.getResponseStatus().getCode());
		System.out.println("message：" + response.getResponseStatus().getMessage());
		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
}
