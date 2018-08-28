package com.wzitech.gamegold.facade.backend.job;

import javax.annotation.Resource;

import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.log.business.ILogManager;
import com.wzitech.gamegold.order.business.IAutoConfigManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.impl.OrderInfoManagerImpl;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

@Component
public class AutoCancellJob {

    protected static final Log log = LogFactory.getLog(AutoCancellJob.class);

    private static final String JOB_ID_CON = "AUTO_CONFIRMATIONPAY_JOB";

    private static final String JOB_ID_CAN = "AUTO_CANCELL_JOB";

    private static final String JOB_ID_TRAN = "AUTO_TRANS_JOB";


    @Autowired
    IOrderInfoManager orderInfoManager;

    @Autowired
    IAutoConfigManager autoConfigManager;

    @Autowired
    ILogManager logManager;

    @Autowired
    ISystemConfigManager systemConfigManager;

    @Resource(name = "jobLock")
    JobLockRedisImpl jobLock;

    @Value(value = "${order.autocancell.time}")
    Integer autoCancellTime = 1800;

    @Value(value = "${order.confirmationpay.time}")
    Integer confirmationpayTime = 5;

    /**
     * 自动确认未支付的订单（每5分钟确认一次,把5分钟之前的未支付但实际买家已支付的订单状态修改为已支付）
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void autoConfirmationPay() {
        Boolean locked = jobLock.lock(JOB_ID_CON);
        if (!locked) {
            log.info("上一个定时器还未执行完成。");
            return;
        }
        try {
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoConfirmPay");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);

            SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.COMPENSATE_ORDER_TIME.getKey());
            if (systemConfig != null && systemConfig.getEnabled()) {
                confirmationpayTime =Integer.valueOf(systemConfig.getConfigValue());
            }

            log.info("自动确认未支付的订单定时器开始");
            orderInfoManager.autoConfirmationPayTimeoutOrder(confirmationpayTime);
            log.info("自动确认未支付的订单定时器结束");
        } finally {
            jobLock.unlock(JOB_ID_CON);
        }

    }

    /**
     * 自动取消未支付的订单（每一30分钟删除一次,把30分钟之前的订单状态修改为已取消）
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void autoCancell() {
        Boolean locked = jobLock.lock(JOB_ID_CAN);
        if (!locked) {
            log.info("上一个定时器还未执行完成。");
            return;
        }
        try {
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoCancell");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);
            log.info("自动取消未付款订单定时器开始");
            orderInfoManager.autoCancellTimeoutOrder(autoCancellTime);
            log.info("自动取消未付款订单定时器结束");
        } finally {
            jobLock.unlock(JOB_ID_CAN);
        }

    }

    /**
     * 自动转人工操作的订单（每一5分钟检查一次,把5分钟之前的订单修改成人工操作状态）
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void autoTrans() {
        Boolean locked = jobLock.lock(JOB_ID_TRAN);
        if (!locked) {
            log.info("上一个定时器还未执行完成。");
            return;
        }
        try {
            //自动转人工操作设置一个用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoTrans");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);
            log.info("自动转人工操作订单定时器开始");
            autoConfigManager.autoTrans(300);
            log.info("自动转人工操作订单定时器结束");
        } finally {
            jobLock.unlock(JOB_ID_TRAN);
        }

    }

}
