package com.wzitech.gamegold.repository.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.repository.dao.ISellerRedisDao;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * 卖家redis dao
 * @author yemq
 */
@Repository
public class SellerRedisDaoImpl extends AbstractRedisDAO<SellerInfo> implements ISellerRedisDao {
    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    };

    /**
     * 卖家使用下线功能
     * @param account
     * @param uid
     */
    public void useOffline(String account, String uid) {
        // 使用了下线功能，记录1个小时
        valueOps.set(RedisKeyHelper.useOffline(account, uid), "true", 1, TimeUnit.HOURS);
    }

    /**
     * 判断卖家是否使用了离线功能,1个小时内使用了离线功能，将返回true
     * @param account
     * @param uid
     * @return
     */
    public boolean isUseOffline(String account, String uid) {
        // 1个小时内使用了离线功能，将返回true
        String v = valueOps.get(RedisKeyHelper.useOffline(account, uid));
        if (StringUtils.isNotBlank(v) && StringUtils.equals(v, "true"))
            return true;
        return false;
    }
}
