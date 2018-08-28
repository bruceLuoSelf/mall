package com.wzitech.gamegold.facade.backend.action.shpurchase;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.ISystemConfigManager;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/15.
 */

@Controller
@Scope("prototype")
@ExceptionToJSON
public class queryConfigAction  extends AbstractAction {

    private SystemConfig systemConfig;

    @Autowired
    private ISystemConfigManager systemConfigManager;

    private List<SystemConfig> configList;


    //------------------------------------------------------------------

    /**
     * 查询出货地址配置列表
     *
     * @return
     */
    public String queryConfigOrder() {
        Map<String, Object> queryMap = new HashMap<String, Object>();
//        if (StringUtils.isNotBlank(systemConfig.getConfigKey())) {
//            queryMap.put("configKey", systemConfig.getConfigKey());
//        }
        GenericPage<SystemConfig> genericPage = systemConfigManager.
                queryPage(queryMap, this.limit, this.start, "id", false);
        configList = genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    //-------------------------------------------------------------


    public List<SystemConfig> getConfigList() {
        return configList;
    }

    public void setConfigList(List<SystemConfig> configList) {
        this.configList = configList;
    }

    public ISystemConfigManager getAddressConfigManager() {
        return systemConfigManager;
    }

    public void setAddressConfigManager(ISystemConfigManager systemConfigManager) {
        this.systemConfigManager = systemConfigManager;
    }

    public SystemConfig getAddressConfig() {
        return systemConfig;
    }

    public void setAddressConfig(SystemConfig systemConfig) {
        this.systemConfig = systemConfig;
    }
}
