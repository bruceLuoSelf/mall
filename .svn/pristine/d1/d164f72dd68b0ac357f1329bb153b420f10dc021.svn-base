package com.wzitech.gamegold.common.main;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wzitech.gamegold.common.utils.RedisDaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by chengXY on 2017/10/24.
 */
@Component
public class mqUtilForOrderCenterToMainImpl implements ImqUtilForOrderCenterToMain {
    private static final Logger logger = LoggerFactory.getLogger(mqUtilForOrderCenterToMainImpl.class);

    @Autowired
    RedisDaoUtil redisDaoUtil;

    @Value("${order.host}")
    private String host = "";

    @Value("${order.username}")
    private String userName = "";

    @Value("${order.password}")
    private String passWord = "";

    @Value("${order.port}")
    private String port = "";

    /**
     * 金币商城订单推送主站通用方法
     */
    @Override
    public void mqPushOrderToMain(String message) {
        Connection connection = null;
        Channel channel = null;
        try {
            //创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            //设置RabbitMQ相关信息
            factory.setHost(host);
            factory.setUsername(userName);
            factory.setPassword(passWord);
            int intPort = Integer.parseInt(port);
            factory.setPort(intPort);
            //创建一个新的连接
            connection = factory.newConnection();
            //创建一个通道
            channel = connection.createChannel();
            //发送消息到队列中
            channel.basicPublish("DataCenter.OrderInfos.JBSC", "", null, message.getBytes("UTF-8"));
            logger.info("Producer Send +'" + message + "'");

        } catch (Exception e) {
            redisDaoUtil.saveOrder(message);
            logger.error("推送金币商城订单至订单中心mq出错：" + e);
        } finally {
            try {
                //关闭通道和连接
                channel.close();
                connection.close();
            } catch (Exception e) {
                logger.error("推送金币商城订单至订单中心mq出错finally：" + e);
                e.printStackTrace();
            }
        }
    }
}
