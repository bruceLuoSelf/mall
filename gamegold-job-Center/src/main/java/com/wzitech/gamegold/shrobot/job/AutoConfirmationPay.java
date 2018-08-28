package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 340082 on 2018/5/29.
 */
@Component
@JobHandler("autoConfirmationPay")
public class AutoConfirmationPay extends IJobHandler {

    @Autowired
    IOrderInfoManager orderInfoManager;

    @Value(value="${order.autocancell.time}")
    Integer autoCancellTime = 1800;

    @Value(value="${order.confirmationpay.time}")
    Integer confirmationpayTime = 600;

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public ReturnT<String> execute(String s)  {
        try {
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoConfirmPay");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);
            log.info("自动确认未支付的订单定时器开始");
            orderInfoManager.autoConfirmationPayTimeoutOrder(confirmationpayTime);
            log.info("自动确认未支付的订单定时器结束");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("autoConfirmationPay出现异常：{}",e);
            return FAIL;
        }
    }
}
