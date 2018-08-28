package com.wzitech.gamegold.order.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.SentMessage;

import java.util.Map;

/**
 * 已发送短信管理
 * @author yemq
 */
public interface ISentMessageManager {
    /**
     * 添加已发送短信
     * @param message
     */
    void add(SentMessage message);

    /**
     * 根据订单号，触发的订单状态查询数量
     * @param queryMap
     * @return
     */
    int countByMap(Map<String, Object> queryMap);

    GenericPage<SentMessage> queryList(Map<String, Object> queryParam, String orderBy, boolean isAsc,
                                       int pageSize, int start) throws SystemException;
}
