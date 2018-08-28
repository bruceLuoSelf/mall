package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
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
 * 自动付款给出货方，每隔5分钟执行一次
 */
@Component
@JobHandler("autoTransfer")
public class AutoTransferJob extends IJobHandler{
    protected static final Logger logger = LoggerFactory.getLogger(AutoTransferJob.class);

    private static final String JOB_ID = "SH_AUTO_TRANSFER_JOB";

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            // 查询等待转账的订单
            List<String> orderIds = deliveryOrderManager.queryWaitTransferOrderIds();
            if (!CollectionUtils.isEmpty(orderIds)) {
                for (String orderId : orderIds) {
                    deliveryOrderManager.transfer(orderId);
                }
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("autoTransfer出错了：{}", e);
            return FAIL;
        }
    }
}
