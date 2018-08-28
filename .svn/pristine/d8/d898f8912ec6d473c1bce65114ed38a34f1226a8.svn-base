package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.ServiceSort;

import java.util.List;

/**
 * 客服分单排序
 * @author yemq
 */
public interface IServiceSortDao extends IMyBatisBaseDAO<ServiceSort,Long> {

    /**
     * 根据客服ID查询客服分单排序数据
     * @param serviceId
     * @param lockMode
     * @return
     */
    ServiceSort selectByServiceId(Long serviceId, boolean lockMode);

    /**
     * 根据待发货订单数由少到多，获取已排序的客服数据
     * @param userType
     * @param gameName
     * @return
     */
    List<ServiceSort> selectSortedServiceList(int userType, String gameName);

    /**
     * 获取下一个该分配的客服ID
     * @param userType
     * @param gameName
     * @return
     */
    Long selectNextServiceId(int userType, String gameName);

    /**
     * 客服待发货订单数量+1
     *
     * @param serviceId
     */
     void incOrderCount(Long serviceId);

    /**
     * 客服待发货订单数量-1
     *
     * @param serviceId
     */
     void decOrderCount(Long serviceId);
}
