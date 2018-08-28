package com.wzitech.gamegold.facade.backend.action.deliverymgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.shorder.business.IConfigManager;
import com.wzitech.gamegold.shorder.entity.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jtp on 2016/12/15.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ShAddressAction extends AbstractAction {

    private  Config  config;

    private List<Config>  configList;

    private Long id;

    private List<Long> ids;

    private String gameName;

    @Autowired
    IConfigManager configManager;

    /**
     * 查询列表数据
     * @return
     */
    public  String  queryConfig() {
        try {
            HashMap<String, Object> paramMap = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(gameName)) {
                paramMap.put("gameNameLike", gameName);
            }

            GenericPage<Config> genericPage = configManager.queryPage(paramMap, this.limit, this.start, "id", false);
            configList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }
    /**
     * 添加配置
     *
     * @return
     */
    public String addConfig() {
        try {
            configManager.addConfig(config);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }
    /**
     * 修改配置
     *
     * @return
     */
    public String updateConfig() {
        try {
            configManager.updateConfig(config);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }
    /**
     * 删除配置
     *
     * @return
     */
    public String deleteConfig() {
        try {
            configManager.deleteConfig(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用
     * @return
     */
    public String enabled() {
        try {
            configManager.enabled(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用
     * @return
     */
    public String disabled() {
        try {
            configManager.disabled(id);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }



    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Config> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Config> configList) {
        this.configList = configList;
    }

}
