package com.wzitech.gamegold.facade.backend.job;


import com.wzitech.gamegold.facade.backend.business.ICompensateOrderManager;
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
public class CompensateOrderJob {
    protected static final Logger logger = LoggerFactory.getLogger(CompensateOrderJob.class);

    private static final String JOB_ID ="COMPENSATE_ORDER_JOB";

    @Autowired
    private ICompensateOrderManager compensaterOrderManagerImpl;


    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    public void compensateOrder() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            logger.info("上一个补偿订单CompensateOrderJob尚未结束");
            return;
        }
        try {
            compensaterOrderManagerImpl.compensateOrderToOrderCenter();
        } finally {
            jobLock.unlock(JOB_ID);
        }

    }
}

