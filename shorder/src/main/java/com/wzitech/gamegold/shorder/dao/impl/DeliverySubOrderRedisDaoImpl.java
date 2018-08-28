package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderDao;
import com.wzitech.gamegold.shorder.dao.IDeliverySubOrderRedisDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Created by 339928 on 2018/1/3.
 */
@Repository
public class DeliverySubOrderRedisDaoImpl extends AbstractRedisDAO<DeliverySubOrder> implements IDeliverySubOrderRedisDao{

    private static final String ORDER_KEY="gamegold:shsubOrder:orderstatus";

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }


    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Override
    public void save(DeliverySubOrder deliverySubOrder) {
        BoundHashOperations<String, String, String> bhOps = template.boundHashOps(ORDER_KEY +":"+deliverySubOrder.getOrderId());
        String json = jsonMapper.toJson(deliverySubOrder);
        bhOps.put(deliverySubOrder.getId().toString(), json);
        template.expire(ORDER_KEY +":"+deliverySubOrder.getId(),24, TimeUnit.HOURS);
    }

    @Override
    public DeliverySubOrder get(String orderId) {
        BoundHashOperations<String, Long, String> userOps = template.boundHashOps(ORDER_KEY +":"+orderId);
        String json = userOps.get(orderId);
        return jsonMapper.fromJson(json, DeliverySubOrder.class);
    }
}
