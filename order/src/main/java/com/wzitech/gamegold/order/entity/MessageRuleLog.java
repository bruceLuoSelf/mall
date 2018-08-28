package com.wzitech.gamegold.order.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.gamegold.common.enums.MessageOperateType;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

import java.util.Date;

/**
 * 短信规则修改日志
 *
 * @author yemq
 */
public class MessageRuleLog extends BaseEntity {
    /**
     * 所属短信规则ID
     */
    private Long messageRuleId;
    /**
     * 操作类型
     */
    private MessageOperateType type;
    /**
     * 操作员
     */
    private UserInfoEO operator;
    /**
     * 操作时间
     */
    private Date createTime;

    public MessageRuleLog() {
    }

    public Long getMessageRuleId() {
        return messageRuleId;
    }

    public void setMessageRuleId(Long messageRuleId) {
        this.messageRuleId = messageRuleId;
    }

    public MessageOperateType getType() {
        return type;
    }

    public void setType(MessageOperateType type) {
        this.type = type;
    }

    public UserInfoEO getOperator() {
        return operator;
    }

    public void setOperator(UserInfoEO operator) {
        this.operator = operator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
