/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		TradePlaceTest
 * 包	名：		com.wzitech.gamegold.facade.frontend
 * 项目名称：	gamegold-facade-frontend
 * 作	者：		Wengwei
 * 创建时间：	2014-2-21
 * 描	述：
 * 更新纪录：	1. Wengwei 创建于 2014-2-21 下午2:05:07
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.facade.frontend.service.shorder.IPayOrderService;
import com.wzitech.gamegold.order.business.IGameConfigManager;
import com.wzitech.gamegold.order.entity.GameConfigEO;
import com.wzitech.gamegold.repository.business.IAgreeInitAccountManager;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.business.IPurchaseOrderManager;
import com.wzitech.gamegold.shorder.dao.IPurchaserDataDao;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
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

import java.util.*;

/**
 * 游戏交易地点测试
 * @author Wengwei
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class TradePlaceTest {
    @Autowired
    IGameConfigManager tradePlaceManager;
    @Autowired
    IRepositoryManager repositoryManager;
    @Autowired
    IPurchaseOrderManager purchaseOrderManager;
    @Autowired
    private ISellerManager sellerManager;
    @Autowired
    private ISellerDBDAO sellerDBDAO;

    @Test
    public void testq() {
        purchaseOrderManager.selectPurchaseOrderAndCgDataById(654L);
    }

    /**
     * 初始化用户session
     */
    @Before
    public void initDB() {
        UserInfoEO loginUser = new UserInfoEO();
        loginUser.setUid("US14022163863213-0315");
        loginUser.setLoginAccount("GeiliTest");
        CurrentUserContext.setUser(loginUser);
    }

    /**
     * 添加游戏交易地
     */
    @Test
    public void addTradePlace() {
        GameConfigEO eo = new GameConfigEO();
        eo.setId(1000001l);
        eo.setGameName("DNF");
        eo.setPlaceName("凯丽");
        eo.setPlaceImage("/shuabao/userfile/1000011/avatar.jpg");
        eo.setCreateTime(new Date());
        eo.setLastUpdateTime(new Date());
        eo.setMailTime(3);
        eo.setIsDeleted(true);

//		tradePlaceManager.addTradePlace(eo);
    }


    @Test
    public void test() {
        //purchaseOrder.getPrice().multiply(new BigDecimal(order.getCount().toString()))
        //.setScale(2, RoundingMode.DOWN)
        //BigDecimal price=new BigDecimal(1.1111).multiply(new BigDecimal(5.5555)).setScale(2, RoundingMode.DOWN);

        repositoryManager.querySellerAvgPriceRepository("地下城与勇士", "广东区", "广东1区", "", ServicesContants.GOODS_TYPE_GOLD);
    }

    @Autowired
    private IFundManager fundManager;
    @Autowired
    private IPayOrderManager payOrderManager;
    @Autowired
    private IPurchaserDataDao purchaserDataDao;

    @Test
    public void test11() {
//        List<PayOrder> payOrders = payOrderManager.queryAvailablePayOrders("dk444", true);
//        for(PayOrder payOrder:payOrders){
//            fundManager.transfer(payOrder.getOrderId(), payOrder.getOrderId(), payOrder.getUid(),
//                    payOrder.getAccount(), "US16122251120213-0720", "wangjunjie", new BigDecimal(80), payOrder);
//        }

        PurchaserData purchaserData = purchaserDataDao.selectByAccountForUpdate("dk444");
    }

    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    IAgreeInitAccountManager agreeInitAccountManager;

    @Test
    public void testaaa(){
        agreeInitAccountManager.initAccount("余文浪");
    }

    @Test
    public void testaa(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("s","s");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry);
        }
    }
    @Test
    public void testa(){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("loginAccount","a7343644");
        List<SellerInfo> list = sellerDBDAO.selectByMap(map);
        System.out.println(list);
    }

    @Test
    public void test2(){
        List<SellerInfo> list = sellerDBDAO.querySellerInfo("a7343644");
        System.out.println(list);
    }


}

