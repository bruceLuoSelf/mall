package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.MainGameConfig;

import java.util.List;

/**
 * Created by jhlcitadmin on 2017/2/23.
 */
public interface IMainGameConfigManager {
    /**
     * 查询全部开启的游戏配置
     */
    List<MainGameConfig> getMainGameConfig();

    /**
     * 增加游戏配置
     */
    void addMainGameConfig(MainGameConfig mainGameConfig);

    /**
     * 修改游戏配置
     */
    void updateMainGameConfig(MainGameConfig mainGameConfig);

    /**
     * 删除游戏配置
     */
    void deleteConfigById(Long id);

    /**
     * 开启收货
     */
    void qyDelivery(Long id);

    /**
     * 禁用收货
     */
    void disableDelivery(Long id);

    /**
     * 开启商城出售
     */
    void qySell(Long id);

    /**
     * 关闭商城出售
     */
    void disableSell(Long id);
}
