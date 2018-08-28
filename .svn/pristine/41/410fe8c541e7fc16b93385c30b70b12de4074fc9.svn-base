package com.wzitech.gamegold.facade.backend.action.message;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.order.business.ISentMessageManager;
import com.wzitech.gamegold.order.entity.SentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 已发送短信管理
 *
 * @author yemq
 */
@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@ExceptionToJSON
public class SentMessageAction extends AbstractAction {

    @Autowired
    private ISentMessageManager sentMessageManager;

    private String orderId;
    private String gameName;
    private Date createStartTime;
    private Date createEndTime;

    private List<SentMessage> list;

    /**
     * 已发送短信列表
     *
     * @return
     */
    public String list() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderId", orderId);
        queryMap.put("gameName", gameName);
        queryMap.put("createStartTime", createStartTime);
        queryMap.put("createEndTime", createEndTime);

        GenericPage<SentMessage> pages = sentMessageManager.queryList(queryMap, "CREATE_TIME", false, this.limit, this.start);
        totalCount = pages.getTotalCount();
        list = pages.getData();
        return returnSuccess();
    }

    public List<SentMessage> getList() {
        return list;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setCreateStartTime(Date createStartTime) {
        this.createStartTime = createStartTime;
    }

    public void setCreateEndTime(Date createEndTime) {
        this.createEndTime = createEndTime;
    }
}
