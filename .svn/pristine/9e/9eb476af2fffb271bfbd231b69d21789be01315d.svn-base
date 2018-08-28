/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DiscountServiceTest
 *	包	名：		com.wzitech.gamegold.facade.frontend
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 下午2:46:50
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

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
import com.wzitech.gamegold.facade.frontend.service.goods.IDiscountInfoService;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 折扣服务测试
 * @author HeJian
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class DiscountServiceTest {
	@Autowired
	IDiscountInfoService discountInfoService;
	
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
	 * 新增折扣
	 */
	@Test
	public void testAddDiscount(){
//		AddDiscountRequest request = new AddDiscountRequest();
//		request.setDiscount(new BigDecimal(0.78));
//		request.setGoldCount(800);
//		request.setGoodsId(1000003l);
//		request.setIsDeleted(false);
		
//		AddDiscountResponse response = (AddDiscountResponse)discountInfoService.addDiscount(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	/**
	 * 修改折扣
	 */
	@Test
	public void testModifyDiscount(){
//		ModifyDiscountRequest request = new ModifyDiscountRequest();
//		request.setId(1000004l);
//		request.setDiscount(new BigDecimal(0.88));
//		ModifyDiscountResponse response = (ModifyDiscountResponse)discountInfoService.modifyDiscount(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	/**
	 * 查询折扣
	 */
	@Test
	public void testQueryDiscount(){
//		QueryDiscountRequest req = new QueryDiscountRequest();
//		req.setGoodsId(1000001l);
//		QueryDiscountResponse response = (QueryDiscountResponse)discountInfoService.queryDiscount(req, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
}
