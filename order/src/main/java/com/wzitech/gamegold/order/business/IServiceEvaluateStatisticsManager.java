package com.wzitech.gamegold.order.business;

import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;

import java.util.List;
import java.util.Map;

/**
 * 担保客服评价统计数据管理
 * @author yemq
 */
public interface IServiceEvaluateStatisticsManager {
    /**
     * 更新客服评价统计
     * @param statistics
     */
    void update(ServiceEvaluateStatistics statistics);

    /**
     * 删除系统中已经被删除的客服账号的评价记录
     */
    void deleteNotExistServices();

    /**
     * 根据条件获取担保客服评价统计记录
     * @param queryParam
     * @param orderBy
     * @param isAsc
     * @return
     */
    List<ServiceEvaluateStatistics> loadRecords(Map<String, Object> queryParam, String orderBy, boolean isAsc);
}
