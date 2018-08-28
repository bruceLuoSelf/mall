 package com.wzitech.gamegold.facade.backend.job;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.log.business.ILogManager;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

@Component
public class AutoPlayJob {
	
	protected static final Log log = LogFactory.getLog(AutoPlayJob.class);
	
	private static final String JOB_ID = "AUTO_PLAY_JOB";
	
	@Autowired
	IOrderInfoManager orderInfoManager;
	
	@Autowired
	ILogManager logManager;
	
	@Resource(name="jobLock")
	JobLockRedisImpl jobLock;
	
	/** 
     * 自动付款（每10分钟,把到付款时间的订单进行自动结单,并修改状态已结单）
     */  
    @Scheduled(cron="0 */10 * * * ?")
    public void autoPlay(){
    	Boolean locked = jobLock.lock(JOB_ID);
    	if(!locked){
			log.info("上一个定时器还未执行完成。");
			return;
		}
    	try{
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
    	}finally{
    		jobLock.unlock(JOB_ID);
    	}
    }

}
