package com.wzitech.gamegold.facade.frontend;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.wzitech.gamegold.common.dto.BuyerSigns;
import com.wzitech.gamegold.common.dto.GoodsInfo;
import com.wzitech.gamegold.common.dto.orderPushVo;
import com.wzitech.gamegold.common.main.IGetGameIdFromMain;
import com.wzitech.gamegold.common.main.ImqUtilForOrderCenterToMain;
import com.wzitech.gamegold.facade.frontend.dto.ZBaoDTO;
import com.wzitech.gamegold.facade.frontend.utils.ConZBaoUtil;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.IOrderPushMainManager;
import com.wzitech.gamegold.order.business.impl.OrderPushMainImpl;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.dao.IOrderPushRedisDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import net.sf.json.JSONArray;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 340032 on 2017/8/19.
 * <p>
 * 测试 初始化,数据更新数据库
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:/META-INF/gold-facade-frontend-context.xml"})
@ActiveProfiles("development")
public class AgrreInitTest {
//    @Autowired
//    private ISellerManager sellerManager;
//    @Autowired
//    ConZBaoUtil conZBaoUtil;
//
//
//
//    @Autowired
//    IGetGameIdFromMain getGameIdFromMain;
//
//    @Autowired
//    IDeliveryOrderManager deliveryOrderManager;
//
//    @Autowired
//    IOrderPushRedisDao orderPushRedisDao;

    @Autowired
    IOrderPushMainManager orderPushMain;

    @Autowired
    IOrderInfoDBDAO orderInfoDBDAO;
//    @Test
//    public void test1() {
////        SellerInfo sellerInfo = sellerManager.queryloginAccountNotLike("jy05576538");
////        ZBaoDTO zBaoDTO =new ZBaoDTO();
////        List<SellerInfo> list=new ArrayList<SellerInfo>();
////        list.add(sellerInfo);
////        //判断是否已绑定用户,如果没绑定,调用7bao绑定并生成7bao账户返回商城
////        if (null == sellerInfo.getisBind() || false==sellerInfo.getisBind()){
////            sellerInfo.setisBind(false);
////            //创建7bao账号
////            zBaoDTO.setLoginAccount(sellerInfo.getLoginAccount());
////            zBaoDTO.setPhoneNumber(sellerInfo.getPhoneNumber());
////            zBaoDTO.setName(sellerInfo.getName());
////            zBaoDTO.setQq(sellerInfo.getQq());
////            zBaoDTO.setUid(sellerInfo.getUid());
////            zBaoDTO.setUserBind(sellerInfo.getisBind());
////            String zBaoAccount= conZBaoUtil.createZBaoAccount(zBaoDTO);
////            sellerInfo.setSevenBaoAccount(zBaoAccount);
////            System.out.println(zBaoAccount+"*-*-*-*-*-*-*-*-*-*-*-*-**-");
////            sellerManager.updateAgrre(list);
////        }
////        //判断该用户是否已阅读,
////        if (null == sellerInfo.getisAgree() || false==sellerInfo.getisAgree()){
////            sellerInfo.setisAgree(false);
////            sellerManager.updateAgrre(list);
////        }else {
////            sellerInfo.setisAgree(true);
////            sellerManager.updateAgrre(list);
////        }
//
//        SellerInfo sellerInfo = sellerManager.queryloginAccountNotLike("meng1990wn");
//        ZBaoDTO zBaoDTO = new ZBaoDTO();
//        List<SellerInfo> list = new ArrayList<SellerInfo>();
//        list.add(sellerInfo);
//        Boolean b1 = null;
//        //判断是否已绑定用户,如果没绑定,调用7bao绑定并生成7bao账户返回商城
//        if (null == sellerInfo.getisBind() || false == sellerInfo.getisBind()) {
//            b1 = false;
//            /*sellerInfo.setisBind(false);
//            sellerManager.updateAgrre(list);*/
////            创建7bao账号
//            zBaoDTO.setLoginAccount(sellerInfo.getLoginAccount());
//            zBaoDTO.setPhoneNumber(sellerInfo.getPhoneNumber());
//            zBaoDTO.setName(sellerInfo.getName());
//            zBaoDTO.setQq(sellerInfo.getQq());
//            zBaoDTO.setUid(sellerInfo.getUid());
//            zBaoDTO.setUserBind(sellerInfo.getisBind());
//            String zBaoAccount = conZBaoUtil.createZBaoAccount(zBaoDTO);
//            sellerInfo.setSevenBaoAccount(zBaoAccount);
//            System.out.println(zBaoAccount + "*-*-*-*-*-*-*-*-*-*-*-*-**-");
//            sellerManager.updateAgrre(list);
//        }
//        Boolean b2 = null;
//        //判断该用户是否已阅读,
//        if (null == sellerInfo.getisAgree() || false == sellerInfo.getisAgree()) {
//            b2 = false;
////            sellerInfo.setisAgree(false);
////            sellerManager.updateAgrre(list);
//        } else {
//            b2 = true;
////            sellerInfo.setisAgree(true);
////            sellerManager.updateAgrre(list);
//        }
//        sellerInfo.setisBind(b1);
//        sellerInfo.setisAgree(b2);
//        sellerManager.updateAgrre(list);
//
//    }
//
//    @Test
//    public void test2() {
//        SellerInfo sellerInfo = sellerManager.queryloginAccountNotLike("307963600");
//        List<SellerInfo> list = new ArrayList<SellerInfo>();
//        list.add(sellerInfo);
//        try {
//            //把此客户是否阅读的数据改为true
//            if (sellerInfo.getisAgree() == false) {
//                sellerInfo.setisAgree(true);
//                sellerManager.updateAgrre(list);
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Test
//    public void test3() {
//
//        //mqUtilForOrderCenterToMain.mqPushOrderToMain("333333");
//    }
//
//    @Test
//    public void test4() {
//        GoodsInfo goodsInfo = new GoodsInfo();
//        goodsInfo.setGameName("地下城与勇士");
//        goodsInfo.setRegion("浙江区");
//        goodsInfo.setServer("浙江2区");
//        GoodsInfo gameAndServer = getGameIdFromMain.getGameAndServer(goodsInfo);
//        System.out.println("--------------------------------------------------");
//        System.out.println(gameAndServer.getGameId());
//        System.out.println(gameAndServer);
//    }
//
//    @Test
//    public void test5() {
//        DeliveryOrder deliveryOrder = deliveryOrderManager.selectByOrderIdForUpdate("SH1705230000001");
//        deliveryOrderManager.orderPushToMain(deliveryOrder,1);
//    }

    @Test
    public void receivedTest(){
        try {
            // 创建连接和频道
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.130.64");
            factory.setUsername("Admin");
            factory.setPassword("123456");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

//        channel.exchangeDeclare("DataCenter.OrderInfos", "");
            // 创建一个非持久的、唯一的且自动删除的队列
            String queueName = channel.queueDeclare().getQueue();
            // 为转发器指定队列，设置binding
            channel.queueBind(queueName, "DataCenter.OrderInfos.JBSC", ""); //

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            QueueingConsumer consumer = new QueueingConsumer(channel);
            // 指定接收者，第二个参数为自动应答，无需手动应答
            channel.basicConsume(queueName, true, consumer);

            while (true) {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [x] Received '" + message + "'");
            }
        }catch (Exception e){

        }
    }


    public static void main(String[] argv) throws IOException, InterruptedException {
        // 创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("mq.5173.com");
        factory.setUsername("YXBMALL");
        factory.setPassword("h9hrH2");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

//        channel.exchangeDeclare("DataCenter.OrderInfos", "");
        // 创建一个非持久的、唯一的且自动删除的队列
        String queueName = channel.queueDeclare().getQueue();
        // 为转发器指定队列，设置binding
        channel.queueBind(queueName, "DataCenter.OrderInfos.JBSC", "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 指定接收者，第二个参数为自动应答，无需手动应答
        channel.basicConsume(queueName, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }
    }

//    @Test
//    public void testRedis1(){
////        orderPushVo pushVo = new orderPushVo();
////        pushVo.setId("233333");
//        List order = orderPushRedisDao.findOrder();
////        System.out.println(order.size()+"------------------------------");
////        System.out.println(order.toString());
//    }

    @Test
    public void removeRedis(){
        OrderInfoEO orderInfoEO =orderInfoDBDAO.queryOrderForUpdate("YX1711290008092");
       orderPushMain.orderPushMain(orderInfoEO);

//        BuyerSigns buyerSigns = new BuyerSigns();
//        buyerSigns.setId("11");
//        buyerSigns.setName("dasdsa");
//        buyerSigns.setPrice(new BigDecimal("1.25"));
//        JSONArray jsonArray = JSONArray.fromObject(buyerSigns);
//        String sign = jsonArray.toString();
//
//        System.out.println(sign+"1111111111111111111111111111111111111111");
    }

}