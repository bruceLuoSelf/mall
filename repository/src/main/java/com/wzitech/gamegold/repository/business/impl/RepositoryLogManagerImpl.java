package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentLogContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.log.entity.LogInfo;
import com.wzitech.gamegold.common.log.entity.RepositoryLogInfo;
import com.wzitech.gamegold.repository.business.IRepositoryLogManager;
import com.wzitech.gamegold.repository.dao.IRepositoryLogDao;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * 库存日志管理实现类
 * @author yemq
 */
@Component
public class RepositoryLogManagerImpl extends AbstractBusinessObject implements IRepositoryLogManager {
    @Autowired
    private IRepositoryLogDao repositoryLogDao;

    @Transactional
    @Override
    public void add(RepositoryLogInfo logInfo, RepositoryInfo repositoryInfo) {
        IUser user = CurrentUserContext.getUser();
        if (user != null) {
            String userId = user.getUid();
            if (StringUtils.isBlank(userId)) {
                userId = String.valueOf(user.getId());
            }
            logInfo.setUserId(userId);
            logInfo.setUserAccount(user.getLoginAccount());
            logInfo.setUserType(UserType.getTypeByCode(user.getUserType()));
        }

        if (repositoryInfo != null) {
            logInfo.setRepositoryId(repositoryInfo.getId());
            logInfo.setSellerAccount(repositoryInfo.getLoginAccount());
            logInfo.setGameAccount(repositoryInfo.getGameAccount());
            logInfo.setSellerGameRole(repositoryInfo.getSellerGameRole());
            logInfo.setGameName(repositoryInfo.getGameName());
            logInfo.setRegion(repositoryInfo.getRegion());
            logInfo.setServer(repositoryInfo.getServer());
            logInfo.setGameRace(repositoryInfo.getGameRace());
            logInfo.setGoodsTypeName(repositoryInfo.getGoodsTypeName());
            if(null!=repositoryInfo.getGoodsTypeName()){
                logInfo.setGoodsTypeId(repositoryInfo.getGoodsTypeId());
            }
        }

        logInfo.setCreateTime(new Date());
        logInfo.setThreadId(CurrentLogContext.getThreadId());
        logInfo.setIp(CurrentIpContext.getIp());
        repositoryLogDao.insert(logInfo);
    }

    @Transactional(readOnly = true)
    @Override
    public GenericPage<RepositoryLogInfo> queryLog(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc) throws SystemException {
        return repositoryLogDao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }
}
