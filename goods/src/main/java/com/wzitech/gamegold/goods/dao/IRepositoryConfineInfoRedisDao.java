package com.wzitech.gamegold.goods.dao;

import com.wzitech.gamegold.goods.entity.RepositoryConfineInfo;

/**
 * 游戏库存配置redis
 *
 * @author mengj
 */
public interface IRepositoryConfineInfoRedisDao {

    /**
     * 根据游戏名称、区、服、阵营查询游戏库存配置
     *
     * @param gameName
     * @param regionName
     * @param serverName
     * @param raceName
     * @return
     */
    RepositoryConfineInfo selectRepositoryConfineInfo(String gameName, String regionName, String serverName, String raceName,String goodsTypeName);

    /**
     * 增加和修改游戏库存配置
     *
     * @param entity
     */
    void addRepositoryConfineInfo(RepositoryConfineInfo entity);

    /**
     * 删除游戏库存配置
     *
     * @param entity
     */
    void deleteRepositoryConfineInfo(RepositoryConfineInfo entity);
}
