package com.wzitech.gamegold.shrobot.job;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.business.IServiceEvaluateManager;
import com.wzitech.gamegold.order.business.IServiceEvaluateStatisticsManager;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ljn
 * @date 2018/5/29.
 * 每天12点定时统计客服评价数据
 */
@Component
@JobHandler("autoStatistics")
public class AutoStatistics extends IJobHandler {

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    IServiceEvaluateStatisticsManager serviceEvaluateStatisticsManager;

    @Autowired
    IUserInfoManager userInfoManager;

    @Autowired
    IServiceEvaluateManager serviceEvaluateManager;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logger.info("评价统计定时器开始");
            // 删除系统中已经被删除的客服账号的评价记录
            serviceEvaluateStatisticsManager.deleteNotExistServices();

            // 查出所有的担保客服
            Map<String, Object> params = new HashMap<String, Object>();
            List<UserInfoEO> serviceList = userInfoManager.findServicers(params, "ID", true);
            if (CollectionUtils.isEmpty(serviceList)) return SUCCESS;
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
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("autoStatistics异常：{}",e);
            return FAIL;
        }
    }
}
