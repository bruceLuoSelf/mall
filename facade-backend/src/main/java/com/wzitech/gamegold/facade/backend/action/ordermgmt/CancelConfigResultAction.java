package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.order.business.IOrderInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * 对配单进行取消
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class CancelConfigResultAction extends AbstractAction {
    @Autowired
    IOrderInfoManager orderInfoManager;

    /**
     * 格式：主订单号_子订单号
     */
    private String id;

    /**
     * 取消配单
     * @return
     */
    public String cancelConfigResult() {
        try {
            orderInfoManager.cancelOrder(id, 1, null,null);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public void setId(String id) {
        this.id = id;
    }
}
