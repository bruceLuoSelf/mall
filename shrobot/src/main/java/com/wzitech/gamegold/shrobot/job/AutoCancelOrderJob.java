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
 * 自动取消交易超时的订单
 */
@Component
public class AutoCancelOrderJob {
    protected static final Logger logger = LoggerFactory.getLogger(AutoCancelOrderJob.class);

    private static final String JOB_ID = "SH_AUTO_CANCEL_DELIVERY_ORDER_JOB";

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    /**
     * 自动撤销交易超时的订单，每隔2分钟执行一次
     */
    public void autoCancelTimoutOrders() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            //logger.info("[自动撤单JOB]上一个任务还未执行完成。");
            return;
        }
        try {
            //logger.info("[自动撤单JOB]任务开始");
            // 查询交易超时的订单
            List<Long> ids = deliveryOrderManager.queryTradeTimeoutOrders();
            //logger.info("[自动撤单JOB]需要配单的数量：{}", orders == null ? 0 : orders.size());
            if (!CollectionUtils.isEmpty(ids)) {
                for (Long id : ids) {
                    try {
                        deliveryOrderManager.cancelTimeoutOrder(id);
                    } catch (Exception e) {
                        logger.error("自动撤单JOB出错了", e);
                    }
                }
            }
            //logger.info("[自动撤单JOB]任务结束");
        } finally {
            jobLock.unlock(JOB_ID);
        }
    }
}
