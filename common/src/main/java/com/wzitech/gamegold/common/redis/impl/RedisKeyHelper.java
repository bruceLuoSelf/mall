package com.wzitech.gamegold.common.redis.impl;

/**
 * Created by 汪俊杰 on 2018/4/19.
 */
public class RedisKeyHelper {
    private static String baseKey = "gamegold:frotend:";

    public static String getAccessLimitKey(String key) {
        return baseKey + key;
    }

    public static String getAccessLimitDayKey() {
        return baseKey + "day";
    }
}
