package com.wzitech.gamegold.shorder.business;

/**
 * Created by Administrator on 2016/12/15.
 */

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.Config;

import java.util.List;
import java.util.Map;

/**
 * 收货地址配置manager
 */
public interface IConfigManager {

    /**
     * 添加收货配置
     *
     * @param config
     * @return
     */
    Config addConfig(Config config);

    /**
     * 修改收货配置
     *
     * @param config
     * @return
     */
    Config updateConfig(Config config);

    /**
     * 删除收货配置
     *
     * @param id
     * @return
     */
    void deleteConfig(Long id);
    /**
     * 根据ID获取配置
     *
     * @param id
     * @return
     */
    Config getById(Long id);

    /**
     * 根据name查询配置
     * @param gameName
     * @return
     */
    List<Config> getConfigByGameName(String gameName);

    /**
     * 启用
     * @param id
     */
    void enabled(Long id);

    /**
     * 禁用
     * @param id
     */
    void disabled(Long id);


    /**
     * 分页查找记录
     * @param paramMap
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<Config> queryPage(Map<String, Object> paramMap, int pageSize, int start, String orderBy,
                                  Boolean isAsc);
}
