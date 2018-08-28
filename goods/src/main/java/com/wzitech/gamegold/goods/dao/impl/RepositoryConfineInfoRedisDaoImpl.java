package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.goods.dao.IRepositoryConfineInfoRedisDao;
import com.wzitech.gamegold.goods.entity.RepositoryConfineInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author mengj
 */
@Component
public class RepositoryConfineInfoRedisDaoImpl extends AbstractRedisDAO<RepositoryConfineInfo> implements IRepositoryConfineInfoRedisDao {
    @Autowired
    @Qualifier("userRedisTemplate")
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
    private String RepositoryConfineInfoRedisKey = "gamegold:serviceRepositoryConfineInfo";


    /**
     * 根据游戏名称、区、服、阵营查询游戏库存配置
     *
     * @param gameName
     * @param regionName
     * @param serverName
     * @param raceName
     * @return
     */
    @Override
    public RepositoryConfineInfo selectRepositoryConfineInfo(String gameName, String regionName, String serverName, String raceName,String goodsTypeName) {
        String key = RedisKeyHelper.serviceRepositoryConfineInfo(gameName, regionName, serverName, raceName,goodsTypeName);
        String json = valueOps.get(key);
        return jsonMapper.fromJson(json, RepositoryConfineInfo.class);
    }

    /**
     * 增加和修改游戏库存配置
     *
     * @param entity
     */
    @Override
    public void addRepositoryConfineInfo(RepositoryConfineInfo entity) {
        if (entity != null) {
            String json = jsonMapper.toJson(entity);
            String key = RedisKeyHelper.serviceRepositoryConfineInfo(entity.getGameName(), entity.getRegionName(),
                    entity.getServerName(), entity.getRaceName(),entity.getGoodsTypeName());
            valueOps.set(key, json);
        }
    }

    /**
     * 删除游戏库存配置
     *
     * @param entity
     */
    @Override
    public void deleteRepositoryConfineInfo(RepositoryConfineInfo entity) {
        String key = RedisKeyHelper.serviceRepositoryConfineInfo(entity.getGameName(), entity.getRegionName(),
                entity.getServerName(), entity.getRaceName(),entity.getGoodsTypeName());
        template.delete(key);
    }
}
