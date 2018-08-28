package com.wzitech.gamegold.shorder.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

import java.math.BigDecimal;

/**出货系统配置
 * Created by Administrator on 2016/12/15.
 */
public class SystemConfig extends BaseEntity {
    /**
     * 配置名称
     */
    private String configKey;

    /**
     * 保证金余额
     */
    private String configValue;

    /**
     *备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 配置名称
     */
    private String AvailableFundKey;

    /**
     * 可用资金余额值
     */
    private String AvailableFundValue;


    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAvailableFundKey() {
        return AvailableFundKey;
    }

    public void setAvailableFundKey(String availableFundKey) {
        AvailableFundKey = availableFundKey;
    }

    public String getAvailableFundValue() {
        return AvailableFundValue;
    }

    public void setAvailableFundValue(String availableFundValue) {
        AvailableFundValue = availableFundValue;
    }
}
