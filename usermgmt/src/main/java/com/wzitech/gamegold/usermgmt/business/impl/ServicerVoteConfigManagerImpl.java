package com.wzitech.gamegold.usermgmt.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.usermgmt.business.IServicerVoteConfigManager;
import com.wzitech.gamegold.usermgmt.dao.rdb.IServicerVoteConfigDBDAO;
import com.wzitech.gamegold.usermgmt.dao.redis.IServicerVoteConfigRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.ServicerVoteConfigEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客服投票配置管理
 *
 * @author yemq
 */
@Component
public class ServicerVoteConfigManagerImpl extends AbstractBusinessObject implements IServicerVoteConfigManager {
    @Autowired
    private IServicerVoteConfigDBDAO servicerVoteConfigDBDAO;
    @Autowired
    private IServicerVoteConfigRedisDAO servicerVoteConfigRedisDAO;

    /**
     * 获取客服投票配置信息
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public ServicerVoteConfigEO loadConfig() {
        // 先从redis中获取
        ServicerVoteConfigEO config = servicerVoteConfigRedisDAO.loadConfig();
        if (config != null)
            return config;

        // 从数据库获取
        List<ServicerVoteConfigEO> configs = servicerVoteConfigDBDAO.selectAll();
        if (configs == null || configs.size() == 0) {
            throw new SystemException(ResponseCodes.EmptyVoteConfig.getCode(), ResponseCodes.EmptyVoteConfig.getMessage());
        } else if (configs.size() > 1) {
            throw new SystemException(ResponseCodes.MultiVoteConfig.getCode(), ResponseCodes.MultiVoteConfig.getMessage());
        }
        config = configs.get(0);

        // 存入redis
        servicerVoteConfigRedisDAO.saveConfig(config);
        return config;
    }
}
