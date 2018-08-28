package com.wzitech.gamegold.common.redis;

import com.wzitech.gamegold.common.main.MainStationKeyEO;

/**
 * @author 340096
 * @date 2017/12/5.
 */
public interface IRedisCommon {
    //query userAccess from redis
    MainStationKeyEO queryUserThough();

    //add userAccess to redis
    void addUserThough(MainStationKeyEO mainStationKeyEO);

    Long incr(String key, int time);

    void incrInDay(String key, String url);
}
