package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.business.IConfigManager;
import com.wzitech.gamegold.shorder.dao.IConfigDao;
import com.wzitech.gamegold.shorder.dao.IConfigRedisDao;
import com.wzitech.gamegold.shorder.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/15.
 */
@Component
public class ConfigManagerImpl implements IConfigManager {

    @Autowired
    private IConfigDao   configDao;

    @Autowired
    private IConfigRedisDao   configRedisDao;


    /**
     * 添加功能
     *
     * @param config
     * @return
     */
    @Override
    @Transactional
    public Config addConfig(Config config) {
        config.setEnabled(true);
        configDao.insert(config);
        configRedisDao.save(config);
        return config;
    }

    /**
     * 修改功能
     *
     * @param config
     * @return
     */
    @Override
    @Transactional
    public Config updateConfig(Config config) {
        Config oldConfig = configDao.selectById(config.getId());
        configRedisDao.delete(oldConfig);

        configDao.update(config);
        Config newConfig = configDao.selectById(config.getId());
        configRedisDao.save(newConfig);
        return config;
    }

    /**
     * 删除功能
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void deleteConfig(Long id) {
     Config config  = configDao.selectById(id);
        if(config==null){
            return;
        }
        configDao.deleteById(id);
        configRedisDao.delete(config);

    }
    /**
     * 启用
     *
     * @param id
     */
    @Override
    @Transactional
    public void enabled(Long id) {
        Config config = new Config();
        config.setId(id);
        config.setEnabled(true);
        this.updateConfig(config);
    }
    /**
     * 禁用
     *
     * @param id
     */
    @Override
    @Transactional
    public void disabled(Long id) {
        Config config = new Config();
        config.setId(id);
        config.setEnabled(false);
        this.updateConfig(config);
    }


    /**
     * 根据id获取配资
     * @param id
     * @return
     */
    @Override
    public Config getById(Long id) {
        return configDao.selectById(id);
    }

    /**
     * 分页查询列表
     * @param paramMap
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    public GenericPage<Config> queryPage(Map<String, Object> paramMap, int pageSize, int start, String orderBy,
                                         Boolean isAsc) {
    return configDao.selectByMap(paramMap,pageSize,start,orderBy,isAsc);
}


    /**
     * 根据gamename查询配置
     * @param gameName
     * @return
     */
    @Override
    public List<Config> getConfigByGameName(String gameName){
        List<Config> configList= configRedisDao.getByGameName(gameName);
        if(configList==null || configList.isEmpty()){
            Map<String,Object> queryMap = new HashMap<String,Object>();
            queryMap.put("gameName", gameName);
            queryMap.put("enabled", true);
            configList = configDao.selectByMap(queryMap);
            if(configList!=null){
                for(Config config:configList){
                    configRedisDao.save(config);
                }
            }
        }
        return configList;
    }
}
