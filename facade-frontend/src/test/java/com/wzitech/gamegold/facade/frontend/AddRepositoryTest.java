/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		AddRepositoryTest
 * 包	名：		com.wzitech.gamegold.facade.frontend
 * 项目名称：	gamegold-facade-frontend
 * 作	者：		HeJian
 * 创建时间：	2014-2-20
 * 描	述：
 * 更新纪录：	1. HeJian 创建于 2014-2-20 下午5:54:53
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.order.business.ISubOrderDetailManager;
import com.wzitech.gamegold.order.dao.IConfigResultInfoDBDAO;
import com.wzitech.gamegold.order.dto.SubOrderDetailDTO;
import com.wzitech.gamegold.shorder.business.IPurchaseOrderManager;
import com.wzitech.gamegold.shorder.business.IShGameConfigManager;
import com.wzitech.gamegold.shorder.business.ISplitRepositoryRequestManager;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.dao.IGameAccountDBDAO;
import com.wzitech.gamegold.shorder.dao.IRepositorySplitRequestManager;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
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
import com.wzitech.gamegold.facade.frontend.service.repository.IRepositoryService;
import com.wzitech.gamegold.order.business.IOrderConfigManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.repository.business.IRepositoryManager;
import com.wzitech.gamegold.repository.dao.IRepositoryRedisDAO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * 加入库存测试
 *
 * @author HeJian
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class AddRepositoryTest {

    @Autowired
    IRepositoryService repositoryService;

    @Autowired
    IRepositoryRedisDAO repositoryRedisDAO;

    @Autowired
    IRepositoryManager repositoryManager;

    @Autowired
    IOrderInfoManager orderInfoManager;

    @Autowired
    IOrderConfigManager orderConfigQuery;

    @Autowired
    IRepositorySplitRequestManager splitRequestManager;

    @Autowired
    IShGameConfigManager shGameConfigManager;

    @Autowired
    IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    IGameAccountDBDAO gam;

    @Autowired
    IConfigResultInfoDBDAO configResult;
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
     * 加入库存测试
     */
    @Test
    public void testAddRepository() {
//        Map<String,Object> quey=new HashMap<String, Object>();
//        quey.put("buyerAccount","supmj3");
//        quey.put("region","安徽区");
//        List<GameAccount> game=gam.selectByMap(quey);
//        for (GameAccount g:game){
//            g.setFreezeNeedCount(25L);
//        }
//        gam.batchAddGameAccountFreezeCount(game);

//        DeliverySubOrder deliver=deliverySubOrderDao.selectById(3385L);
//        deliver.setSplited(true);
//        deliverySubOrderDao.updateSplited(deliver);

//            Integer count=configResult.selectConfigResultWithGameRole("地下城与勇士","湖北区","湖北5区","","乡东所正南","supmj11","1794116184");
//        System.out.println(count);

    }

    //    @Autowired
//    IRepositoryManager repositoryManager;
    @Autowired
    private IUserInfoDBDAO userInfoDBDAO;

    @Test
    public void test11() {

        List<UserInfoEO> list = userInfoDBDAO.selectFreeConsignmentService();
    }

    @Test
    public void testOrderConfig() throws InterruptedException {
        UserInfoEO userInfoEO = new UserInfoEO();
        userInfoEO.setLoginAccount("wangjunjie");
        userInfoEO.setUid("US16122251120213-0720");
        for (int i = 0; i < 100; i++) {
            List<RepositoryInfo> repositoryInfoListUpload = new ArrayList<RepositoryInfo>();
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setGameName("地下城与勇士");
            repositoryInfo.setRegion("广东区");
            repositoryInfo.setServer("广东1区");
            repositoryInfo.setGameAccount("234131");
            repositoryInfo.setSellerGameRole("123123");
            repositoryInfo.setSellableCount(1000L);
            repositoryInfo.setGoldCount(1000L);
            repositoryInfo.setUnitPrice(new BigDecimal(200));
            repositoryInfo.setGoodsTypeName("游戏币");
            repositoryInfo.setMoneyName("万金");
            repositoryInfo.setGamePassWord("12313213");
            repositoryInfoListUpload.add(repositoryInfo);
            repositoryManager.addRepository(repositoryInfoListUpload, userInfoEO);
        }

//		OrderInfoEO dbOrderInfoEO = orderInfoManager.selectById("YX14022700011");
//		
//		Map<String, Object> queryMap = new HashMap<String, Object>();
//		queryMap.put("gameName", "魔兽世界(国服)");
//		queryMap.put("region", "劈成手背");
//		queryMap.put("server", "一区(北京网通)");
//		List<RepositoryInfo> repositorys = repositoryManager.queryRepository(queryMap, 1, 0, "ID", true).getData();
//		
//		if(repositorys == null || repositorys.size() == 0){
//			return;
//		}
//		
//		dbOrderInfoEO.setTradeType(1);
//		orderInfoManager.configOrder(dbOrderInfoEO, repositorys.get(0));
    }

    @Test
    public void teseQueryConfig() {
        List<ConfigResultInfoEO> configResultInfoEOs = orderConfigQuery.orderConfigList("YX14022500002");
        System.out.println(configResultInfoEOs);
    }

    @Test
    public void testRedis() {
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setGameName("魔兽世界");
        repositoryInfo.setRegion("上海二区");
        repositoryInfo.setServer("黑石塔");
        repositoryInfo.setServicerId(1000004L);

//		repositoryRedisDAO.addRepositorySum(repositoryInfo);

//		repositoryRedisDAO.queryServicerId("魔兽世界", "上海二区", "黑石塔", "", 110);
    }

    @Test
    public void uploadRepositoryTest() {

    }

    /**
     * 密保卡更新测试
     */
    @Test
    public void updatePasspodTest() {
        RepositoryInfo repositoryInfo = new RepositoryInfo();
        repositoryInfo.setAccountUid("US14022163863213-0315");
        repositoryInfo.setLoginAccount("GeiliTest");
        repositoryInfo.setGameAccount("chengfei.sun");
        repositoryInfo.setGameName("剑灵");
        repositoryInfo.setRegion("上海一区");
        repositoryInfo.setServer("服务器1");
        repositoryInfo.setPasspodUrl("/gamegold/passport/url");

//		repositoryManager.updatePasspod(repositoryInfo);
    }

    @Autowired
    IPurchaseOrderManager purchaseOrderManager;

    @Test
    public void testData() throws IOException {
        String ss = EncryptHelper.md5(String.format("%s_%s_%s_%s", "ddcef64cb8444d4d9422d062c8ae3cbe", "wjj3172088", "!rBq^EA", "5173"));
        for (int i = 0; i < 100; i++) {
            int num = (int) (Math.random() * 5);
            System.out.println(num);
        }
//        List<Long> ids = new ArrayList<Long>();
//        ids.add(4040L);
//        purchaseOrderManager.setPurchaseOrderOnline(ids, false, "yh94888");
    }


    @Autowired
    ISubOrderDetailManager subOrderDetailManager;

    @Test
    public void test() {
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("orderId", "YX1801040001318");
        queryParam.put("subOrderId", 9160738);
        queryParam.put("sellerAccount", "yangl0088");
        SubOrderDetailDTO subOrderDetail = subOrderDetailManager.querySellerOrderDetail(queryParam);
    }
}
