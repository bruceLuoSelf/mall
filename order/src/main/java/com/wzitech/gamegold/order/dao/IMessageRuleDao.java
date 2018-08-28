package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.MessageRule;

/**
 * 短信规则DAO
 * @author yemq
 */
public interface IMessageRuleDao extends IMyBatisBaseDAO<MessageRule, Long> {
    /**
     * 解挂
     * @param id
     */
    void enabled(Long id);

    /**
     * 挂起
     * @param id
     */
    void disabled(Long id);
}
