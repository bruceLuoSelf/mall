package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.ServiceEvaluate;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 客服评价记录dao
 * @author yemq
 */
public interface IServiceEvaluateDao  extends IMyBatisBaseDAO<ServiceEvaluate,Long> {

    /**
     * 根据订单号查询所有的评价记录
     * @param orderId
     * @return
     */
    List<ServiceEvaluate> queryByOrderId(String orderId);

    /**
     * 根据订单号删除评价记录
     * @param orderId
     */
    void removeByOrderId(String orderId);

    /**
     * 分页统计评价数据
     * @param params
     * @param pageSize
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<ServiceEvaluateStatistics> statistics(Map<String, Object> params, int pageSize, int startIndex, String orderBy, boolean isAsc);
}
