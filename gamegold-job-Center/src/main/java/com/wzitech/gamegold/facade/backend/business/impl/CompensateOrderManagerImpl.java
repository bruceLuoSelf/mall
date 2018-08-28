package com.wzitech.gamegold.facade.backend.business.impl;

import com.wzitech.gamegold.facade.backend.business.ICompensateOrderManager;
import com.wzitech.gamegold.order.business.IOrderPushMainManager;
import com.wzitech.gamegold.order.dao.IOrderInfoDBDAO;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import com.wzitech.gamegold.shorder.business.IPushToMqUtil;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.dao.IOrderPushRedisDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 340096
 * @date 2017/12/4.
 */
@Component
public class CompensateOrderManagerImpl implements ICompensateOrderManager {
    @Autowired
    private IOrderPushRedisDao orderPushRedisDao;

    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    private IPushToMqUtil pushToMqUtilImpl;

    @Autowired
    private IOrderInfoDBDAO orderInfoDBDAO;

    @Autowired
    private IOrderPushMainManager orderPushMainImpl;

    @Override
    public void compensateOrderToOrderCenter() {
        List order = orderPushRedisDao.findOrder();
        if (order!=null) {
            int size = order.size();
            for (int i = 0; i < order.size(); i++) {
                String orderString = (String) order.get(i);
                JSONObject json = JSONObject.fromObject(orderString);
                String id = (String) json.get("Id");
                if (id.contains("YX")) {
                    OrderInfoEO orderInfoEO = orderInfoDBDAO.queryOrderId(id);
                    orderPushMainImpl.orderPushMain(orderInfoEO);
                }else {
                    DeliveryOrder deliveryOrder = deliveryOrderDao.getByOrderId(id);
                    pushToMqUtilImpl.pushToMainMq(deliveryOrder, 1);
                }
            }
            orderPushRedisDao.removeOrder(size);
        }

    }
}
