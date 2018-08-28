package com.wzitech.gamegold.order.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.MessageOperateType;
import com.wzitech.gamegold.order.business.IMessageRuleLogManager;
import com.wzitech.gamegold.order.business.IMessageRuleManager;
import com.wzitech.gamegold.order.dao.IMessageRuleDao;
import com.wzitech.gamegold.order.dao.IMessageRuleLogDao;
import com.wzitech.gamegold.order.entity.MessageRule;
import com.wzitech.gamegold.order.entity.MessageRuleLog;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 短信规则管理
 * @author yemq
 */
@Component
@Transactional
public class MessageRuleManagerImpl implements IMessageRuleManager {

    @Autowired
    private IMessageRuleDao messageRuleDao;
    @Autowired
    private IMessageRuleLogManager messageRuleLogManager;

    /**
     * 添加短信规则
     *
     * @param rule
     * @return
     */
    @Override
    public MessageRule addRule(MessageRule rule) {
        Date now = new Date();
        rule.setLastUpdateTime(now);
        rule.setOperator((UserInfoEO)CurrentUserContext.getUser());
        rule.setEnabled(false);
        messageRuleDao.insert(rule);

        // 添加短信规则操作日志
        MessageRuleLog log = new MessageRuleLog();
        log.setMessageRuleId(rule.getId());
        log.setType(MessageOperateType.ADD);
        log.setOperator((UserInfoEO)CurrentUserContext.getUser());
        log.setCreateTime(now);
        messageRuleLogManager.insert(log);

        return rule;
    }

    /**
     * 修改短信规则
     *
     * @param rule
     * @return
     */
    @Override
    public MessageRule updateRule(MessageRule rule) {
        rule.setLastUpdateTime(new Date());
        rule.setOperator((UserInfoEO)CurrentUserContext.getUser());
        messageRuleDao.update(rule);

        // 添加短信规则操作日志
        MessageRuleLog log = new MessageRuleLog();
        log.setMessageRuleId(rule.getId());
        log.setType(MessageOperateType.MODIFY);
        log.setOperator((UserInfoEO)CurrentUserContext.getUser());
        log.setCreateTime(new Date());
        messageRuleLogManager.insert(log);
        return rule;
    }

    /**
     * 删除规则
     *
     * @param id
     * @return
     */
    @Override
    public void delete(Long id) {
        messageRuleDao.deleteById(id);
        messageRuleLogManager.deleteByRuleId(id);
    }

    /**
     * 根据ID获取短信规则
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public MessageRule getById(Long id) {
        return messageRuleDao.selectById(id);
    }

    /**
     * 解挂
     *
     * @param id
     */
    @Override
    public void enabled(Long id) {
        messageRuleDao.enabled(id);

        // 添加短信规则操作日志
        MessageRuleLog log = new MessageRuleLog();
        log.setMessageRuleId(id);
        log.setType(MessageOperateType.ENABLED);
        log.setOperator((UserInfoEO)CurrentUserContext.getUser());
        log.setCreateTime(new Date());
        messageRuleLogManager.insert(log);
    }

    /**
     * 挂起
     *
     * @param id
     */
    @Override
    public void disabled(Long id) {
        messageRuleDao.disabled(id);

        // 添加短信规则操作日志
        MessageRuleLog log = new MessageRuleLog();
        log.setMessageRuleId(id);
        log.setType(MessageOperateType.DISABLED);
        log.setOperator((UserInfoEO)CurrentUserContext.getUser());
        log.setCreateTime(new Date());
        messageRuleLogManager.insert(log);
    }

    /**
     * 分页获取短信规则
     *
     * @param queryParam
     * @param orderBy
     * @param isAsc
     * @param pageSize
     * @param start
     * @return
     * @throws com.wzitech.chaos.framework.server.common.exception.SystemException
     */
    @Override
    @Transactional(readOnly = true)
    public GenericPage<MessageRule> queryList(Map<String, Object> queryParam, String orderBy, boolean isAsc,
                                              int pageSize, int start) throws SystemException {
        return messageRuleDao.selectByMap(queryParam, pageSize, start, orderBy, isAsc);
    }

    /**
     * 获取所有的短信规则
     *
     * @param queryParam
     * @param orderBy
     * @param isAsc
     * @return
     * @throws com.wzitech.chaos.framework.server.common.exception.SystemException
     */
    @Override
    public List<MessageRule> queryList(Map<String, Object> queryParam, String orderBy, boolean isAsc) throws SystemException {
        return messageRuleDao.selectByMap(queryParam, orderBy, isAsc);
    }
}
