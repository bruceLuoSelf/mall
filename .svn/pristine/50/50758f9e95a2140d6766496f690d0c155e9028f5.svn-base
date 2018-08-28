 package com.wzitech.gamegold.shrobot.job;

 import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.log.business.ILogManager;
import com.wzitech.gamegold.order.business.IAutoConfigManager;
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

import javax.annotation.Resource;

 @Component
 @JobHandler(value="autoCancellJobAutoTrans")
 public class AutoCancellJob extends IJobHandler {

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

     @Resource(name="jobLock")
     JobLockRedisImpl jobLock;

     @Value(value="${order.autocancell.time}")
     Integer autoCancellTime = 1800;

     @Value(value="${order.confirmationpay.time}")
     Integer confirmationpayTime = 600;

     @Override
     public ReturnT<String> execute(String s) throws Exception {
         //自动转人工操作设置一个用户信息
         UserInfoEO user = new UserInfoEO();
         user.setId(-1L);
         user.setLoginAccount("autoTrans");
         user.setUserType(UserType.System.getCode());
         CurrentUserContext.setUser(user);
         log.info("自动转人工操作订单定时器开始");
         autoConfigManager.autoTrans(300);
         log.info("自动转人工操作订单定时器结束");
         return SUCCESS;
     }
 }
