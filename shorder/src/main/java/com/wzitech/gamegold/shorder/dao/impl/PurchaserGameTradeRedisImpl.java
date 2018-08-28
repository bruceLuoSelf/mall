package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.shorder.dao.IPurchaserGameTradeRedis;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by ljn on 2018/4/18.
 */
@Component
public class PurchaserGameTradeRedisImpl extends AbstractRedisDAO<PurchaserGameTrade> implements IPurchaserGameTradeRedis{

    @Autowired
    @Qualifier("userRedisTemplate")
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Override
    public void add(Long id, List<PurchaserGameTrade> list) {
        String key = RedisKeyHelper.getPurchaserGameTrade(id);
        String json = jsonMapper.toJson(list);
        valueOps.set(key,json);
    }

    @Override
    public void delete(Long id) {
        String key = RedisKeyHelper.getPurchaserGameTrade(id);
        template.delete(key);
    }

    @Override
    public List<PurchaserGameTrade> getPurchaserGameTrade(Long id) {
        String key = RedisKeyHelper.getPurchaserGameTrade(id);
        String json = valueOps.get(key);
        List<PurchaserGameTrade> list = jsonMapper.fromJson(json, jsonMapper.createCollectionType(List.class, PurchaserGameTrade.class));
        return list;
    }

    @Override
    public void deleteAll() {
        String key = RedisKeyHelper.purchaserGameTradeForDelete();
        Set<String> keys = template.keys(key);
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String k = it.next();
            template.delete(k);
        }
    }
}
