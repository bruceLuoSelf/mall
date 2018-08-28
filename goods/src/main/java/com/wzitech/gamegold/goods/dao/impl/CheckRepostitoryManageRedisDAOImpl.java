package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.enums.SystemConfigEnum;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.goods.dao.ICheckRespostitoryManageRedisDAO;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/7  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 */
@Repository
public class CheckRepostitoryManageRedisDAOImpl extends AbstractRedisDAO<FirmInfo> implements ICheckRespostitoryManageRedisDAO {
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
    @Autowired
    ISystemConfigManager systemConfigManager;


    @Override
    public Integer saveFirmAndLoginAccount(String uuid, String firmsSecret, String loginAccount, String uid) {
        FirmsAccount firmsAccount = new FirmsAccount();
        firmsAccount.setFirmsSecret(firmsSecret);
        firmsAccount.setLoginAccount(loginAccount);
        firmsAccount.setUid(uid);
        firmsAccount.setEnabled(null);
        String json = jsonMapper.toJson(firmsAccount);
        int minutes = 120;
        //根据key查询生效时间,以分钟为单位
        SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.FIRMS_TIMEOUT.getKey());
        //未能生效就将其设置为120
        if (systemConfig != null || !systemConfig.getEnabled()) {
            minutes = Integer.parseInt(systemConfig.getConfigValue());
        }
        valueOps.set(RedisKeyHelper.uuidKey(uuid), json, minutes, TimeUnit.MINUTES);
        return minutes;
    }


    @Override
    public String saveFirmAndLoginAccount(String loginAccount) {
        int minutes = 120;
        SystemConfig systemConfig = systemConfigManager.getSystemConfigByKey(SystemConfigEnum.FIRMS_TIMEOUT.getKey());
        if (systemConfig != null && systemConfig.getEnabled()) {
            minutes = Integer.parseInt(systemConfig.getConfigValue());
        }
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        valueOps.set(RedisKeyHelper.tokenKeyForOuterWebSite(loginAccount), uuid, minutes, TimeUnit.MINUTES);
        return uuid;
    }

    @Override
    public String getToken(String loginAccount) {
        String token = valueOps.get(RedisKeyHelper.tokenKeyForOuterWebSite(loginAccount));
        return StringUtils.isBlank(token) ? null : token;
    }

    @Override
    public String getRedisValue(String cookie) {
        String json = valueOps.get(RedisKeyHelper.uuidKey(cookie));
        return StringUtils.isBlank(json) ? null : json;
    }
}
