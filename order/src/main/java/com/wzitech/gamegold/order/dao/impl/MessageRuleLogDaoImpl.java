package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.order.dao.IMessageRuleDao;
import com.wzitech.gamegold.order.dao.IMessageRuleLogDao;
import com.wzitech.gamegold.order.entity.MessageRule;
import com.wzitech.gamegold.order.entity.MessageRuleLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 短信规则操作日志dao
 * @author yemq
 */
@Repository
public class MessageRuleLogDaoImpl extends AbstractMyBatisDAO<MessageRuleLog, Long>  implements IMessageRuleLogDao {
    /**
     * 根据短信规则ID删除短信操作日志
     *
     * @param ruleId
     */
    @Override
    public void deleteByRuleId(Long ruleId) {
        getSqlSession().delete(getMapperNamespace()+".deleteByRuleId", ruleId);
    }

    /**
     * 删除最后一条日志
     *
     * @param ruleId
     */
    @Override
    public void deleteLastLog(Long ruleId) {
        getSqlSession().delete(getMapperNamespace()+".deleteLastLog", ruleId);
    }

    /**
     * 根据短信规则ID获取短信操作日志
     *
     * @param ruleId
     * @return
     */
    @Override
    public List<MessageRuleLog> selectByRuleId(Long ruleId) {
        return getSqlSession().selectList(getMapperNamespace() + ".selectByRuleId", ruleId);
    }


}
