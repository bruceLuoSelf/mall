package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.Config;
import com.wzitech.gamegold.shorder.entity.SystemConfig;

import java.util.List;


/**
 * Created by Administrator on 2016/12/15.
 */
public interface IConfigRedisDao {
    public List<Config> getByGameName(String gameName) ;

    public void  save(Config configEO);

    public int delete(Config configEO);

    /**
     * 存储从7bao获取可用配置金额至redis
     * */
    void saveSevenBaoFund(SystemConfig systemConfig);

    /**
     * 查询7bao可用收货配置金
     * */
    SystemConfig querySevenBaoFund();

    /**
     * 删除7bao可用收货配置金
     * */
    void deleteSevenBaoFund();
}
