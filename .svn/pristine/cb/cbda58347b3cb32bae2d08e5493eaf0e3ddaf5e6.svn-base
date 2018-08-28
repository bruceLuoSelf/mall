package com.wzitech.gamegold.order.business.impl;

import com.wzitech.gamegold.order.business.IServiceEvaluateStatisticsManager;
import com.wzitech.gamegold.order.dao.IServiceEvaluateStatisticsDao;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 担保客服评价统计数据管理
 * @author yemq
 */
@Component
public class ServiceEvaluateStatisticsManagerImpl implements IServiceEvaluateStatisticsManager {

    @Autowired
    IServiceEvaluateStatisticsDao serviceEvaluateStatisticsDao;

    /**
     * 更新客服评价统计
     *
     * @param statistics
     */
    @Override
    @Transactional
    public void update(ServiceEvaluateStatistics statistics) {
        // 先查询有没有客服评价记录统计
        ServiceEvaluateStatistics o = serviceEvaluateStatisticsDao.selectById(statistics.getId());
        if (o != null) {
            serviceEvaluateStatisticsDao.update(statistics);
        } else {
            serviceEvaluateStatisticsDao.insert(statistics);
        }
    }

    /**
     * 删除系统中已经被删除的客服账号的评价记录
     */
    @Override
    @Transactional
    public void deleteNotExistServices() {
        serviceEvaluateStatisticsDao.deleteNotExistServices();
    }

    /**
     * 根据条件获取担保客服评价统计记录
     * @param queryParam
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public List<ServiceEvaluateStatistics> loadRecords(Map<String, Object> queryParam, String orderBy, boolean isAsc) {
        return serviceEvaluateStatisticsDao.selectByMap(queryParam, orderBy, isAsc);
    }
}
