package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.OrderLog;

import java.util.List;
import java.util.Map;

/**
 * 出货单日志管理
 *
 * @yemq
 */
public interface IDeliveryOrderLogManager {
    /**
     * 写日志
     *
     * @param orderLog
     */
    void writeLog(OrderLog orderLog);

    /**
     * 根据出货单ID查询所有的日志
     *
     * @param orderId
     * @return
     */
    List<OrderLog> getByOrderId(String orderId);

    /**
     * 根据出货单ID查询所有的日志
     * @param orderId
     * @param type
     * @return
     */
    List<OrderLog> getByOrderId(String orderId, int type);

    /**
     * 根据日志ID查询
     *
     * @param id
     * @return
     */
    OrderLog getById(Long id);

    /**
     * 根据创建时间和出货订单号查询所有日志
     *
     * @param map
     * @return
     */
    GenericPage<OrderLog> queryByMap(Map<String, Object> map, int pageSize, int startIndex, String orderBy,
                                     Boolean isAsc);

    /**
     * 根据map查询所有日志
     * @param map
     * @return
     */
    List<OrderLog> queryAllByMap(Map<String,Object> map);

    /**
     * 是否存在邮寄自动化返回的日志信息
     * @param subId
     * @return
     */
     boolean isHaveLog(Long subId);
}
