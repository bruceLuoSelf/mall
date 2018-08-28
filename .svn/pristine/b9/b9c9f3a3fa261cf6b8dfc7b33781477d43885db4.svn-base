package com.wzitech.gamegold.facade.backend.job;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.order.business.IInsuranceManager;
import com.wzitech.gamegold.order.business.IInsuranceOrderManager;
import com.wzitech.gamegold.order.entity.InsuranceOrder;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 保险任务
 *
 * @author yemq
 */
@Component
public class InsuranceJob {
    protected static final Log log = LogFactory.getLog(InsuranceJob.class);

    private static final String JOB_ID = "INSURANCE_JOB";

    @Autowired
    IInsuranceOrderManager insuranceOrderManager;
    @Autowired
    IInsuranceManager insuranceManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    /**
     * 自动创建保单（每35分钟,自动创建保单）
     */
    @Scheduled(cron = "0 */35 * * * ?")
    public void autoCreateBQ() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            log.info("上一个定时器还未执行完成。");
            return;
        }
        try {
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoCreateBQ");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);

            log.info("自动创建保单定时器开始");

            //查询需要创建保单的订单记录
            List<InsuranceOrder> orders = insuranceOrderManager.queryNeedCreateBQList();
            for (InsuranceOrder insuranceOrder : orders) {
                try {
                    insuranceManager.createBQOrder(insuranceOrder);
                } catch (Exception e) {
                    log.error("调用保单创建接口出错了", e);
                }
            }
            log.info("自动创建保单定时器结束");
        } finally {
            jobLock.unlock(JOB_ID);
        }
    }

    /**
     * 自动保单结单（每40分钟,自动保单结单）
     */
    @Scheduled(cron = "0 */40 * * * ?")
    public void autoModifyTransferTime() {
        Boolean locked = jobLock.lock(JOB_ID);
        if (!locked) {
            log.info("上一个定时器还未执行完成。");
            return;
        }
        try {
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoModifyTransferTime");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);

            log.info("自动保单结单定时器开始");

            //查询需要移交保单的订单记录
            List<InsuranceOrder> orders = insuranceOrderManager.queryNeedModifyTransferTimeList();
            for (InsuranceOrder insuranceOrder : orders) {
                try {
                    insuranceManager.modifyTransferTime(insuranceOrder);
                } catch (Exception e) {
                    log.error("调用保单结单接口出错了", e);
                }
            }
            log.info("自动保单结单定时器结束");
        } finally {
            jobLock.unlock(JOB_ID);
        }
    }
}
