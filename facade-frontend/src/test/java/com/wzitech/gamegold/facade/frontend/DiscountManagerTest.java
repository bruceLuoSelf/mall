package com.wzitech.gamegold.facade.frontend;
/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		DiscountManagerTest
 *	包	名：		com.wzitech.gamegold.facade.frontend
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-19
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-19 下午4:16:22
 * 				
 ************************************************************************************/
import java.math.BigDecimal;
import java.util.HashMap;

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
import com.wzitech.gamegold.goods.business.IDiscountInfoManager;
import com.wzitech.gamegold.goods.dao.IDiscountInfoDBDAO;
import com.wzitech.gamegold.goods.entity.DiscountInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 商品对应折扣管理测试
 * @author HeJian
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class DiscountManagerTest {
	@Autowired
	IDiscountInfoManager discountInfoManager;
	
	@Autowired
	IDiscountInfoDBDAO discountInfoDBDAO;
	
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
	 * 新增折扣测试
	 */
	@Test
	public void testAddDiscount(){
		DiscountInfo discountInfo = new DiscountInfo();
		discountInfo.setDiscount(new BigDecimal(0.9));
		discountInfo.setGoldCount(500);
		discountInfo.setIsDeleted(false);
		discountInfo.setGoodsId(1000001l);
		
//		discountInfoManager.addDiscount(discountInfo);
	}
	
	/**
	 * 修改折扣测试
	 */
	@Test
	public void testModifyDiscount(){
		DiscountInfo discountInfo = new DiscountInfo();
		discountInfo.setId(1000001l);
		discountInfo.setDiscount(new BigDecimal(0.7));
		discountInfo.setGoldCount(1100);
		
//		discountInfoManager.modifyDiscount(discountInfo);
	}
	
	/**
	 * 查询折扣分页测试
	 */
	@Test
	public void testQueryDiscount(){
		HashMap<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("goodsId", 1000001);
		
		discountInfoManager.queryDiscount(queryMap, 1, 1, "GOLD_COUNT", true);
	}
	
	/**
	 * 查询折扣列表测试
	 */
	@Test
	public void testQueryDiscountList(){
		discountInfoManager.queryDiscountInfos(1000001l);
	}
}
