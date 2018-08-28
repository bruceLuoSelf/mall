package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.SystemConfig;

/**
 * Created by Administrator on 2016/12/15.
 */
public interface ISystemConfigRedisDAO {

    public SystemConfig getByConfigKey(String configKey) ;

    public void  save(SystemConfig configEO);

    public int delete(SystemConfig configEO);
}
