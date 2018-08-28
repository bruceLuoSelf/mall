package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@JobHandler("autoTimeOutOrder")
public class AutoTimeOutOrderJob extends IJobHandler{

    protected final Log logger = LogFactory.getLog(getClass());

    private static final String JOB_ID = "SH_AUTO_TIMEOUT_DELIVERY_ORDER_JOB";

    @Autowired
    IDeliveryOrderManager deliveryOrderManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            // 查询交易超时的订单
            List<Long> ids = deliveryOrderManager.queryTimeoutOrders();
            if (!CollectionUtils.isEmpty(ids)) {
                for (Long id : ids) {
                    deliveryOrderManager.setTimeoutOrder(id);
                }
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("autoTimeOutOrder异常:{}", e);
            return FAIL;
        }
    }
}
