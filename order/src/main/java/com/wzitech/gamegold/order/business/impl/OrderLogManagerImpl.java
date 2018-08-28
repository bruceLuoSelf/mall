package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentLogContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.UserType;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.order.business.IOrderLogManager;
import com.wzitech.gamegold.order.dao.IOrderLogDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * 订单日志管理
 * @author yemq
 */
@Component
public class OrderLogManagerImpl extends AbstractBusinessObject implements IOrderLogManager {
    @Autowired
    private IOrderLogDao orderLogDao;

    /**
     * 添加日志
     *
     * @param log
     * @return
     * @throws com.wzitech.chaos.framework.server.common.exception.SystemException
     */
    @Override
    @Transactional
    public OrderLogInfo add(OrderLogInfo log) throws SystemException {
        IUser user = CurrentUserContext.getUser();
        if (user != null) {
            String userId = user.getUid();
            if (StringUtils.isBlank(userId)) {
                userId = String.valueOf(user.getId());
            }
            log.setUserId(userId);
            log.setUserAccount(user.getLoginAccount());
            log.setUserType(UserType.getTypeByCode(user.getUserType()));
        }

        log.setCreateTime(new Date());
        log.setThreadId(CurrentLogContext.getThreadId());
        log.setIp(CurrentIpContext.getIp());
        Long id = (Long) orderLogDao.insert(log);
        log.setId(id);
        return log;
    }

    /**
     * 根据条件分页查询
     *
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     * @throws com.wzitech.chaos.framework.server.common.exception.SystemException
     */
    @Override
    @Transactional(readOnly = true)
    public GenericPage<OrderLogInfo> queryLog(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc) throws SystemException {
        return orderLogDao.selectByMap(queryMap, limit, start, sortBy, isAsc);
    }
}
