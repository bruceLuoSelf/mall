package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;

import java.util.List;
import java.util.Map;

/**
 * 前台安心买热卖商品配置管理
 *         Update History
 *         Date         Name            Reason For Update
 *         ----------------------------------------------
 *         2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 * @author yemq
 */
public interface IHotRecommendConfigManager {

    /**
     * 添加配置
     *
     * @param config
     * @return
     */
    HotRecommendConfig add(HotRecommendConfig config);

    /**
     * 更新配置
     *
     * @param config
     * @return
     */
    HotRecommendConfig update(HotRecommendConfig config);

    /**
     * 根据ID批量删除
     *
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 分页查询
     *
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    GenericPage<HotRecommendConfig> queryConfigList(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc);

    /**
     * 从redis中获取配置
     *
     * @param gameName
     * @return
     */
    public HotRecommendConfig getConfigFromRedis(String gameName,String goodsTypeName); /**ZW_C_JB_00008_20170512 add   **/
}
