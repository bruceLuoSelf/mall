package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.MachineArtificialConfig;

import java.util.List;
import java.util.Map;

/**
 * ZW_C_JB_00004 mj
 * Created by 339931 on 2017/5/11.
 */
public interface IMachineArtificialConfigManager {

    /**
     * 分页查询
     *
     * @param map
     * @param pageSize
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<MachineArtificialConfig> selectByMap(Map<String, Object> map, int pageSize, int startIndex, String orderBy, boolean isAsc);

    /**
     * 根据游戏名称查询机器转人工配置
     *
     * @param gameName
     * @return
     */
    MachineArtificialConfig selectByGameName(String gameName);

    /**
     * 新增数据
     *
     * @param entity
     */
    void addMachineArtificialConfig(MachineArtificialConfig entity);


    /**
     * 启用配置
     *
     * @param entity
     */
    void enableConfig(MachineArtificialConfig entity);

    /**
     * 禁用配置
     *
     * @param entity
     */
    void disabledConfig(MachineArtificialConfig entity);

    /**
     * 根据id删除数据
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据游戏名获取自动分配客服开关
     *
     * @return
     */
    public boolean queryenableByGameName(String gameName);
}
