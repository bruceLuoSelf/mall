package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderStartRedisDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**ZW_C_JB_00004 sunyang
 * Created by sunynag on 2017/5/11.
 */
@Repository
public class DeliveryOrderStartRedisDaoImpl extends AbstractRedisDAO<DeliveryOrder> implements IDeliveryOrderStartRedisDao {


    private static String ORDER_KEY = "gamegold:shorder:orderstruts";

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }


    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Override
    public void save(DeliveryOrder deliveryOrder) {
        BoundHashOperations<String, String, String> bhOps = template.boundHashOps(ORDER_KEY +":"+deliveryOrder.getOrderId());
        String json = jsonMapper.toJson(deliveryOrder);
        bhOps.put(deliveryOrder.getOrderId(), json);
        template.expire(ORDER_KEY +":"+deliveryOrder.getOrderId(),24, TimeUnit.HOURS);
    }

    @Override
    public DeliveryOrder get(String orderid) {
        BoundHashOperations<String, String, String> userOps = template.boundHashOps(ORDER_KEY +":"+orderid);
        String json = userOps.get(orderid);
        return jsonMapper.fromJson(json, DeliveryOrder.class);
    }


}
