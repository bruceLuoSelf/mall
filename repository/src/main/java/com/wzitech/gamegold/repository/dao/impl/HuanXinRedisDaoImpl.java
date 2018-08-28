package com.wzitech.gamegold.repository.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.repository.dao.IHuanXinRedisDao;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.repository.util.HuanXinRedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by yin on 16/12/15.
 */
@Repository
public class HuanXinRedisDaoImpl extends AbstractRedisDAO<SellerInfo>
        implements IHuanXinRedisDao {

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

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
