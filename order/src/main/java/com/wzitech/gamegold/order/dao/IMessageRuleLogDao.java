package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.MessageRuleLog;

import java.util.List;

/**
 * 短信规则操作日志
 *
 * @author yemq
 */
public interface IMessageRuleLogDao extends IMyBatisBaseDAO<MessageRuleLog, Long> {
    /**
     * 根据短信规则ID删除短信操作日志
     * @param ruleId
     */
    void deleteByRuleId(Long ruleId);

    /**
     * 删除最后一条日志
     * @param ruleId
     */
    void deleteLastLog(Long ruleId);

    /**
     * 根据短信规则ID获取短信操作日志
     * @param ruleId
     * @return
     */
    List<MessageRuleLog> selectByRuleId(Long ruleId);
}
