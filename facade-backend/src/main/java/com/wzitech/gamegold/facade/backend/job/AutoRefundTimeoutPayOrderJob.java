package com.wzitech.gamegold.facade.backend.job;

import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.repository.business.IShrobotRefundOrderManager;
import com.wzitech.gamegold.shorder.business.IPayOrderManager;
import com.wzitech.gamegold.shorder.business.IRefundOrderManager;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自动退款超过三个月的充值订单
 * Created by 汪俊杰 on 2017/6/15.
 */
@Component("autoRefundTimeoutPayOrderJob")
public class AutoRefundTimeoutPayOrderJob {
    protected static final Logger logger = LoggerFactory.getLogger(AutoRefundTimeoutPayOrderJob.class);

    private static final String JOB_ID = "AUTO_REFUND_TIMEOUT_PAY_ORDER_JOB";

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Autowired
    IPayOrderManager payOrderManager;
    @Autowired
    IRefundOrderManager refundOrderManager;
    @Autowired
    ISystemConfigManager systemConfigManager;
    @Autowired
    IShrobotRefundOrderManager shrobotRefundOrderManager;
    /**
     * 自动退充值订单
     */
    public void autoRefundTimeoutPayOrder() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            logger.info("[自动退充值订单JOB]上一个任务还未执行完成。");
            return;
        }
        try {
            logger.info("[自动退充值订单JOB]任务开始");
            SystemConfig automateTimeout = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.AUTO_TIMEOUT_PAY_ORDER.getKey());
            if (automateTimeout == null || !automateTimeout.getEnabled()) {
                return;
            }
            String configValue = automateTimeout.getConfigValue();
            int day = Integer.parseInt(configValue);//超过多少天自动退款

            List<PayOrder> list = payOrderManager.queryTimeoutPayOrders(day);
            if (list != null && list.size() > 0) {
                for (PayOrder payOrder : list) {
                    try {
                        shrobotRefundOrderManager.autoRefundTimeoutPayOrder(payOrder.getOrderId(), payOrder.getAccount(), day);
                    } catch (Exception ex) {
                        logger.info("[自动退充值订单JOB]报错," + "订单号：" + payOrder.getOrderId() + ":" + ex.toString());
                    }
                }
            }

            logger.info("[自动退充值订单JOB]任务结束");
        } finally {
            jobLock.unlock(JOB_ID);
        }
    }
}
