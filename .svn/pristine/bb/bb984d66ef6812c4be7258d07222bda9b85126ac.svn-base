package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IMainGameConfigDao;
import com.wzitech.gamegold.shorder.dao.IMainGameConfigRedisDao;
import com.wzitech.gamegold.shorder.entity.MainGameConfig;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by mengjie on 2017/3/4.
 */
@Component
public class MainGameConfigRedisDaoImpl extends AbstractRedisDAO<MainGameConfig> implements IMainGameConfigRedisDao{

    private static String MAINGAMECONFIG_KEY = "gamegold:shorder:MainGameConfig:";

    @Autowired
    private IMainGameConfigDao mainGameConfigDao;

    @Autowired
    @Qualifier("userRedisTemplate")
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    /**
     * 查询所有主游戏配置
     * @return
     */
    @Override
    public List<MainGameConfig> selectAll() {
        Set<String> values= zSetOps.range(MAINGAMECONFIG_KEY,0,-1);
        List<MainGameConfig> gameConfigList=new ArrayList<MainGameConfig>();
        for(String gameConfigJson:values){
            MainGameConfig entity = jsonMapper.fromJson(gameConfigJson,MainGameConfig.class);
            gameConfigList.add(entity);
        }
        return gameConfigList;
    }


    /**
     * 新增主游戏配置
     * @param entity
     */
    @Override
    public void save(MainGameConfig entity) {
        String json = jsonMapper.toJson(entity);
        zSetOps.add(MAINGAMECONFIG_KEY,json,entity.getId());
    }

    /**
     * 删除主游戏配置
     * @param mainGameConfig
     */
    @Override
    public void delete(MainGameConfig mainGameConfig) {
        zSetOps.removeRangeByScore(MAINGAMECONFIG_KEY, mainGameConfig.getId(), mainGameConfig.getId());
    }

}
