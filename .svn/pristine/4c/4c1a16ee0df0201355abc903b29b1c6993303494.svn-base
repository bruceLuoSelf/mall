package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 从寄售物服机器人退回
 * @author yemq
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class SendBackFromRobotAction extends AbstractAction {
    private String orderId;
    private Long subOrderId;
    private String reason;

    @Autowired
    IOrderInfoManager orderInfoManager;

    public SendBackFromRobotAction() {}

    /**
     * 从寄售全自动机器人退回，重新安排寄售物服
     * @return
     */
    public String sendbackFromRobot() {
        try {
            if (StringUtils.isBlank(reason))
                reason = "需人工处理";

            orderInfoManager.sendbackFromJsRobot(orderId, subOrderId, reason);
            return this.returnSuccess(message);
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setSubOrderId(Long subOrderId) {
        this.subOrderId = subOrderId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
