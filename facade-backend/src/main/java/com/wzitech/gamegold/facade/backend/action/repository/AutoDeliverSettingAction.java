package com.wzitech.gamegold.facade.backend.action.repository;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.repository.business.IAutoDeliverSettingManager;
import com.wzitech.gamegold.repository.entity.AutoDeliverSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 335854 on 2015/10/15.
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class AutoDeliverSettingAction extends AbstractAction {
    private List<AutoDeliverSetting> autoDeliverSettingList;

    private AutoDeliverSetting autoDeliverSetting;

    private Long id;

    private List<Long> ids;

    @Autowired
    IAutoDeliverSettingManager autoDeliverSettingManager;

    /**
     * 查询寄售全自动配置列表
     * @return
     */

    public String queryAutoDeliverSetting() {
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if(autoDeliverSetting!=null) {
            paramMap.put("gameName", autoDeliverSetting.getGameName());
            paramMap.put("region", autoDeliverSetting.getRegion());
        }
        GenericPage<AutoDeliverSetting> genericPage = autoDeliverSettingManager.queryAutoDeliverSetting(paramMap, this.limit, this.start, "ID",true);
        autoDeliverSettingList = genericPage.getData();
        totalCount = genericPage.getTotalCount();

        return this.returnSuccess();
    }

    /**
     * 新增配置
     * @return
     * @throws IOException
     */
    public String addAutoDeliverSetting() throws IOException {
        try {
            Boolean flag=autoDeliverSettingManager.addAutoDeliverSetting(autoDeliverSetting);

            String message=flag?"新增成功":"该游戏大区下已存在配置";
            return this.returnSuccess(message);
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改配置
     * @return
     * @throws IOException
     */
    public String modifyAutoDeliverSetting() throws IOException {
        try {
            autoDeliverSetting.setId(id);
            autoDeliverSettingManager.modifyAutoDeliverSetting(autoDeliverSetting);
            return this.returnSuccess("修改成功");
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除配置
     * @return
     */
    public String deleteAutoDeliverSetting() {
        try {
            autoDeliverSettingManager.deleteAutoDeliverSetting(ids);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用和禁用配置
     * @return
     */
    public String settingEnableDeliverSetting() {
        try {
            autoDeliverSettingManager.settingEnable(ids, autoDeliverSetting.getEnabled());
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public List<AutoDeliverSetting> getAutoDeliverSettingList() {
        return autoDeliverSettingList;
    }

    public void setAutoDeliverSettingList(List<AutoDeliverSetting> autoDeliverSettingList) {
        this.autoDeliverSettingList = autoDeliverSettingList;
    }

    public void setAutoDeliverSetting(AutoDeliverSetting autoDeliverSetting) {
        this.autoDeliverSetting = autoDeliverSetting;
    }

    public AutoDeliverSetting getAutoDeliverSetting() {
        return autoDeliverSetting;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
