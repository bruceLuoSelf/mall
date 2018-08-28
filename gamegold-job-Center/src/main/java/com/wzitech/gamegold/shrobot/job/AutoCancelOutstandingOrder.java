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
 * @author ljn
 * @date 2018/5/29.
 * 自动取消未支付的订单（每一30分钟删除一次,把30分钟之前的订单状态修改为已取消）
 *
 */
@Component
@JobHandler("autoCancelOutstandingOrder")
public class AutoCancelOutstandingOrder extends IJobHandler {

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    IOrderInfoManager orderInfoManager;

    @Value(value="${order.autocancell.time}")
    Integer autoCancelTime = 1800;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("autoCancell");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);
            log.info("自动取消未付款订单定时器开始");
            orderInfoManager.autoCancellTimeoutOrder(autoCancelTime);
            log.info("自动取消未付款订单定时器结束");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("autoCancelOutstandingOrder出现异常:{}",e);
            return FAIL;
        }
    }
}
