package com.wzitech.gamegold.goods.dao;

import com.wzitech.gamegold.goods.entity.HotRecommendConfig;

/**
 * redis-前台安心买热卖商品配置
 * @author yemq
 *
 * Update History
 * Date         Name            Reason For Update
 * ----------------------------------------------
 * 2017/5/12    wrf              ZW_C_JB_00008商城增加通货
 */
public interface IHotRecommendConfigRedisDAO {

    /**
     * 根据游戏名称获取配置
     * @param gameName
     * @return
     */
    HotRecommendConfig getHotRecommendConfig(String gameName,String goodsTypeName);/**ZW_C_JB_00008_20170512 mod**/

    /**
     * 保存更新配置
     * @param config
     */
    void saveUpdate(HotRecommendConfig config);

    /**
     * 根据游戏名称删除配置
     * @param gameName
     */
    void remove(String gameName,String goodsTypeName);
}
