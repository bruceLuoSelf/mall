package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.order.dao.IMessageRuleDao;
import com.wzitech.gamegold.order.entity.MessageRule;
import org.springframework.stereotype.Repository;

/**
 * 短信规则设置dao
 * @author yemq
 */
@Repository
public class MessageRuleDaoImpl extends AbstractMyBatisDAO<MessageRule, Long>  implements IMessageRuleDao {
    /**
     * 解挂
     *
     * @param id
     */
    @Override
    public void enabled(Long id) {
        getSqlSession().update(getMapperNamespace()+".enabled", id);
    }

    /**
     * 挂起
     *
     * @param id
     */
    @Override
    public void disabled(Long id) {
        getSqlSession().update(getMapperNamespace()+".disabled", id);
    }
}
