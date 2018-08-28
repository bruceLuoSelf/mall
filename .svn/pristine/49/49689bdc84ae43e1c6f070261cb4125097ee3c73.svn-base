package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.order.dao.IServiceEvaluateStatisticsDao;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;
import org.springframework.stereotype.Repository;

/**
 * 担保客服评价统计数据DAO
 * @author yemq
 */
@Repository
public class ServiceEvaluateStatisticsDaoImpl extends AbstractMyBatisDAO<ServiceEvaluateStatistics, Long> implements IServiceEvaluateStatisticsDao {
    /**
     * 删除系统中已经被删除的客服账号的评价记录
     */
    @Override
    public void deleteNotExistServices() {
        this.getSqlSession().delete(this.getMapperNamespace() + ".deleteNotExistServices");
    }
}
