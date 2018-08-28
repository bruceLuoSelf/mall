package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.shorder.dao.IExistLogRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * Created by 340032 on 2018/5/10.
 */
@Repository
public class ExistLogRedisImpl extends AbstractRedisDAO<BaseEntity> implements IExistLogRedis {

    @Autowired
    @Qualifier("userRedisTemplate")
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }


    /**
     * 返回true 代表没写过日志
     * 返回false 代表写过日志
     * @param subOrderId
     * @return
     */
    @Override
    public Boolean isWrite(Long subOrderId) {
        Boolean result = valueOps.setIfAbsent(RedisKeyHelper.ExistLog(subOrderId), subOrderId.toString());
        if (result != null && result) {
            String key = RedisKeyHelper.ExistLog(subOrderId);
            template.expire(key,1L,TimeUnit.DAYS);
            return result;
        }
        return result;
    }
}
