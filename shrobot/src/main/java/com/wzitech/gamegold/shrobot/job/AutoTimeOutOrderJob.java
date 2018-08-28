package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自动设置订单超时
 * Created by 汪俊杰 on 2017/1/6.
 */
@Component
public class AutoTimeOutOrderJob {
    protected static final Logger logger = LoggerFactory.getLogger(AutoCancelOrderJob.class);

    private static final String JOB_ID = "SH_AUTO_TIMEOUT_DELIVERY_ORDER_JOB";

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    /**
     * 自动设置订单超时，每个5分钟执行一次
     */
    public void autoTimeOutOrder() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            return;
        }
        try {
            // 查询交易超时的订单
            List<Long> ids = deliveryOrderManager.queryTimeoutOrders();
            //logger.info("[自动撤单JOB]需要配单的数量：{}", orders == null ? 0 : orders.size());
            if (!CollectionUtils.isEmpty(ids)) {
                for (Long id : ids) {
                    try {
                        deliveryOrderManager.setTimeoutOrder(id);
                    } catch (Exception e) {
                        logger.error("自动交易超时JOB出错了", e);
                    }
                }
            }
        } finally {
            jobLock.unlock(JOB_ID);
        }
    }
}
