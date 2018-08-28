package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IMainGameConfigManager;
import com.wzitech.gamegold.shorder.dao.IMainGameConfigDao;
import com.wzitech.gamegold.shorder.dao.IMainGameConfigRedisDao;
import com.wzitech.gamegold.shorder.entity.MainGameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by jhlcitadmin on 2017/2/23.
 */
@Component
public class MainGameConfigManagerImpl implements IMainGameConfigManager {
    @Autowired
    private IMainGameConfigDao mainGameConfigDao;
    @Autowired
    private IMainGameConfigRedisDao mainGameConfigRedisDao;

    /**
     * 查询开启收货的游戏名
     *
     * @return
     */
    @Override
    public List<MainGameConfig> getMainGameConfig() {
        List<MainGameConfig> list = new ArrayList<MainGameConfig>();
        list = mainGameConfigRedisDao.selectAll();
        if (list != null && list.size() <= 0) {
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("ableDelivery", true);
            list = mainGameConfigDao.selectByMap(query);
            if (list.size() <= 0) {
                throw new SystemException(ResponseCodes.NotAvailableGameConfig.getCode(),
                        ResponseCodes.NotAvailableGameConfig.getMessage());
            }
            //将数据库查询的数据存到redis
            for (MainGameConfig mainGameConfig : list) {
                mainGameConfigRedisDao.save(mainGameConfig);
            }
        }
        return list;
    }

    /**
     * 增加总游戏配置项
     */
    @Override
    public void addMainGameConfig(MainGameConfig mainGameConfig) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gameName", mainGameConfig.getGameName());
        map.put("gameId", mainGameConfig.getGameId());
        int count = mainGameConfigDao.countByMap(map);
        if (count > 0) {
            throw new SystemException(ResponseCodes.MainGameConfigExist.getCode(),
                    ResponseCodes.MainGameConfigExist.getMessage());
        }
        if (mainGameConfig != null) {
            mainGameConfig.setAbleSell(true);
            mainGameConfig.setAbleDelivery(true);
            mainGameConfig.setCreateTime(new Date());
            mainGameConfig.setUpdateTime(new Date());
            mainGameConfigDao.insert(mainGameConfig);
            //将数据保存到redis中
            mainGameConfigRedisDao.save(mainGameConfig);
        }
    }

    /**
     * 修改主游戏配置
     *
     * @param mainGameConfig
     */
    @Override
    public void updateMainGameConfig(MainGameConfig mainGameConfig) {
        if (mainGameConfig != null) {
            mainGameConfig.setUpdateTime(new Date());
            MainGameConfig entity = mainGameConfigDao.selectById(mainGameConfig.getId());
            mainGameConfigDao.update(mainGameConfig);
            if (entity.getAbleDelivery()) {
                //删除之前的数据再保存更新后的数据
                mainGameConfigRedisDao.delete(entity);
                entity = mainGameConfigDao.selectById(mainGameConfig.getId());
                mainGameConfigRedisDao.save(entity);
            } else {
                mainGameConfigRedisDao.delete(entity);
            }
        }
    }

    /**
     * 删除主游戏配置
     *
     * @param id
     */
    @Override
    public void deleteConfigById(Long id) {
        MainGameConfig mainGameConfig = mainGameConfigDao.selectById(id);
        if (mainGameConfig != null) {
            //先删除redis中的数据
            mainGameConfigRedisDao.delete(mainGameConfig);
            mainGameConfigDao.deleteById(id);
        }
    }

    /***
     * 开启收货
     */
    @Override
    public void qyDelivery(Long id) {
        if (id != null) {
            MainGameConfig mainGameConfig = mainGameConfigDao.selectById(id);
            if (mainGameConfig != null) {
                mainGameConfig.setAbleDelivery(true);
                mainGameConfigDao.update(mainGameConfig);
                //保存到redis中
                mainGameConfigRedisDao.save(mainGameConfig);
            }
        }
    }

    /**
     * 关闭收货
     *
     * @param id
     */
    @Override
    public void disableDelivery(Long id) {
        if (id != null) {
            MainGameConfig mainGameConfig = mainGameConfigDao.selectById(id);
            if (mainGameConfig != null) {
                //关闭收获时删除redis中的数据
                mainGameConfigRedisDao.delete(mainGameConfig);
                mainGameConfig.setAbleDelivery(false);
                mainGameConfigDao.update(mainGameConfig);
            }
        }
    }

    /***
     * 启用商城出售
     *
     * @param id
     */
    @Override
    public void qySell(Long id) {
        if (id != null) {
            MainGameConfig mainGameConfig = mainGameConfigDao.selectById(id);
            if (mainGameConfig != null) {
                mainGameConfig.setAbleSell(true);
                mainGameConfigDao.update(mainGameConfig);
                if (mainGameConfig.getAbleDelivery()) {
                    //启用商城出售时删除redis中的数据
                    mainGameConfigRedisDao.delete(mainGameConfig);
                    mainGameConfig = mainGameConfigDao.selectById(id);
                    //保存到redis中
                    mainGameConfigRedisDao.save(mainGameConfig);
                }
            }
        }
    }

    /**
     * 禁用商城出售
     *
     * @param id
     */
    @Override
    public void disableSell(Long id) {
        if (id != null) {
            MainGameConfig mainGameConfig = mainGameConfigDao.selectById(id);
            if (mainGameConfig != null) {
                mainGameConfig.setAbleSell(false);
                mainGameConfigDao.update(mainGameConfig);
                if (mainGameConfig.getAbleDelivery()) {
                    //禁用商城出售时删除redis中的数据
                    mainGameConfigRedisDao.delete(mainGameConfig);
                    mainGameConfig = mainGameConfigDao.selectById(id);
                    //保存到redis中
                    mainGameConfigRedisDao.save(mainGameConfig);
                }
            }
        }
    }

}
