package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.goods.dao.IReferencePriceRedisDao;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by jhlcitadmin on 2017/3/17.
 */
@Component
public class ReferencePriceRedisDaoImpl extends AbstractRedisDAO<ReferencePrice> implements IReferencePriceRedisDao {

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }
    private JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    /**
     * 保存更新配置
     * @param
     */
    @Override
    public void saveUpdate(ReferencePrice price) {
        String json = jsonMapper.toJson(price);
        BoundHashOperations<String, String, String> hashOps = template.boundHashOps(RedisKeyHelper.serviceReferencePrice(price.getGameName(),price.getRegionName(),price.getServerName(),price.getRaceName(),price.getGoodsTypeName()));
        hashOps.put("json", json);
        template.expire(RedisKeyHelper.serviceReferencePrice(price.getGameName(),price.getRegionName(),price.getServerName(),price.getRaceName(),price.getGoodsTypeName()),60, TimeUnit.SECONDS);
    }

    /**
     * 获取配置信息
     * @param
     */
    @Override
    public ReferencePrice get(ReferencePrice price) {
        BoundHashOperations<String, String, String> ops = template.boundHashOps(RedisKeyHelper.serviceReferencePrice(price.getGameName(),price.getRegionName(),price.getServerName(),price.getRaceName(),price.getGoodsTypeName()));
        String json = ops.get("json");
        ReferencePrice config = jsonMapper.fromJson(json, ReferencePrice.class);
        return config;
    }

}
