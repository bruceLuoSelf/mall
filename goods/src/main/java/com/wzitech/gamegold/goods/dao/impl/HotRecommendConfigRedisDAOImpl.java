package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.goods.dao.IHotRecommendConfigRedisDAO;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis-前台安心买热卖商品配置
 * @author yemq
 *   Update History
 * Date         Name            Reason For Update
 * ----------------------------------------------
 * 2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 */
@Component
public class HotRecommendConfigRedisDAOImpl extends AbstractRedisDAO<HotRecommendConfig> implements IHotRecommendConfigRedisDAO {

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    private JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    /**
     * 根据游戏名称获取配置
     *
     * @param gameName
     * @return
     */
    @Override
    public HotRecommendConfig getHotRecommendConfig(String gameName,String goodsTypeName) {
        BoundHashOperations<String, String, String> ops = template.boundHashOps(RedisKeyHelper.HotRecommendConfig(gameName,goodsTypeName)); /**ZW_C_JB_00008_20170512 mod**/
        String json = ops.get("json");
        HotRecommendConfig config = jsonMapper.fromJson(json, HotRecommendConfig.class);
        return config;
    }

    /**
     * 保存更新配置
     * @param config
     */
    @Override
    public void saveUpdate(HotRecommendConfig config) {
        String json = jsonMapper.toJson(config);
        BoundHashOperations<String, String, String> hashOps = template.boundHashOps(RedisKeyHelper.HotRecommendConfig(config.getGameName(),config.getGoodsTypeName()));/**ZW_C_JB_00008_20170512 add**/
        hashOps.put("json", json);
    }

    /**
     * 根据游戏名称删除配置
     * @param gameName
     */
    @Override
    public void remove(String gameName,String goodsTypeName) {
        BoundHashOperations<String, String, String> ops = template.boundHashOps(RedisKeyHelper.HotRecommendConfig(gameName,goodsTypeName));
        ops.delete("json");
    }
}
