package com.wzitech.gamegold.shrobot.job;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.IServiceEvaluateManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author ljn
 * @date 2018/5/29.
 */
@Component
@JobHandler("autoEvaluate")
public class AutoEvaluate extends IJobHandler {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    IOrderInfoManager orderInfoManager;

    @Autowired
    IServiceEvaluateManager serviceEvaluateManager;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            //自动付款设置一个自动付款的用户信息
            UserInfoEO user = new UserInfoEO();
            user.setId(-1L);
            user.setLoginAccount("auto_evaluate_job");
            user.setUserType(UserType.System.getCode());
            CurrentUserContext.setUser(user);

            logger.info("自动评价定时器开始");
            // 查询指定订单状态下，超过4个小时未评价的订单号
            List<String> orderIds = orderInfoManager.queryAfter4HourNotEvaluateOrders();
            if (!CollectionUtils.isEmpty(orderIds)) {
                for (String orderId : orderIds) {
                    serviceEvaluateManager.add(orderId, 3, 3, 3, "默认好评", true);
                }
            }
            logger.info("自动评价定时器结束");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("autoEvaluate异常:{}",e);
            return FAIL;
        }
    }
}
