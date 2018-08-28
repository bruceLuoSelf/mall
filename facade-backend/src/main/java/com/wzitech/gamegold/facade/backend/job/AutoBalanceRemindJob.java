 package com.wzitech.gamegold.facade.backend.job;

 import com.wzitech.chaos.framework.server.common.exception.SystemException;
 import com.wzitech.gamegold.common.context.CurrentUserContext;
 import com.wzitech.gamegold.common.enums.ResponseCodes;
 import com.wzitech.gamegold.common.enums.SystemConfigEnum;
 import com.wzitech.gamegold.common.enums.UserType;
 import com.wzitech.gamegold.common.log.business.ILogManager;
 import com.wzitech.gamegold.order.business.IAutoConfigManager;
 import com.wzitech.gamegold.order.business.IOrderInfoManager;
 import com.wzitech.gamegold.shorder.business.IDeliveryOrderManager;
 import com.wzitech.gamegold.shorder.business.IPurchaserDataManager;
 import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
 import com.wzitech.gamegold.shorder.entity.SystemConfig;
 import com.wzitech.gamegold.shorder.utils.SevenBaoFund;
 import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
 import org.apache.commons.lang3.StringUtils;
 import org.apache.commons.logging.Log;
 import org.apache.commons.logging.LogFactory;
 import org.apache.poi.util.StringUtil;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.scheduling.annotation.Scheduled;
 import org.springframework.stereotype.Component;

 import javax.annotation.Resource;
 import java.math.BigDecimal;

 @Component
 public class AutoBalanceRemindJob {

     protected static final Log log = LogFactory.getLog(AutoBalanceRemindJob.class);

     private static final String JOB_ID_REMIND = "AUTO_BALANCE_REMIND_JOB";

     private static final String JOB_ID_STOP_SH = "AUTO_BALANCE_STOP_SH_JOB";



     @Autowired
	 IPurchaserDataManager purchaserDataManager;

     @Autowired
	 ISystemConfigManager systemConfigManager;

     @Autowired
     ILogManager logManager;

     @Autowired
     SevenBaoFund sevenBaoFund;

     @Resource(name="jobLock")
     JobLockRedisImpl jobLock;

     /**
      * 余额低于数值，自动提醒（每30分钟检查一次）
      */
     @Scheduled(cron="0 */30 * * * ?")
     public void autoBalanceRemind(){
         Boolean locked = jobLock.lock(JOB_ID_REMIND);
         if(!locked){
             log.info("上一个定时器还未执行完成。");
             return;
         }
         try{
             //自动付款设置一个自动付款的用户信息
             UserInfoEO user = new UserInfoEO();
             user.setId(-1L);
             user.setLoginAccount("autoBalanceRemind");
             user.setUserType(UserType.System.getCode());
             CurrentUserContext.setUser(user);
             log.info("余额低于数值，自动提醒定时器开始");
			 SystemConfig config = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_REMIND_LINE.getKey());

			 if(config==null || StringUtils.isBlank(config.getConfigValue()) || config.getEnabled()==false){
				 log.info("未配置余额提醒线");
				 return;
			 }
			 BigDecimal remindLine = new BigDecimal(config.getConfigValue());
			 purchaserDataManager.autoBalanceRemind(remindLine);

             log.info("余额低于数值，自动提醒定时器结束");
         }finally{
             jobLock.unlock(JOB_ID_REMIND);
         }

     }
     /**
      * 余额低于数值，自动停止收货（每5分钟执行一次）
      */
     @Scheduled(cron="0 */5 * * * ?")
     public void autoBalanceStopSh(){
         Boolean locked = jobLock.lock(JOB_ID_STOP_SH);
         if(!locked){
             log.info("上一个定时器还未执行完成。");
             return;
         }
         try{
             //自动付款设置一个自动付款的用户信息
             UserInfoEO user = new UserInfoEO();
             user.setId(-1L);
             user.setLoginAccount("autoBalanceStopSh");
             user.setUserType(UserType.System.getCode());
             CurrentUserContext.setUser(user);
             log.info("余额低于数值，自动停止收货定时器开始");
			 SystemConfig config = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.BALANCE_STOP_LINE.getKey());

			 if(config==null || StringUtils.isBlank(config.getConfigValue()) || config.getEnabled()==false){
				 log.info("未配置余额停止收货线");
				 return;
			 }

			 BigDecimal stopLine = new BigDecimal(config.getConfigValue());
             int oldFundOrNewFund = 1;
			 purchaserDataManager.autoBalanceStopSh(stopLine,oldFundOrNewFund);

             //获取新资金最低收获金配额
             SystemConfig fund = sevenBaoFund.createFund();
             if (fund==null){
                 log.info("可用收货金配置不能为空:{}"+fund);
                 throw new SystemException(ResponseCodes.Configuration.getCode(), ResponseCodes.Configuration.getMessage());
             }
             String configValue = fund.getConfigValue();
             BigDecimal stopLineNewFund = new BigDecimal(configValue);
             oldFundOrNewFund = 2;
             purchaserDataManager.autoBalanceStopSh(stopLineNewFund,oldFundOrNewFund);
             log.info("余额低于数值，自动停止收货定时器结束");
         }finally{
             jobLock.unlock(JOB_ID_STOP_SH);
         }

     }

 }
