/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderServiceTest
 *	包	名：		com.wzitech.gamegold.facade.frontend
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-18
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-18 下午10:47:10
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

import java.math.BigDecimal;
import java.util.Date;

import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
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
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.facade.frontend.service.order.IOrderService;
import com.wzitech.gamegold.facade.frontend.service.order.dto.AddOrderRequest;
import com.wzitech.gamegold.facade.frontend.service.order.dto.ModifyOrderRequest;
import com.wzitech.gamegold.order.dao.IOrderInfoRedisDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * @author SunChengfei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class OrderServiceTest {
	@Autowired
	IOrderService orderService;
	
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
	public void addOrderTest(){
		AddOrderRequest request = new AddOrderRequest();
		request.setMobileNumber("13878777777");
		request.setQq("77474747");
		request.setReceiver("某某角色");
		request.setServicerId(1000002L);
		request.setGoldCount(500);
		request.setGameName("魔兽世界(国服)");
		request.setGoodsId(1000001L);
		request.setRegion("劈成手背");
		request.setServer("一区(北京网通)");
		request.setTitle("商品1");
		request.setMoneyName("元宝");
		request.setDiscount(new BigDecimal(1));
		request.setDeliveryTime(5);
		request.setUnitPrice(new BigDecimal(0.1));
		
//		AddOrderResponse response = (AddOrderResponse) orderService.addOrder(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	@Test
	public void updateOrderTest(){
		ModifyOrderRequest request = new ModifyOrderRequest();
		request.setMobileNumber("22221111");
		request.setQq("33355555");
		request.setReceiver("dandan角色");
		request.setTotalPrice(new BigDecimal(200));
		request.setOrderState(OrderState.WaitDelivery.getCode());
		request.setOrderId("14022100001");
		
//		ModifyOrderResponse response = (ModifyOrderResponse) orderService.modifyOrder(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	/**
	 * 根据订单id查询订单测试
	 */
	@Test
	public void testQueryOrderById(){
//		QueryOrderByIdRequest request = new QueryOrderByIdRequest();
//		request.setOrderId("14022100001");
//
//		QueryOrderByIdResponse response = (QueryOrderByIdResponse) orderService.queryOrderById(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	@Autowired
	IOrderInfoRedisDAO orderInfoRedisDAO;
	
	@Test
	public void redisTest(){
		OrderInfoEO orderInfoEO = new OrderInfoEO();
		orderInfoEO.setMobileNumber("13878777777");
		orderInfoEO.setQq("77474747");
		orderInfoEO.setReceiver("某某角色");
		orderInfoEO.setServicerId(1000002L);
		orderInfoEO.setGoldCount(500L);
		orderInfoEO.setUid("1000002");
		orderInfoEO.setCreateTime(new Date());
		orderInfoEO.setDeliveryTime(5);
		orderInfoEO.setEndTime(new Date());
		orderInfoEO.setGameName("魔兽世界");
		orderInfoEO.setGameRace("部落");
		orderInfoEO.setGoodsId(1000002l);
		orderInfoEO.setIsDelay(false);
		orderInfoEO.setIsHaveStore(true);
		orderInfoEO.setNotes("备注");
		orderInfoEO.setOrderId("14022100002");
		orderInfoEO.setOrderState(1);
		orderInfoEO.setRegion("美服");
		orderInfoEO.setSendTime(new Date());
		orderInfoEO.setServer("石头");
		orderInfoEO.setServicerId(1000003l);
		orderInfoEO.setTitle("魔石");
		orderInfoEO.setTradeType(2);
		orderInfoEO.setTotalPrice(new BigDecimal(6000));
		orderInfoEO.setUnitPrice(new BigDecimal(30));
		orderInfoEO.setUserAccount("hehehe");
		
//		orderInfoRedisDAO.saveOrder(orderInfoEO);
//		orderInfoRedisDAO.queryOrder("1000002");
	}
	
	/**
	 * 查询最新5笔订单测试
	 */
	@Test
	public void testQueryFiveOrder(){
//		QueryOrderFiveRequest request = new QueryOrderFiveRequest();
//		
//		QueryOrderFiveResponse response = (QueryOrderFiveResponse) orderService.queryOrderFive(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}
	
	/**
	 * 查询最新的订单测试
	 */
	@Test
	public void testQueryNewOrder(){
//		QueryNewOrderRequest request = new QueryNewOrderRequest();
//		request.setGameName("魔兽世界");
//		request.setGameRace("联盟");
//		request.setRegion("国服");
//		request.setServer("石塔尖");
//		
//		QueryOrderByIdResponse response = (QueryOrderByIdResponse)orderService.queryNewOrder(request, null);
//		System.out.println("code：" + response.getResponseStatus().getCode());
//		System.out.println("message：" + response.getResponseStatus().getMessage());
//		Assert.assertEquals(ResponseCodes.Success.getCode(), response.getResponseStatus().getCode());
	}

    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;

    @Test
    public void stateJudgeTest(){
        OrderState orderState = OrderState.Cancelled;

        OrderInfoEO orderInfo = orderInfoDBDAO.selectUniqueByProp("orderId",
                "YX1408180000008");

        System.out.println("test start");

        if (orderState.equals(orderInfo.getOrderState())) {
            System.out.println("test step in");
        }

        // 订单状态无改变，就不做后续操作
        if (orderInfo.getOrderState() != null && orderState.getCode() == orderInfo.getOrderState()) {
            System.out.println("test step in");
        }

        System.out.println("test end");
    }
}
