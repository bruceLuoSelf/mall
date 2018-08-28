package com.wzitech.gamegold.repository.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.common.log.entity.RepositoryLogInfo;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;

import java.util.Map;

/**
 * 库存日志管理接口
 * @author yemq
 */
public interface IRepositoryLogManager {
    void add(RepositoryLogInfo logInfo, RepositoryInfo repositoryInfo);

    GenericPage<RepositoryLogInfo> queryLog(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc)throws SystemException;
}
