package com.wzitech.gamegold.facade.backend.action.message;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.order.business.IMessageRuleManager;
import com.wzitech.gamegold.order.entity.MessageRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信规则管理
 *
 * @author yemq
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class MessageRuleAction extends AbstractAction {

    @Autowired
    private IMessageRuleManager messageRuleManager;

    /**
     * 游戏名称
     */
    private String gameName;

    private Long id;

    private MessageRule rule;

    /**
     * 消息规则列表
     */
    private List<MessageRule> messageRules;

    /**
     * 获取短信规则列表
     *
     * @return
     */
    public String list() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("gameName", gameName);
        GenericPage<MessageRule> genericPage = messageRuleManager.queryList(paramMap, "ID", false, this.limit, this.start);
        totalCount = genericPage.getTotalCount();
        messageRules = genericPage.getData();
        return returnSuccess();
    }

    /**
     * 添加短信规则
     *
     * @return
     */
    public String add() {
        try {
            messageRuleManager.addRule(rule);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改短信规则
     *
     * @return
     */
    public String update() {
        try {
            messageRuleManager.updateRule(rule);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除短信规则
     *
     * @return
     */
    public String delete() {
        try {
            messageRuleManager.delete(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 解挂
     * @return
     */
    public String enabled() {
        try {
            messageRuleManager.enabled(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 挂起
     * @return
     */
    public String disabled() {
        try {
            messageRuleManager.disabled(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public List<MessageRule> getMessageRules() {
        return messageRules;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageRule getRule() {
        return rule;
    }

    public void setRule(MessageRule rule) {
        this.rule = rule;
    }
}
