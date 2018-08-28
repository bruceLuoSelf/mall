package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentLogContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.log.entity.GoodsLogInfo;
import com.wzitech.gamegold.goods.business.IGoodsLogManager;
import com.wzitech.gamegold.goods.dao.IGoodsLogDao;
import com.wzitech.gamegold.goods.entity.GoodsInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * 商品日志管理实现
 * @author yemq
 */
@Component
public class GoodsLogManagerImpl extends AbstractBusinessObject implements IGoodsLogManager {
    @Autowired
    private IGoodsLogDao goodsLogDao;

    @Transactional
    @Override
    public void add(GoodsLogInfo logInfo, GoodsInfo goods) {
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

        if (goods != null) {
            logInfo.setTitle(goods.getTitle());
            logInfo.setGameName(goods.getGameName());
            logInfo.setRegion(goods.getRegion());
            logInfo.setServer(goods.getServer());
            logInfo.setGameRace(goods.getGameRace());
        }

        logInfo.setCreateTime(new Date());
        logInfo.setThreadId(CurrentLogContext.getThreadId());
        logInfo.setIp(CurrentIpContext.getIp());
        goodsLogDao.insert(logInfo);
    }

    @Transactional(readOnly = true)
    @Override
    public GenericPage<GoodsLogInfo> queryLog(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc) throws SystemException {
        return goodsLogDao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }
}
