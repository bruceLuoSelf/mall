package com.wzitech.gamegold.order.business;

import com.wzitech.gamegold.order.entity.MessageRuleLog;

import java.util.List;

/**
 * 短信规则操作日志管理
 * @author yemq
 */
public interface IMessageRuleLogManager {
    /**
     * 根据短信规则ID获取操作日志
     * @param ruleId 短信
     * @return
     */
    List<MessageRuleLog> selectByRuleId(Long ruleId);

    /**
     * 根据短信规则ID删除所有日志记录
     * @param ruleId
     */
    void deleteByRuleId(Long ruleId);

    /**
     * 添加日志，最多10条，超出后删除最后一条
     * @param log
     */
    void insert(MessageRuleLog log);
}
