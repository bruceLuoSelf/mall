package com.wzitech.gamegold.repository.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.repository.dao.ITransferRedisDao;
import com.wzitech.gamegold.repository.entity.TransferFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/20  wangmin           ZW_J_JB_00073 金币商城增加区服互通功能
 */
@Repository
public class TransferRedisDaoIMpl extends AbstractRedisDAO<TransferFile> implements ITransferRedisDao {
    @Autowired
    @Qualifier("userRedisTemplate")
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Override
    public void remove() {
        template.delete(RedisKeyHelper.transferGmeNameJsonString());
    }

    @Override
    public void save(TransferFile transferFile) {
        BoundHashOperations<String, String, String> voteOps = this.template.boundHashOps(RedisKeyHelper.transferGmeNameJsonString());
        voteOps.put(transferFile.getGameName(), jsonMapper.toJson(transferFile));
    }

    @Override
    public String getJsonString(String gameName) {
        BoundHashOperations<String, String, String> voteOps = this.template.boundHashOps(RedisKeyHelper.transferGmeNameJsonString());
        TransferFile transferFile = jsonMapper.fromJson(voteOps.get(gameName), TransferFile.class);
        if (transferFile == null) {
            return null;
        }
        return transferFile.getJsonString();
    }

    public Map<String, String> getJsonString() {
        BoundHashOperations<String, String, String> voteOps = this.template.boundHashOps(RedisKeyHelper.transferGmeNameJsonString());
        if (voteOps == null || voteOps.entries() == null) {
            return null;
        }
        return voteOps.entries();
    }

}
