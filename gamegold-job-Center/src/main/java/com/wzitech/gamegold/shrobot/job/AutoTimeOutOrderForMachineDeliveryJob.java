package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IDeliveryOrderLogManager;
import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.wzitech.gamegold.shorder.business.IDeliverySubOrderManager;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 340096
 * @date 2017/12/25.
 * 自动设置机器收货订单超时，每隔1分钟执行一次
 */
@Component
@JobHandler("autoTimeOutOrderForMachineDeliveryJob")
public class AutoTimeOutOrderForMachineDeliveryJob extends IJobHandler{

    protected static final Logger logger = LoggerFactory.getLogger(AutoTimeOutOrderForMachineDeliveryJob.class);

    private static final String JOB_ID = "SH_AUTO_TIMEOUT_MACHINE_DELIVERY_ORDER_JOB";

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;
    @Autowired
    IDeliverySubOrderManager deliverySubOrderManager;
    @Autowired
    IDeliveryOrderLogManager orderLogManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logger.info("超时转物服订单处理" + new Date());
            List<Long> subIds = deliverySubOrderManager.queryMachineSellerDeliveryTimeoutOrders();
            if (!CollectionUtils.isEmpty(subIds)) {
                for (Long orderId : subIds) {
                    try {
                        Boolean flag = orderLogManager.isHaveLog(orderId);
                        if (flag) {
                            logger.error("检测机器收货订单，交易中是否超时JOB出错了", orderId);
                            continue;
                        }
                        //邮寄收货自动化超时转物服处理
                        Integer exceptionFromAuto = 2;
                        String otherReason = "由于系统长时间未响应，无法完成自动收货，订单转人工处理。";
                        deliveryOrderManager.subOrderAutoDistributionManager(orderId, exceptionFromAuto,otherReason);
                    } catch (Exception e) {
                        logger.error("检测机器收货自动化响应超时订单，交易中是否超时JOB出错了", e);
                    }
                }
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("autoTimeOutOrderForMachineDeliveryJob异常：{}",e);
            return FAIL;
        }
    }
}
