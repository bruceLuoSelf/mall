package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IMachineArtificialConfigDao;
import com.wzitech.gamegold.shorder.entity.MachineArtificialConfig;
import org.springframework.stereotype.Component;

/**
 * ZW_C_JB_00004 mj
 * Created by 339931 on 2017/5/11.
 * 机器转人工配置DAO
 */
@Component
public class MachineArtificialConfigDaoImpl extends AbstractMyBatisDAO<MachineArtificialConfig,Long>
        implements IMachineArtificialConfigDao{
    /**
     * 根据游戏名称查询机器转人工配置
     *
     * @param gameName
     * @return
     */
    @Override
    public MachineArtificialConfig selectMachineArtificialConfigByName(String gameName) {
        return this.getSqlSession().selectOne(this.getMapperNamespace()+".selectByName",gameName);
    }
}
