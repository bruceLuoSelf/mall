package com.wzitech.gamegold.shorder.dao.impl;

/**
 * Created by jtp on 2016/12/15.
 */

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IConfigRedisDao;
import com.wzitech.gamegold.shorder.entity.Config;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 通过缓存生成配置
 */
@Repository
public class ConfigRedisDaoImpl extends AbstractRedisDAO<Config>  implements IConfigRedisDao{

    private static String CONFIG_KEY = "gamegold:shorder:config:";

    private static String SEVEN_BAO_FUND = "gamegold:shorder:config:sevenbaofund";

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
    /**
     * 获取
     * @param gameName
     */
    @Override
    public List<Config> getByGameName(String gameName) {
        BoundHashOperations<String, String, String> bhOps = template.boundHashOps(CONFIG_KEY+gameName);
        List<Config> list = new ArrayList<Config>();

        if(bhOps!=null && bhOps.size()>0){
            for(String jsonStr:bhOps.values()){
                Config config = jsonMapper.fromJson(jsonStr, Config.class);
                list.add(config);
            }
        }
        return list;
    }

    /**
     * 保存
     * @param configEO
     */
    @Override
    public void save(Config configEO) {
        if (configEO != null && !StringUtils.isBlank(configEO.getGameName())){
            if(configEO.getEnabled()==true){
                BoundHashOperations<String, String, String> bhOps = template.boundHashOps(CONFIG_KEY+ configEO.getGameName());
                String json = jsonMapper.toJson(configEO);
                bhOps.put(configEO.getId().toString(), json);
            }else{
                delete(configEO);
            }

        }

    }
    /**
     * 删除
     * @param configEO
     */
    @Override
    public int delete(Config configEO){
        if (configEO != null && !StringUtils.isBlank(configEO.getGameName())) {
            // 删除对应的keys
            BoundHashOperations<String, String, String> bhOps = template.boundHashOps(CONFIG_KEY+configEO.getGameName());
            if(bhOps!=null){
                bhOps.delete(configEO.getId().toString());
            }
        }
        return 0;
    }

    /**
     * 存储从7bao获取可用配置金额至redis
     * */
    @Override
    public void saveSevenBaoFund(SystemConfig systemConfig) {
        if (systemConfig != null) {
            BoundHashOperations<String, String, String> bhOps = template.boundHashOps(SEVEN_BAO_FUND);
            String json = jsonMapper.toJson(systemConfig);
            bhOps.put("1", json);
            //cache twenty-four hours
            template.expire(SEVEN_BAO_FUND, 24, TimeUnit.HOURS);
        }
    }
    /**
     * 查询7bao可用收货配置金
     * */
    @Override
    public SystemConfig querySevenBaoFund(){
        BoundHashOperations<String, String, String> bhOps = template.boundHashOps(SEVEN_BAO_FUND);
        SystemConfig systemConfig = null;
        List<SystemConfig> list = new ArrayList<SystemConfig>();

        if (bhOps != null && bhOps.size() > 0) {
            for (String jsonStr : bhOps.values()) {
                SystemConfig config = jsonMapper.fromJson(jsonStr, SystemConfig.class);
                list.add(config);
            }
        }
        if (list != null && list.size() > 0) {
            systemConfig = list.get(0);
        }
        return systemConfig;
    }

    /**
     * 删除7bao可用收货配置金
     * */
    @Override
    public void deleteSevenBaoFund(){
        BoundHashOperations<String, String, String> bhOps = template.boundHashOps(SEVEN_BAO_FUND);
        if(bhOps!=null){
            bhOps.delete("1");
        }
    }
}

