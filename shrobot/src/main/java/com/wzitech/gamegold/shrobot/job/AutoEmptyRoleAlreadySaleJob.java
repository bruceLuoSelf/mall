package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.shorder.business.IGameAccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ljn
 * @date 2018/6/13.
 */
@Component
public class AutoEmptyRoleAlreadySaleJob {

    @Autowired
    IGameAccountManager gameAccountManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    protected static final Logger logger = LoggerFactory.getLogger(AutoEmptyRoleAlreadySaleJob.class);

    private static final String JOB_ID = "AUTO_EMPTY_ROLE_ALREADY_SALE";

    public void autoEmptyRoleAlreadySale() {
        try{
            Boolean lock = jobLock.lock(JOB_ID);
            if (!lock) {
                logger.info("[清空已售数量JOB]上一个任务还未执行完成。");
                return;
            }
            gameAccountManager.emptyTodaySaleCount();
            logger.info("[清空已售数量JOB]任务结束。");
        }catch (Exception e){
            logger.info("清空已售数量出错");
        }finally {
            jobLock.unlock(JOB_ID);
        }
    }


}
