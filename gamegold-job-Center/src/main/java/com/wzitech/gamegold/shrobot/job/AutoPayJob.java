 package com.wzitech.gamegold.shrobot.job;

 import com.wzitech.gamegold.common.context.CurrentUserContext;
 import com.wzitech.gamegold.common.enums.UserType;
 import com.wzitech.gamegold.common.log.business.ILogManager;
 import com.wzitech.gamegold.order.business.IOrderInfoManager;
 import com.wzitech.gamegold.order.entity.OrderInfoEO;
 import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
 import com.xxl.job.core.biz.model.ReturnT;
 import com.xxl.job.core.handler.IJobHandler;
 import com.xxl.job.core.handler.annotation.JobHandler;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.scheduling.annotation.Scheduled;
 import org.springframework.stereotype.Component;

 import javax.annotation.Resource;
 import java.util.List;

 /**
  * 自动付款（每10分钟,把到付款时间的订单进行自动结单,并修改状态已结单）
  */
 @Component
 @JobHandler("autoPay")
 public class AutoPayJob extends IJobHandler{

     protected static final Log log = LogFactory.getLog(AutoPayJob.class);

     private static final String JOB_ID = "AUTO_PLAY_JOB";

     @Autowired
     IOrderInfoManager orderInfoManager;

     @Autowired
     ILogManager logManager;

     @Resource(name="jobLock")
     JobLockRedisImpl jobLock;

     @Scheduled(cron="0 */10 * * * ?")

     @Override
     public ReturnT<String> execute(String s) throws Exception {
         try {
             //自动付款设置一个自动付款的用户信息
             UserInfoEO user = new UserInfoEO();
             user.setId(-1L);
             user.setLoginAccount("autoPay");
             user.setUserType(UserType.System.getCode());
             CurrentUserContext.setUser(user);
             log.info("自动付款定时器开始");
             //查询已发货的订单，但是还未结单的订单进行自动付款，并结单
             List<OrderInfoEO> deliveryOrders = orderInfoManager.queryOrderForAutoPlay();
             for(OrderInfoEO order : deliveryOrders){
                 try{
                     orderInfoManager.autoPay(order);
                 }catch (Exception e) {
                     log.info(e);
                 }
             }
             log.info("自动付款定时器结束");
             return SUCCESS;
         } catch (Exception e) {
             e.printStackTrace();
             log.info("autoPay异常:{}",e);
             return FAIL;
         }
     }
 }
