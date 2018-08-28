package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.LevelCarryLimitEO;

/**
 * Created by ljn on 2018/4/12.
 */
public interface ILevelCarryLimitRedisDao {

    void addLevelCarryLimit(LevelCarryLimitEO levelCarryLimitEO);

    void deleteLevelCarryLimit(LevelCarryLimitEO levelCarryLimitEO);

    LevelCarryLimitEO getLevelCarryLimit(int level,String gameName,Long goodsTypeId);

    void deleteAll();
}
