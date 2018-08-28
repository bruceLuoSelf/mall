package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IFundStatisticsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 资金统计JOB
 *
 * @author yemq
 */
@Component
public class FundStatisticsJob {
    protected static final Logger logger = LoggerFactory.getLogger(FundStatisticsJob.class);

    private static final String JOB_ID = "SH_FUND_STATISTICS_JOB";

    @Autowired
    IFundStatisticsManager fundStatisticsManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    /**
     * 每天0点10分统计一次
     */
    public void autoStatistics() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            //logger.info("[资金统计JOB]上一个任务还未执行完成。");
            return;
        }
        try {
            //logger.info("[资金统计JOB]任务开始");
            fundStatisticsManager.statistics();
            //logger.info("[资金统计JOB]任务结束");
        } catch (Exception e) {
            logger.error("资金统计JOB出错了", e);
        } finally {
            jobLock.unlock(JOB_ID);
        }
    }
}
