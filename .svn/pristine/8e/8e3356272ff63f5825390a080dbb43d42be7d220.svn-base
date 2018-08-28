package com.wzitech.gamegold.shorder;

import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.shorder.business.*;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
//import com.wzitech.gamegold.shorder.entity.RepositoryConfine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单完成测试
 *
 * @author yemq
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-shorder-context.xml"})
@ActiveProfiles("development")
public class DeliveryOrderTest {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryOrderTest.class);

    @Autowired
    IDeliveryOrderFinishManager deliveryOrderFinishManager;
    @Autowired
    IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    IFundStatisticsManager fundStatisticsManager;
    @Autowired
    IDeliveryOrderAutoConfigManager autoConfigManager;
    @Autowired
    ISplitRepositoryRequestManager splitRepositoryRequestManager;
    @Autowired
    IShGameConfigManager shGameConfigManager;
//    @Autowired
//    IRepositoryConfigManager repositoryConfigManager;
    /**
     * 自动配单
     */
    @Test
    public void autoConfig() {
        String orderId = "SH1612230000019";
        DeliveryOrder order = deliveryOrderManager.getByOrderId(orderId);
        autoConfigManager.autoConfigOrderReady(order.getId());
    }

    /**
     * 完单
     */
    @Test
    public void finish() throws IOException {
        String mainOrderId = "SH1612210000007";
        long subOrderId = 14;
        int type = 100;
        long quantity = 1000;
        String remark = "交易成功";
//        deliveryOrderFinishManager.finish(mainOrderId, subOrderId, type, quantity, remark);
    }

    /**
     * 创建分仓订单
     */
    @Test
    public void createSplitRepoOrder() {
//        // 查询该订单下的账号信息
//        List<DeliverySubOrder> list = deliverySubOrderDao.queryAllByOrderId("SH1612270000018");
//        for (DeliverySubOrder subOrder : list) {
//            // 交易完成后，生成分仓请求
//            splitRepositoryRequestManager.create(subOrder);
//        }
    }

    /**
     * 转账
     */
    @Test
    public void transfer() {
        String orderId = "SH1804250000004";
        deliveryOrderManager.transfer(orderId);
    }

    /**
     * 资金平衡表统计
     */
    @Test
    public void fundStatistics() {
        fundStatisticsManager.statistics();
    }

//    @Test
//    public void test8(){
//        List<RepositoryConfine> list = repositoryConfigManager.queryRepositoryList(null,25,0,"id",true).getData();
//        for (RepositoryConfine entity:list){
//            System.out.println(entity.getGameName()+","+entity.getId());
//        }
//
//    }

    /**
     * 资金平衡表统计
     */
    @Test
    public void testRedis() {
        shGameConfigManager.getConfigByGameName("地下城与勇士", ServicesContants.GOODS_TYPE_GOLD, null, null);
    }


    /**
     * 发送http请求 冻结7bao资金
     * */
    @Test
    public void  testFreeze(){
        String url = "http://192.168.42.135:8080/7Bao-frontend/services/updatefund/changefund";
        String account = "cxy@5173.com";
        String uid = "123456";
        BigDecimal amount = new BigDecimal("20");
        Integer yesOrNo = 1;
       // deliveryOrderManager.freezeAmountZBao(account,uid,amount,yesOrNo,url);
    }
}
