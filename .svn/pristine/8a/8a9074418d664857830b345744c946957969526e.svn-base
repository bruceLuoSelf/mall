package com.wzitech.gamegold.order.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;

import java.util.Map;

/**
 * 订单日志管理
 * @author yemq
 */
public interface IOrderLogManager {
    /**
     * 添加日志
     * @param orderLogInfo
     * @return
     * @throws SystemException
     */
    OrderLogInfo add(OrderLogInfo orderLogInfo) throws SystemException;

    /**
     * 根据条件分页查询
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     * @throws SystemException
     */
    GenericPage<OrderLogInfo> queryLog(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc)throws SystemException;
}
