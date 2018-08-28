/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryServicerTest
 *	包	名：		com.wzitech.gamegold.facade.frontend
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 下午4:59:10
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

import java.util.Date;

import com.wzitech.gamegold.shorder.business.IFundDetailManager;
import com.wzitech.gamegold.shorder.entity.FundType;
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
import com.wzitech.gamegold.facade.frontend.service.repository.IQueryServicerService;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryServicerRequest;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QueryServicerResponse;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 订单页查询客服测试
 * @author HeJian
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class QueryServicerTest {
	@Autowired
	IQueryServicerService queryServicerService;
	
	@Autowired
	IUserInfoManager userInfoManager;
	/**
	 * 订单页查询客服测试
	 */
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
	@Test
	public void testQueryServicer(){
		QueryServicerRequest request = new QueryServicerRequest();
		request.setGameName("魔兽世界");
		request.setGoldCount(100);
		request.setRegion("上海一区");
		request.setServer("黑石塔");
		
		QueryServicerResponse response = (QueryServicerResponse)queryServicerService.queryOrderServicer(request, null);
		System.out.println("code：" + response.getResponseStatus().getCode());
		System.out.println("message：" + response.getResponseStatus().getMessage());
		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	/**
	 * 添加客服测试
	 */
	@Test
	public void testAddServicer(){
		UserInfoEO user = new UserInfoEO();
		user.setAvatarUrl("/gamegold/userfile/1000011/avatar.jpg");
		user.setCreateTime(new Date());
		user.setLoginAccount("192468080@qq.com");
		user.setFundsAccount("chengfei.sun@wzitech.com");
		user.setFundsAccountId("1000007");
		user.setIsDeleted(false);
		user.setLastUpdateTime(new Date());
		user.setNickName("小王");
		user.setPassword("11111111");
		user.setPhoneNumber("13761661011");
		user.setQq("192468080");
		user.setRealName("何");
		user.setSex("1");
		user.setSign("签名：我喜欢白天，因为白天能作白日梦");
		user.setUserType(1);
		user.setWeiXin("weixin@weixin.com");
		
//		userInfoManager.addUser(user);
		
	}

	@Autowired
	protected IFundDetailManager fundDetailManager;
	@Test
	public void isExistFundDetail(){
		fundDetailManager.isExistFundDetail("supmj6", "SHZF16122100000031", FundType.RECHARGE.getCode());
	}
}
