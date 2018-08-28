package com.wzitech.gamegold.usermgmt.dao.redis.impl;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.usermgmt.dao.redis.IServicerVoteConfigRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.ServicerVoteConfigEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * 客服投票配置信息redis管理
 *
 * @author yemq
 */
@Repository
public class ServicerVoteRedisDAOImpl extends AbstractRedisDAO<ServicerVoteConfigEO> implements IServicerVoteConfigRedisDAO {

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    /**
     * 获取客服投票配置信息
     *
     * @return ServicerVoteConfigEO
     */
    @Override
    public ServicerVoteConfigEO loadConfig() {
        BoundHashOperations<String, String, String> voteOps = this.template.boundHashOps(RedisKeyHelper.servicerVoteConfig());
        // 从redis中获取客服投票配置信息
        return voteOps.entries().isEmpty() != true ? mapper.fromHash(voteOps.entries()) : null;
    }

    /**
     * 保存客服投票配置信息，有效期为1天
     * @param config
     */
    @Override
    public void saveConfig(ServicerVoteConfigEO config) {
        BoundHashOperations<String, String, String> voteOps = this.template.boundHashOps(RedisKeyHelper.servicerVoteConfig());
        voteOps.put("startTime", String.valueOf(config.getStartTime().getTime()));
        voteOps.put("endTime", String.valueOf(config.getEndTime().getTime()));
        template.expire(RedisKeyHelper.servicerVoteConfig(), 1, TimeUnit.DAYS);
    }
}
