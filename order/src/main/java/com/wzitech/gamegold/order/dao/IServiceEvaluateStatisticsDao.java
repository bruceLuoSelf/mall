package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;

/**
 * 担保客服评价统计数据DAO
 * @author yemq
 */
public interface IServiceEvaluateStatisticsDao extends IMyBatisBaseDAO<ServiceEvaluateStatistics,Long> {
    /**
     * 删除系统中已经被删除的客服账号的评价记录
     */
    void deleteNotExistServices();
}
