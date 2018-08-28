package com.wzitech.gamegold.facade.backend.action.repository;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IMachineArtificialConfigManager;
import com.wzitech.gamegold.shorder.entity.MachineArtificialConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 339931 on 2017/5/12.
 */
@Controller("machineArtificialConfigAction")
@Scope("prototype")
@ExceptionToJSON
public class MachineArtificialConfigAction extends AbstractAction {
    @Autowired
    private IMachineArtificialConfigManager machineArtificialConfigManager;

    private List<MachineArtificialConfig> machineArtificialConfigList;

    private MachineArtificialConfig machineArtificialConfig;

    private Long configId;

    private String gameName;

    /**
     * 分页查询
     *
     * @return
     */
    public String queryMachineArtificialConfigList() {
        Map<String, Object> map = new HashMap<String, Object>();
        if (machineArtificialConfig != null) {
            //根据游戏名查询
            if (StringUtils.isNotBlank(machineArtificialConfig.getGameName())) {
                map.put("gameName", machineArtificialConfig.getGameName());
            }
        }
        GenericPage<MachineArtificialConfig> genericPage = machineArtificialConfigManager.selectByMap(map,
                this.limit, this.start, "id", true);
        machineArtificialConfigList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 新增机器转人工配置
     *
     * @return
     */
    public String addMachineArtificialConfig() {
        MachineArtificialConfig entity = null;
        //根据游戏名查询数据是否存在
        if (StringUtils.isNotBlank(machineArtificialConfig.getGameName())) {
            entity = machineArtificialConfigManager.selectByGameName(machineArtificialConfig.getGameName());
        }
        if (entity != null) {
            return returnError("该机器转人工配置已存在");
        }
        machineArtificialConfigManager.addMachineArtificialConfig(machineArtificialConfig);
        return this.returnSuccess();
    }

    /**
     * 启用配置
     *
     * @return
     */
    public String enableMachineArtificialConfig() {
        machineArtificialConfigManager.enableConfig(machineArtificialConfig);
        return this.returnSuccess();
    }

    /**
     * 禁用配置
     *
     * @return
     */
    public String disabledMachineArtificialConfig() {
        machineArtificialConfigManager.disabledConfig(machineArtificialConfig);
        return this.returnSuccess();
    }

    /**
     * 删除
     *
     * @return
     */
    public String deleteMachineArtificialConfig() {
        machineArtificialConfigManager.deleteById(configId);
        return returnSuccess();
    }


    public IMachineArtificialConfigManager getMachineArtificialConfigManager() {
        return machineArtificialConfigManager;
    }

    public void setMachineArtificialConfigManager(IMachineArtificialConfigManager machineArtificialConfigManager) {
        this.machineArtificialConfigManager = machineArtificialConfigManager;
    }

    public List<MachineArtificialConfig> getMachineArtificialConfigList() {
        return machineArtificialConfigList;
    }

    public void setMachineArtificialConfigList(List<MachineArtificialConfig> machineArtificialConfigList) {
        this.machineArtificialConfigList = machineArtificialConfigList;
    }

    public MachineArtificialConfig getMachineArtificialConfig() {
        return machineArtificialConfig;
    }

    public void setMachineArtificialConfig(MachineArtificialConfig machineArtificialConfig) {
        this.machineArtificialConfig = machineArtificialConfig;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
