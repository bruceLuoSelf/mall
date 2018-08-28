package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IFundStatisticsManager;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 资金统计JOB
 *
 * @author yemq
 * 每天0点10分统计一次
 */
@Component
@JobHandler("fundStatistics")
public class FundStatisticsJob extends IJobHandler{

    protected static final Logger logger = LoggerFactory.getLogger(FundStatisticsJob.class);

    private static final String JOB_ID = "SH_FUND_STATISTICS_JOB";

    @Autowired
    IFundStatisticsManager fundStatisticsManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            fundStatisticsManager.statistics();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("autoStatistics出错了", e);
            return FAIL;
        }
    }
}
