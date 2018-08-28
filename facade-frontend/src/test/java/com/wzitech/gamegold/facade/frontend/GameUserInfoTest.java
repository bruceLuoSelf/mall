/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameUserInfoTest
 *	包	名：		com.wzitech.gamegold.facade.frontend
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午6:06:22
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

import java.io.IOException;

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

import com.wzitech.gamegold.common.usermgmt.IGameUserManager;
import com.wzitech.gamegold.common.usermgmt.entity.GameUserInfo;

/**
 * 5173注册用户信息管理
 * @author SunChengfei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class GameUserInfoTest {
	@Autowired
	IGameUserManager gameUserManager;
	
	@Test
	public void analysisCookieTest() throws ClientProtocolException, IOException{
		GameUserInfo userInfo = gameUserManager.analysisCookie("p38qEFNNLaG01CyOJsl9O8I0/tnMUk7sPc4R7NP/E++/2M+kbuGS7BQrSSkI1tCNkuk0"
				+ "ppFjqw0GGzVvJRsywaUBQ/CdhwjOY9imlXAj1dC3+V1yJT5g0QJ7wZxwAh63oZuzO+QqOgfJnQqr6omCDxUHdvoxgMltIuVXDOvG5b27oF4EjBcnX"
				+ "UZl+CVvUilHPztLPswXoHSdqq8i8f5B9+MKycey8wzH6KLy7D8OejAY5v/isNUODusTZuYQYs3elRM1twVSepcfWaoNvkFkTSW+YrPhtaH/wqnCGm"
				+ "PUCyHg8R4QAgN96A");
		
		System.out.println(userInfo);
	}
}
