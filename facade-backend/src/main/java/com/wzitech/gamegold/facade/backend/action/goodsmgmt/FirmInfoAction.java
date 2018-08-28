package com.wzitech.gamegold.facade.backend.action.goodsmgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.goods.business.IFirmInfoManager;
import com.wzitech.gamegold.goods.business.IFirmsAccountManager;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/7/7
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class FirmInfoAction extends AbstractAction {

    @Autowired
    private IFirmInfoManager firmInfoManager;

    @Autowired
    private IFirmsAccountManager firmsAccountManager;

    private List<FirmInfo> firmInfoList;

    private FirmInfo firmInfo;

    private String firmsName;

    private Boolean enabled;

    private Long id;

    /**
     * 查询工作室信息
     */
    public String queryFirmInfos() throws ParseException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (!"全部".equals(firmsName)) {
            queryMap.put("firmsName", firmsName);
        }
        GenericPage<FirmInfo> genericPage = firmInfoManager.selectFirms(queryMap, this.limit, this.start, "id", false);
        firmInfoList = genericPage.getData();
        if (firmInfoList != null && firmInfoList.size() > 0) {
            for (FirmInfo firmInfo : firmInfoList) {
                firmInfo.setFirmsSecret(null);
            }
        }
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }


    /**
     * 新增工作室信息
     */
    public String addFirm() {
        try {
            FirmInfo firmInfo = new FirmInfo();
            firmInfo.setFirmsName(firmsName);
            FirmInfo selectFirmInfo = firmInfoManager.selectByMap(firmInfo);
            if (selectFirmInfo != null) {
                return returnError("该工作室已存在");
            }
            firmInfo.setEnabled(enabled);
            firmInfoManager.addFirmInfo(firmInfo);

            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除工作室信息
     */
    public String deleteFirmInfo() {
        try {
            if (id != null) {
                FirmInfo firmInfo = firmInfoManager.selectById(id);
                firmInfoManager.delFirmInfo(id);
                firmsAccountManager.delAccountByfirmsSecret(firmInfo.getFirmsSecret());
            }
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用
     */
    public String able() {
        try {
            FirmInfo firmInfo = new FirmInfo();
            firmInfo.setId(id);
            firmInfo.setEnabled(true);
            firmInfoManager.modifyFirmInfo(firmInfo);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用
     *
     * @return
     */
    public String disable() {
        try {
            FirmInfo firmInfo = new FirmInfo();
            firmInfo.setId(id);
            firmInfo.setEnabled(false);
            firmInfoManager.modifyFirmInfo(firmInfo);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 更新工作室名称
     */
    public String updateFirmName() {
        try {
            firmInfoManager.updateFimrs(id, firmsName);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 记录查询秘钥的操作记录
     */
    public String addLogForSelect() {
        try {
            IUser currentUser = CurrentUserContext.getUser();
//            currentUser.get
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /***
     * 更新工作室密匙
     *
     * @return
     */
    public String refreshSecret() {
        try {
            firmInfoManager.refreshSecret(firmInfo);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 获取工作室密匙
     *
     * @return
     */
    public String showSecret() {
        try {
            firmInfo = firmInfoManager.getSecret(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public List<FirmInfo> getFirmInfoList() {
        return firmInfoList;
    }

    public void setFirmInfoList(List<FirmInfo> firmInfoList) {
        this.firmInfoList = firmInfoList;
    }

    public void setFirmsName(String firmsName) {
        this.firmsName = firmsName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FirmInfo getFirmInfo() {
        return firmInfo;
    }

    public void setFirmInfo(FirmInfo firmInfo) {
        this.firmInfo = firmInfo;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
