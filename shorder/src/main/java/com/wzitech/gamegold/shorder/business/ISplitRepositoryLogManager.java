package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryLog;

import java.util.List;
import java.util.Map;

/**
 * 分仓日志
 */
public interface ISplitRepositoryLogManager {
    /**
     * 分页查找分仓日志
     *
     * @param map
     * @param orderBy
     * @param start
     * @param pageSize
     * @return
     */
    GenericPage<SplitRepositoryLog> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc);

    /**
     * 保存分仓日志
     *
     * @param log
     */
    void saveLog(SplitRepositoryLog log);

    void saveLogs(List<SplitRepositoryLog> splitRepositoryLogs);

    /**
     * 保存收货日志
     *
     * @param deliverySubOrder
     * @param logContent
     */
    void saveLog(DeliverySubOrder deliverySubOrder, String logContent);

}
