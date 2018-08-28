package com.wzitech.gamegold.gamegold.app.service.order.dto;

import com.wzitech.gamegold.gamegold.app.dto.Response;

/**
 * Created by 340032 on 2018/3/22.
 */
public class AppNewInterfaceResponse extends Response {
//    /**
//     * 配置名称(url路径)
//     */
//    private String configKey;

    /**
     * 是否启用
     */
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

//    public String getConfigKey() {
//        return configKey;
//    }
//
//    public void setConfigKey(String configKey) {
//        this.configKey = configKey;
//    }
}
