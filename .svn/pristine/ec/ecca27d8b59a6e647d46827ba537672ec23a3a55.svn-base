package com.wzitech.gamegold.facade.backend.action.insurance;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.order.business.IInsuranceSettingsManager;
import com.wzitech.gamegold.order.entity.InsuranceSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保险功能配置
 * @author yemq
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class InsuranceSettingsAction extends AbstractAction {

    @Autowired
    private IInsuranceSettingsManager insuranceSettingsManager;

    private InsuranceSettings setting;
    private Long id;
    private Boolean enabled;
    private String gameName;
    private List<InsuranceSettings> list;


    /**
     * 保存保险配置
     * @return
     */
    public String add() {
        try {
            insuranceSettingsManager.addInsuranceSettings(setting);
            return returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改
     * @return
     */
    public String update() {
        try {
            insuranceSettingsManager.modifyInsuranceSettings(setting);
            return returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String delete() {
        try {
            insuranceSettingsManager.deleteInsuranceSettings(id);
            return returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 分页查询
     * @return
     */
    public String queryPage() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("gameName", gameName);
            GenericPage<InsuranceSettings> page = insuranceSettingsManager.queryInsuranceSettings(queryMap, "UPDATE_TIME",false, this.limit, this.start);
            list = page.getData();
            totalCount = page.getTotalCount();
            return returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用保险配置
     * @return
     */
    public String enabled() {
        try {
            insuranceSettingsManager.enabled(id);
            return returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用保险配置
     * @return
     */
    public String disabled() {
        try {
            insuranceSettingsManager.disabled(id);
            return returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public InsuranceSettings getSetting() {
        return setting;
    }

    public void setSetting(InsuranceSettings setting) {
        this.setting = setting;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<InsuranceSettings> getList() {
        return list;
    }

    public void setList(List<InsuranceSettings> list) {
        this.list = list;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
