package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.ShGameConfig;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */
public interface IShGameConfigRedisDao {
    public void save(ShGameConfig shGameConfig);

    public int delete(ShGameConfig shGameConfig);

    public ShGameConfig getByConfigKey(String gameName, String goodsTypeName);

    public List<ShGameConfig> selectByName(String name);

    /**
     * 根据游戏名称和开关获取配置
     * @param name
     * @param isEnabled
     * @param enableMall
     * @return
     */
    List<ShGameConfig> selectByNameAndSwitch(String name, Boolean isEnabled, Boolean enableMall);
}
