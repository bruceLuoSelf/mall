package com.wzitech.gamegold.facade.backend.action.log;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.LogType;
import com.wzitech.gamegold.order.business.IOrderLogManager;
import com.wzitech.gamegold.common.log.entity.OrderLogInfo;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.facade.backend.util.WebServerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单日志查询
 * @author yemq
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class OrderLogAction extends AbstractAction {
    @Autowired
    private IOrderLogManager orderLogManager;

    private LogType logType;
    private String userAccount;
    private Date startTime;
    private Date endTime;
    private String orderId;

    private List<OrderLogInfo> logs;

    /**
     * 查询订单日志
     * @return
     */
    public String queryOrderLogs() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("logType", logType);
        queryMap.put("userAccount", userAccount);
        queryMap.put("startTime", startTime);
        queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
        queryMap.put("orderId", orderId);
        GenericPage<OrderLogInfo> genericPage = orderLogManager.queryLog(queryMap, limit, start, "ID", false);
        logs = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    public List<OrderLogInfo> getLogs() {
        return logs;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
