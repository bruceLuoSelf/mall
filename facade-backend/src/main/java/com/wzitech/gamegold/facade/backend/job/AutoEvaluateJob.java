package com.wzitech.gamegold.facade.backend.job;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import com.wzitech.gamegold.order.business.IServiceEvaluateManager;
import com.wzitech.gamegold.order.business.IServiceEvaluateStatisticsManager;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易成功超过4小时未评价的，自动默认好评
 * @author yemq
 */
@Component
public class AutoEvaluateJob {
    protected static final Logger logger = LoggerFactory.getLogger(AutoEvaluateJob.class);

    private static final String JOB_ID = "AUTO_EVALUATE_JOB";

    private static final String JOB_ID_STATISTICS = "AUTO_EVALUATE_STATISTICS_JOB";

    @Autowired
    IOrderInfoManager orderInfoManager;
    @Autowired
    IServiceEvaluateManager serviceEvaluateManager;
    @Autowired
    IServiceEvaluateStatisticsManager serviceEvaluateStatisticsManager;
    @Autowired
    IUserInfoManager userInfoManager;

    @Resource(name="jobLock")
    JobLockRedisImpl jobLock;

    /**
     * 交易成功超过4小时未评价的，自动默认好评
     */
    @Scheduled(cron="0 */30 * * * ?")
    public void autoEvaluate(){
        Boolean locked = jobLock.lock(JOB_ID);
        if(!locked){
            logger.info("上一个定时器还未执行完成。");
            return;
        }
        try{
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
                    try {
                        serviceEvaluateManager.add(orderId, 3, 3, 3, "默认好评", true);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            logger.info("自动评价定时器结束");
        }finally{
            jobLock.unlock(JOB_ID);
        }
    }

    /**
     * 每天12点定时统计客服评价数据
     */
    @Scheduled(cron="0 0 12 * * ?")
    public void statistics() {
        Boolean locked = jobLock.lock(JOB_ID_STATISTICS);
        if(!locked){
            logger.info("评价统计上一个定时器还未执行完成。");
            return;
        }
        try{
            logger.info("评价统计定时器开始");
            // 删除系统中已经被删除的客服账号的评价记录
            serviceEvaluateStatisticsManager.deleteNotExistServices();

            // 查出所有的担保客服
            Map<String, Object> params = new HashMap<String, Object>();
            List<UserInfoEO> serviceList = userInfoManager.findServicers(params, "ID", true);
            if (CollectionUtils.isEmpty(serviceList)) return;
            params = new HashMap<String, Object>();
            for (UserInfoEO service : serviceList) {
                params.put("serviceId", service.getId());
                GenericPage<ServiceEvaluateStatistics> page = serviceEvaluateManager.statistics(params, 1, 0, "ID", true);
                if (page.hasData()) {
                    ServiceEvaluateStatistics statistics = page.getData().get(0);
                    serviceEvaluateStatisticsManager.update(statistics);
                }
            }
            logger.info("评价统计定时器结束");
        }finally{
            jobLock.unlock(JOB_ID_STATISTICS);
        }

    }
}
