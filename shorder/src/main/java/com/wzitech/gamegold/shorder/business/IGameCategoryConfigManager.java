package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.GameCategoryConfig;

import java.util.List;
import java.util.Map;

/**
 * 游戏类目配置
 * 孙杨  2017/2/14
 */
public interface IGameCategoryConfigManager {
    /**
     * 查询系统配置
     *
     * @param paramMap
     * @param limit
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<GameCategoryConfig> queryPage(Map<String, Object> paramMap, int limit, int startIndex, String orderBy,
                                              Boolean isAsc);

    /**
     * 不分页查询
     * @param paramMap
     * @param orderBy
     * @param isAsc
     * @return
     */
    List<GameCategoryConfig> queryPage(Map<String, Object> paramMap, String orderBy, Boolean isAsc);
    /**
     * 删除系统配置
     */
    void deleteConfigById(Long id);

    /**
     * 增加系统配置
     */
    void addConfig(GameCategoryConfig gameCategoryConfig);

    /**
     * 修改系统配置
     */
    void updateConfig(GameCategoryConfig gameCategoryConfig);

    /**
     * 禁用配置
     *
     * @param id
     */
    void disableConfig(Long id);

    /**
     * 启用配置
     *
     * @param id
     */
    void qyConfig(Long id);

}
