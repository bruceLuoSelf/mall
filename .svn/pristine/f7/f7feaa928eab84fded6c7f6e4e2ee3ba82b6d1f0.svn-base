package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IHuanXinRedisShorderDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.utils.HuanXinRedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

/**
 * Created by yin on 16/12/15.
 */
@Repository
public class HuanXinRedisDaoShorderImpl extends AbstractRedisDAO<DeliveryOrder>
        implements IHuanXinRedisShorderDao {

    @Autowired
    @Qualifier("userRedisTemplate")
    public void setTemplate(StringRedisTemplate template) {
        this.template = template;
    }
//
//
//    protected StringRedisTemplate template;
//
//    protected ValueOperations<String, String> valueOps;
//
//    public HuanXinRedisDaoShorderImpl(){
//        valueOps = this.template.opsForValue();
//    }

    @Override
    public void setHuanXinToken(String token) {
        valueOps.set(HuanXinRedisKeyUtils.huanXinTokenKey(), token);
    }

    @Override
    public void setHXTokenTimeout(Long expiresIn) {
        valueOps.set(HuanXinRedisKeyUtils.huanXinTimeoutLeft(), expiresIn.toString());
    }

    @Override
    public String getHuanXinToken() {
        return valueOps.get(HuanXinRedisKeyUtils.huanXinTokenKey());
    }

    @Override
    public Long getHuaxnXinTokenTimeout() {
        String strExpiresIn = valueOps.get(HuanXinRedisKeyUtils.huanXinTimeoutLeft());
        Long tokenExpiresIn = null;
        if (strExpiresIn != null) {
            tokenExpiresIn = Long.valueOf(strExpiresIn);
        }
        return tokenExpiresIn;
    }
}
