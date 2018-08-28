package com.wzitech.gamegold.shrobot.job;


import com.wzitech.gamegold.facade.backend.business.ICompensateOrderManager;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by chengXY on 2017/11/1.
 * 对推送订单中心mq失败的订单 进行job补偿再次推送
 */
@Component
@JobHandler("compensateOrder")
public class CompensateOrderJob extends IJobHandler{
    protected static final Logger logger = LoggerFactory.getLogger(CompensateOrderJob.class);

    private static final String JOB_ID ="COMPENSATE_ORDER_JOB";

    @Autowired
    private ICompensateOrderManager compensaterOrderManagerImpl;


    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            compensaterOrderManagerImpl.compensateOrderToOrderCenter();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("compensateOrder异常:{}",e);
            return FAIL;
        }
    }
}

