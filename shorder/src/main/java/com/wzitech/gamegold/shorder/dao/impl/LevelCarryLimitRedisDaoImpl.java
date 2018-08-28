package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.shorder.dao.ILevelCarryLimitRedisDao;
import com.wzitech.gamegold.shorder.entity.LevelCarryLimitEO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by ljn on 2018/4/12.
 */
@Component
public class LevelCarryLimitRedisDaoImpl extends AbstractRedisDAO<LevelCarryLimitEO> implements ILevelCarryLimitRedisDao {


    @Autowired
    @Qualifier("userRedisTemplate")
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Override
    public void addLevelCarryLimit(LevelCarryLimitEO levelCarryLimitEO) {
        String json = jsonMapper.toJson(levelCarryLimitEO);
        String key = RedisKeyHelper.getLevelCarryUpperLimit(levelCarryLimitEO.getGameName(),levelCarryLimitEO.getGoodsTypeId());
        zSetOps.add(key,json,levelCarryLimitEO.getMinLevel());
    }

    @Override
    public void deleteLevelCarryLimit(LevelCarryLimitEO levelCarryLimitEO) {
        String key = RedisKeyHelper.getLevelCarryUpperLimit(levelCarryLimitEO.getGameName(),levelCarryLimitEO.getGoodsTypeId());
        zSetOps.removeRangeByScore(key,levelCarryLimitEO.getMinLevel(),levelCarryLimitEO.getMinLevel());
    }

    @Override
    public LevelCarryLimitEO getLevelCarryLimit(int level,String gameName,Long goodsTypeId) {
        String key = RedisKeyHelper.getLevelCarryUpperLimit(gameName,goodsTypeId);
        Set<String> limits = zSetOps.rangeByScore(key, 1, level);
        if (CollectionUtils.isEmpty(limits)) {
            return null;
        }
        List<LevelCarryLimitEO> list = new ArrayList<LevelCarryLimitEO>();
        for (String json : limits) {
            LevelCarryLimitEO levelCarryLimitEO = jsonMapper.fromJson(json, LevelCarryLimitEO.class);
            list.add(levelCarryLimitEO);
        }
        return list.get(list.size()-1);
    }

    @Override
    public void deleteAll() {
        String key = RedisKeyHelper.levelCarryUpperLimitForDelete();
        Set<String> keys = template.keys(key);
        Iterator<String> it = keys.iterator();
        while(it.hasNext()) {
            String k = it.next();
            template.delete(k);
        }
    }
}