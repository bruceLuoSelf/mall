package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.MachineArtificialConfig;

/**
 * ZW_C_JB_00004 mj
 * Created by 339931 on 2017/5/11.
 * 机器转人工配置Dao
 */

public interface IMachineArtificialConfigDao extends IMyBatisBaseDAO<MachineArtificialConfig,Long> {

    /**
     * 根据游戏名称查询机器转人工配置
     * @param gameName
     * @return
     */
    public MachineArtificialConfig selectMachineArtificialConfigByName(String gameName);
}
