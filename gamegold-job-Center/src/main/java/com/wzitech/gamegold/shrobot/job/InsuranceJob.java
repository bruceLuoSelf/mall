package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.order.business.IInsuranceManager;
import com.wzitech.gamegold.order.business.IInsuranceOrderManager;
import com.wzitech.gamegold.order.entity.InsuranceOrder;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 保险任务
 * 自动保单结单（每40分钟,自动保单结单）
 *
 * @author yemq
 */
@Component
@JobHandler("autoModifyTransferTime")
public class InsuranceJob extends IJobHandler{
    protected static final Log log = LogFactory.getLog(InsuranceJob.class);

    private static final String JOB_ID = "INSURANCE_JOB";

    @Autowired
    IInsuranceOrderManager insuranceOrderManager;
    @Autowired
    IInsuranceManager insuranceManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
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
                insuranceManager.modifyTransferTime(insuranceOrder);
            }
            log.info("自动保单结单定时器结束");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("autoModifyTransferTime异常:{}",e);
            return FAIL;
        }
    }
}
