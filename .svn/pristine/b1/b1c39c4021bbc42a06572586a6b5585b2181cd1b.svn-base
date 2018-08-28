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
 * 自动转账JOB
 *
 * @author yemq
 */
@Component
public class AutoTransferJob {
    protected static final Logger logger = LoggerFactory.getLogger(AutoTransferJob.class);

    private static final String JOB_ID = "SH_AUTO_TRANSFER_JOB";

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    /**
     * 自动付款给出货方，每隔5分钟执行一次
     */
    public void autoTransfer() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            //logger.info("[自动付款给出货方JOB]上一个任务还未执行完成。");
            return;
        }
        try {
            //logger.info("[自动付款给出货方JOB]任务开始");
            // 查询等待转账的订单
            List<String> orderIds = deliveryOrderManager.queryWaitTransferOrderIds();
//            logger.info("[自动付款给出货方JOB]需要配单的数量：{}", orders == null ? 0 : orders.size());
            if (!CollectionUtils.isEmpty(orderIds)) {
                for (String orderId : orderIds) {
                    try {
                        deliveryOrderManager.transfer(orderId);
                    } catch (Exception e) {
                        logger.error("自动付款给出货方JOB出错了", e);
                    }
                }
            }
            //logger.info("[自动付款给出货方JOB]任务结束");
        } finally {
            jobLock.unlock(JOB_ID);
        }
    }
}
