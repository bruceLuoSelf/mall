package com.wzitech.gamegold.order.business.impl;

import com.wzitech.gamegold.order.business.IMessageRuleLogManager;
import com.wzitech.gamegold.order.dao.IMessageRuleLogDao;
import com.wzitech.gamegold.order.entity.MessageRuleLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信规则操作日志管理
 * @author yemq
 */
@Component
public class MessageRuleLogManager implements IMessageRuleLogManager {

    @Autowired
    private IMessageRuleLogDao messageRuleLogDao;

    /**
     * 根据短信规则ID获取操作日志
     *
     * @param ruleId 短信
     * @return
     */
    @Override
    public List<MessageRuleLog> selectByRuleId(Long ruleId) {
        return messageRuleLogDao.selectByRuleId(ruleId);
    }

    /**
     * 根据短信规则ID删除所有日志记录
     *
     * @param ruleId
     */
    @Transactional
    @Override
    public void deleteByRuleId(Long ruleId) {
        messageRuleLogDao.deleteByRuleId(ruleId);
    }

    /**
     * 添加日志，最多10条，超出后删除最后一条
     * @param log
     */
    @Transactional
    public synchronized void insert(MessageRuleLog log) {
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("messageRuleId", log.getMessageRuleId());
        int count = messageRuleLogDao.countByMap(queryParam);
        if (count >= 10) {
            // 最多10条记录，超出后删除最后一条日志记录
            messageRuleLogDao.deleteLastLog(log.getMessageRuleId());
        }
        messageRuleLogDao.insert(log);
    }
}
