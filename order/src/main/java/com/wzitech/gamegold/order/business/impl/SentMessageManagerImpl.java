package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.business.ISentMessageManager;
import com.wzitech.gamegold.order.dao.ISentMessageDao;
import com.wzitech.gamegold.order.entity.SentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 已发送短信管理
 * @author yemq
 */
@Component
@Transactional
public class SentMessageManagerImpl implements ISentMessageManager {

    @Autowired
    private ISentMessageDao sentMessageDao;

    /**
     * 添加已发送短信
     *
     * @param message
     */
    @Override
    public void add(SentMessage message) {
        message.setCreateTime(new Date());
        sentMessageDao.insert(message);
    }

    /**
     * 根据订单号，触发的订单状态查询数量
     *
     * @param queryMap
     * @return
     */
    @Override
    public int countByMap(Map<String, Object> queryMap) {
        return sentMessageDao.countByMap(queryMap);
    }

    @Transactional(readOnly = true)
    @Override
    public GenericPage<SentMessage> queryList(Map<String, Object> queryParam, String orderBy, boolean isAsc, int pageSize, int start) throws SystemException {
        return sentMessageDao.selectByMap(queryParam,pageSize,start,orderBy,isAsc);
    }

}
