package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by 汪俊杰 on 2016/12/17.
 * 5分钟执行一次充值单补单
 */
@Component
@JobHandler("autoPayOrderInSh")
public class AutoPayOrderInShJob extends IJobHandler{

    protected static final Log log = LogFactory.getLog(AutoPayOrderInShJob.class);

    private static final String JOB_ID_CON = "AUTO_PAYORDERINSH_JOB";

    @Value(value="${order.confirmationpay.time}")
    Integer confirmationpayTime = 600;

    @Resource(name="jobLock")
    JobLockRedisImpl jobLock;

    @Autowired
    private IFundManager fundManager;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoConfirmPay");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);
            log.info("自动确认未支付的充值单定时器开始");
            fundManager.autoConfirmationPayTimeoutOrder(confirmationpayTime);
            log.info("自动确认未支付的充值单定时器结束");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("autoPayOrderInSh异常:{}",e);
            return FAIL;
        }
    }
}
