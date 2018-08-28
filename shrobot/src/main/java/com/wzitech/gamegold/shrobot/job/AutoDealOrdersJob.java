package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.enums.DeliveryOrderStatus;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by 339928 on 2017/12/25.
 */
@Component
public class
AutoDealOrdersJob {
    protected static final Logger logger = org.slf4j.LoggerFactory.getLogger(AutoDealOrdersJob.class);

    private static final String JOB_ID = "SH_AUTO_CONFIRM_MAIL_DELIVERY_ORDER_JOB";

    @Autowired
    IDeliverySubOrderDao deliverySubOrderDao;

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    public void autoConfig() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            logger.info("10分钟自动完单上一个任务未完成");
            return;
        }
        try {
            //查询出所有十分钟交易完成的订单进行自动完单
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("waitToConfirm", true);
            //全自动返回信息后10分钟 之后的信息
            queryMap.put("updateTimeAddTenMinute", new Date(System.currentTimeMillis() - 10 * 60 * 1000));
            List<DeliverySubOrder> deliverySubOrders = deliverySubOrderDao.selectByMap(queryMap);
            for (DeliverySubOrder deliverySubOrder : deliverySubOrders) {
                logger.info("处理未确认订单:"+deliverySubOrder.getId());
                Map<Long, Long> map = new HashMap<Long, Long>();
                map.put(deliverySubOrder.getId(), deliverySubOrder.getRealCount());
                Map<Long, String> cancelReason = new HashMap<Long, String>();
                if (deliverySubOrder.getShInputCount() == 0) {
                    cancelReason.put(deliverySubOrder.getId(), "出货商对订单无异议已逾10分钟");
                }
                deliveryOrderManager.handleOrderForMailDeliveryOrderMax(map, deliverySubOrder.getOrderId(), cancelReason, null,null);
                logger.info("订单:"+deliverySubOrder.getId()+"已自动完单完毕");
            }
        } catch (Exception e) {
            logger.error("处理未确认订单发生异常:{}", e);
            e.printStackTrace();
        } finally {
            jobLock.unlock(JOB_ID);
        }

    }


}
