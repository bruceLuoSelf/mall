package com.wzitech.gamegold.facade.backend.action.message;

import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.order.business.IMessageRuleLogManager;
import com.wzitech.gamegold.order.entity.MessageRuleLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 短信规则操作管理
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class MessageRuleLogAction extends AbstractAction {

    @Autowired
    private IMessageRuleLogManager messageRuleLogManager;

    /**
     * 短信规则ID
     */
    private Long ruleId;

    private List<MessageRuleLog> ruleLogs;

    /**
     * 获取短信规则操作日志
     * @return
     */
    public String loadRuleLogs() {
        ruleLogs = messageRuleLogManager.selectByRuleId(ruleId);
        return returnSuccess();
    }

    public List<MessageRuleLog> getRuleLogs() {
        return ruleLogs;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
}
