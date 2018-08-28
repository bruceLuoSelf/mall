package com.wzitech.gamegold.facade.backend.job;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.shorder.business.IFundManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by 汪俊杰 on 2016/12/17.
 */
@Component
public class AutoPayOrderInShJob {
    protected static final Log log = LogFactory.getLog(AutoPayOrderInShJob.class);

    private static final String JOB_ID_CON = "AUTO_PAYORDERINSH_JOB";

    @Value(value="${order.confirmationpay.time}")
    Integer confirmationpayTime = 600;

    @Resource(name="jobLock")
    JobLockRedisImpl jobLock;

    @Autowired
    private IFundManager fundManager;

    /**
     * 5分钟执行一次充值单补单
     */
    @Scheduled(cron="0 */5 * * * ?")
    public void autoPayOrderInSh(){
        Boolean locked = jobLock.lock(JOB_ID_CON);
        if(!locked){
            log.info("上一个定时器还未执行完成。");
            return;
        }
        try{
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoConfirmPay");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);
            log.info("自动确认未支付的充值单定时器开始");
            fundManager.autoConfirmationPayTimeoutOrder(confirmationpayTime);
            log.info("自动确认未支付的充值单定时器结束");
        }finally{
            jobLock.unlock(JOB_ID_CON);
        }

    }
}
