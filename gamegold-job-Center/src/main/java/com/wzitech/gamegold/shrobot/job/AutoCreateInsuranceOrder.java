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

import java.util.List;

/**
 * @author ljn
 * @date 2018/5/29.
 * 自动创建保单（每35分钟,自动创建保单）
 */
@Component
@JobHandler("autoCreateInsuranceOrder")
public class AutoCreateInsuranceOrder extends IJobHandler{

    protected final Log log = LogFactory.getLog(getClass());

    @Autowired
    IInsuranceOrderManager insuranceOrderManager;

    @Autowired
    IInsuranceManager insuranceManager;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
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
                insuranceManager.createBQOrder(insuranceOrder);
            }
            log.info("自动创建保单定时器结束");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("autoCreateInsuranceOrder异常:{}",e);
            return FAIL;
        }
    }
}
