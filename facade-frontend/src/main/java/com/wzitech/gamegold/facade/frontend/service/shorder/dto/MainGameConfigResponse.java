package com.wzitech.gamegold.facade.frontend.service.shorder.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.shorder.entity.MainGameConfig;

import java.util.List;

/**
 * Created by jhlcitadmin on 2017/2/23.
 */
public class MainGameConfigResponse extends AbstractServiceResponse{
    private List<MainGameConfig> mainGameConfigList;

    private MainGameConfig mainGameConfig;

    public List<MainGameConfig> getMainGameConfigList() {
        return mainGameConfigList;
    }

    public void setMainGameConfigList(List<MainGameConfig> mainGameConfigList) {
        this.mainGameConfigList = mainGameConfigList;
    }

    public MainGameConfig getMainGameConfig() {
        return mainGameConfig;
    }

    public void setMainGameConfig(MainGameConfig mainGameConfig) {
        this.mainGameConfig = mainGameConfig;
    }
}
