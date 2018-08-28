package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.LevelCarryLimitEO;

import java.util.Map;

/**
 * Created by 339939 on 2018/4/3.
 */
public interface ILevelCarryLimitManager {

    void add(LevelCarryLimitEO levelCarryLimitEO);

    void deleteById(Long id);

    void updateLevelCarryLimit(LevelCarryLimitEO levelCarryLimitEO);

    GenericPage<LevelCarryLimitEO> selectByMap(Map<String,Object> map, int pageSize, int start, String orderBy, boolean isAsc);

    LevelCarryLimitEO selectById(Long id);

    Long selectCarryUpperLimit(int level,String gameName,Long goodsTypeId);

    void buildRedisData();
}
