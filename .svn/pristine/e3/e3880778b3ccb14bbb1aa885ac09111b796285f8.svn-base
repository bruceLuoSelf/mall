package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.shorder.business.IMachineArtificialConfigManager;
import com.wzitech.gamegold.shorder.dao.IMachineArtificialConfigDao;
import com.wzitech.gamegold.shorder.entity.MachineArtificialConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * ZW_C_JB_00004 mj
 * Created by 339931 on 2017/5/11.
 */
@Component
public class MachineArtificialConfigManagerImpl implements IMachineArtificialConfigManager {

    @Autowired
    private IMachineArtificialConfigDao machineArtificialConfigDao;

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
    @Override
    public GenericPage<MachineArtificialConfig> selectByMap(Map<String, Object> map,
                                                            int pageSize, int startIndex, String orderBy, boolean isAsc) {
        if (pageSize == 0) {
            pageSize = 25;
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "id";
        }
        return machineArtificialConfigDao.selectByMap(map, pageSize, startIndex, orderBy, isAsc);
    }

    /**
     * 根据游戏名称查询机器转人工配置
     *
     * @param gameName
     * @return
     */
    @Override
    public MachineArtificialConfig selectByGameName(String gameName) {
        if (StringUtils.isBlank(gameName)) {
            throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                    ResponseCodes.EmptyGameName.getMessage());
        }
        return machineArtificialConfigDao.selectMachineArtificialConfigByName(gameName);
    }

    /**
     * 新增数据
     *
     * @param entity
     */
    @Override
    public void addMachineArtificialConfig(MachineArtificialConfig entity) {
        if (entity != null) {
            if (StringUtils.isBlank(entity.getGameName())) {
                throw new SystemException(ResponseCodes.EmptyGameName.getCode(),
                        ResponseCodes.EmptyGameName.getMessage());
            }
            Date time = new Date();
            entity.setCreateTime(time);
            entity.setUpdateTime(time);
            machineArtificialConfigDao.insert(entity);
        }
    }

    /**
     * 启用配置
     *
     * @param entity
     */
    @Override
    public void enableConfig(MachineArtificialConfig entity) {
        if (entity != null) {
            if (entity.getId() == null) {
                throw new SystemException(ResponseCodes.NullMachineArtificialConfigId.getCode(),
                        ResponseCodes.NullMachineArtificialConfigId.getMessage());
            }
            MachineArtificialConfig machineArtificialConfig = machineArtificialConfigDao.selectById(entity.getId());
            if (machineArtificialConfig == null) {
                throw new SystemException(ResponseCodes.MachineNotGameName.getCode(),
                        ResponseCodes.MachineNotGameName.getMessage());
            }
            if (machineArtificialConfig.getEnable()) {
                throw new SystemException(ResponseCodes.RepositoryIsEnableWrong.getCode(),
                        ResponseCodes.RepositoryIsEnableWrong.getMessage());
            }
            machineArtificialConfig.setEnable(true);
            machineArtificialConfigDao.update(machineArtificialConfig);
        }
    }

    /**
     * 禁用配置
     *
     * @param entity
     */
    @Override
    public void disabledConfig(MachineArtificialConfig entity) {
        if (entity != null) {
            if (entity.getId() == null) {
                throw new SystemException(ResponseCodes.NullMachineArtificialConfigId.getCode(),
                        ResponseCodes.NullMachineArtificialConfigId.getMessage());
            }
            MachineArtificialConfig machineArtificialConfig = machineArtificialConfigDao.selectById(entity.getId());
            if (machineArtificialConfig == null) {
                throw new SystemException(ResponseCodes.MachineNotGameName.getCode(),
                        ResponseCodes.MachineNotGameName.getMessage());
            }
            if (!machineArtificialConfig.getEnable()) {
                throw new SystemException(ResponseCodes.RepositoryIsDisableWrong.getCode(),
                        ResponseCodes.RepositoryIsDisableWrong.getMessage());
            }
            machineArtificialConfig.setEnable(false);
            machineArtificialConfig.setUpdateTime(new Date());
            machineArtificialConfigDao.update(machineArtificialConfig);
        }
    }

    /**
     * 根据id删除数据
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new SystemException(ResponseCodes.NullMachineArtificialConfigId.getCode(),
                    ResponseCodes.NullMachineArtificialConfigId.getMessage());
        }
        machineArtificialConfigDao.deleteById(id);
    }

    /**
     * 根据游戏名获取自动分配客服开关
     * ZW_C_JB_00004 sunyang
     * @param gameName
     * @return
     */
    @Override
    public boolean queryenableByGameName(String gameName) {
        MachineArtificialConfig machineArtificialConfig = machineArtificialConfigDao.selectMachineArtificialConfigByName(gameName);
        if (machineArtificialConfig==null) {
            new SystemException(ResponseCodes.MachineNotGameName.getCode(),
                    ResponseCodes.MachineNotGameName.getMessage());
        }
        return machineArtificialConfig.getEnable();
    }
}
