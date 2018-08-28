package com.wzitech.gamegold.order.business;

import com.wzitech.gamegold.order.entity.ServiceSort;

import java.util.List;

/**
 * 客服分单排序
 * @author yemq
 */
public interface IServiceSortManager {

    /**
     * 初始化客服待发货订单数量和最后分配时间
     * @param serviceId
     */
    void initOrderCount(Long serviceId);

    /**
     * 根据待发货订单数由少到多，获取已排序的客服数据
     * @param userType 只能传寄售或担保
     */
    List<ServiceSort> getSortedServiceList(int userType, String gameName);

    /**
     * 获取下一个该分配的客服ID
     * @param userType 只能传寄售或担保
     * @return
     */
    Long getNextServiceId(int userType, String gameName);

    /**
     * 客服待发货订单数量+1
     * @param serviceId
     */
    void incOrderCount(Long serviceId);

    /**
     * 客服待发货订单数量-1
     * @param serviceId
     */
    void decOrderCount(Long serviceId);
}
