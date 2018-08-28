package com.wzitech.gamegold.order.business;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.order.entity.MessageRule;

import java.util.List;
import java.util.Map;

/**
 * 短信规则管理
 *
 * @author yemq
 */
public interface IMessageRuleManager {

    /**
     * 添加短信规则
     *
     * @param rule
     * @return
     */
    MessageRule addRule(MessageRule rule);

    /**
     * 修改短信规则
     *
     * @param rule
     * @return
     */
    MessageRule updateRule(MessageRule rule);

    /**
     * 删除规则
     *
     * @param id
     * @return
     */
    void delete(Long id);

    /**
     * 根据ID获取短信规则
     *
     * @param id
     * @return
     */
    MessageRule getById(Long id);

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

    /**
     * 分页获取短信规则
     * @param queryParam
     * @param orderBy
     * @param isAsc
     * @param pageSize
     * @param start
     * @return
     * @throws SystemException
     */
    GenericPage<MessageRule> queryList(Map<String, Object> queryParam, String orderBy, boolean isAsc,
                                       int pageSize, int start) throws SystemException;

    /**
     * 获取所有的短信规则
     * @param queryParam
     * @param orderBy
     * @param isAsc
     * @return
     * @throws SystemException
     */
    List<MessageRule> queryList(Map<String, Object> queryParam, String orderBy, boolean isAsc) throws SystemException;
}
