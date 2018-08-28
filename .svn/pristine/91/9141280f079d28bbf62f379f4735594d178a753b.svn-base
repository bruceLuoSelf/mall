/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GoodsServiceTest
 *	包	名：		com.wzitech.gamegold.facade.frontend
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-17
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-17 下午5:59:20
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

import java.math.BigDecimal;

import com.wzitech.gamegold.common.enums.ResponseCodes;
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
import com.wzitech.gamegold.facade.frontend.service.goods.IGoodsInfoService;
import com.wzitech.gamegold.facade.frontend.service.goods.dto.QueryGoodsRequest;
import com.wzitech.gamegold.facade.frontend.service.goods.dto.QueryGoodsResponse;
import com.wzitech.gamegold.goods.business.IGoodsInfoManager;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * @author HeJian
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class GoodsServiceTest {
	@Autowired
	IGoodsInfoService goodsInfoService;
	
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
	
	@Autowired
	IGoodsInfoManager goodsInfoManager;
	
	/**
	 * 新增商品
	 */
	@Test
	public void addGoodsTest(){
		GoodsInfo request = new  GoodsInfo();
		request.setGameName("魔兽世界(国服)");
		request.setImageUrls("/shuabao/userfile/1000011/avatar.jpg");
		request.setRegion("二区(北京网通)");
		request.setServer("阿迦玛甘");
		request.setTitle("商品1");
		request.setUnitPrice(new BigDecimal(0.1));
		request.setDeliveryTime(3);
		request.setGoodsCat(1);
		request.setMoneyName("金币");
		
//		goodsInfoManager.addGoodsInfo(request);
	}
	
	/**
	 * 查询商品
	 */
	@Test
	public void queryGoodsTest(){
//		QueryGoodsRequest request = new QueryGoodsRequest();
//		request.setGameName("魔兽世界");
//		request.setServer("石塔尖2");
//		request.setPageSize(1);
//		request.setPubSize(1);
//		
//		QueryGoodsResponse response = (QueryGoodsResponse)goodsInfoService.queryGoodsByForm(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	/**
	 * 修改商品
	 */
	@Test
	public void modifyGoodsTest(){
//		ModifyGoodsRequest request = new ModifyGoodsRequest();
//		request.setId(1000002l);
//		request.setGameName("魔兽争霸");
//		request.setRegion("上海三");
//		request.setUnitPrice(new BigDecimal(0.2));
//		request.setDeliveryTime(2);
//		request.setServer("服务器2");
//		request.setTitle("商品4");
//		request.setIsDeleted(true);
//		request.setImageUrls("/shuabao/userfile/1000022/avatar.jpg");
//		
//		ModifyGoodsResponse response = (ModifyGoodsResponse)goodsInfoService.modifyGoods(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	/**
	 * 查询商品测试
	 *//*
	@Test
	public void selectGoodsTest(){
        QueryGoodsRequest request = new QueryGoodsRequest();
        request.setCurrentUrl("http://s.5173.com/dnf-0-f10pkw-uokzto-0-bx1xiv-0-0-0-a-a-a-a-a-0-0-0-0.shtml");

        QueryGoodsResponse response = (QueryGoodsResponse)goodsInfoService.selectGoods(request, null);
		System.out.println("code：" + response.getResponseStatus().getCode());
		System.out.println("message：" + response.getResponseStatus().getMessage());
		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	@Test
	public void querySigelTest(){
		QueryGoodsRequest request = new QueryGoodsRequest();
		request.setGameName("魔兽世界(国服)");
		request.setGameRace("联盟");
		request.setGoodsCat(1);
		request.setRegion("二区(北京网通)");
		request.setServer("阿迦玛甘");
		
		QueryGoodsResponse response2 = (QueryGoodsResponse) goodsInfoService.selectGoods(request, null);
		System.out.println(response2);
		
		QueryGoodsResponse response = (QueryGoodsResponse) goodsInfoService.querySingleGoods(request, null);
		System.out.println(response);
	}*/
	
	/*@Test
	public void deleteGoodsInfoTest(){
		goodsInfoManager.deleteGoodsInfo(1000016L);
	}*/
}
