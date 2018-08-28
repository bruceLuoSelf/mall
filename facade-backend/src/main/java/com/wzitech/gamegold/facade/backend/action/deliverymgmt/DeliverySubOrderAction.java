package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IDeliverySubOrderManager;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出货子订单
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class DeliverySubOrderAction  extends AbstractAction {
    private DeliverySubOrder deliverySubOrder;

    private List<DeliverySubOrder> deliverySubOrderList;

    @Autowired
    IDeliverySubOrderManager deliverySubOrderManager;

    /**
     * 查询出货子订单信息列表
     * @return
     */
    public String queryDeliverySubOrder(){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if(deliverySubOrder!=null) {
            if(deliverySubOrder.getChId()!=null){
                queryMap.put("chId", deliverySubOrder.getChId());
            }
        }
        deliverySubOrderList=deliverySubOrderManager.querySubOrders(queryMap);
        return this.returnSuccess();
    }

    public DeliverySubOrder getDeliverySubOrder() {
        return deliverySubOrder;
    }

    public void setDeliverySubOrder(DeliverySubOrder deliverySubOrder) {
        this.deliverySubOrder = deliverySubOrder;
    }

    public List<DeliverySubOrder> getDeliverySubOrderList() {
        return deliverySubOrderList;
    }

    public void setDeliverySubOrderList(List<DeliverySubOrder> deliverySubOrderList) {
        this.deliverySubOrderList = deliverySubOrderList;
    }
}
