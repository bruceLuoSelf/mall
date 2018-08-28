package com.wzitech.gamegold.facade.backend.action.goodsmgmt;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;
import com.wzitech.gamegold.goods.business.IFirmInfoManager;
import com.wzitech.gamegold.goods.business.IFirmsAccountManager;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import com.wzitech.gamegold.repository.business.ISellerManager;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created by wangmin
 * Date:2017/7/7
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class FirmsAccountAction extends AbstractAction {

    @Autowired
    private IFirmsAccountManager firmsAccountManager;

    @Autowired
    private IFirmInfoManager firmInfoManager;

    @Autowired
    ISellerManager sellerManager;

    private List<FirmsAccount> firmInfoList;

    private String firmsName;

    private Boolean enabled;

    private Long id;

    private String loginAccount;

    private FirmsAccount firmsAccount;


    /**
     * 查询工作室信息
     */
    public String queryFirmsAccounts() throws ParseException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        if (firmsAccount != null) {
            if (!"全部".equals(firmsAccount.getFirmsName())) {
                queryMap.put("firmsName", firmsAccount.getFirmsName());
            }
            if (StringUtils.isNotBlank(firmsAccount.getLoginAccount())) {
                queryMap.put("loginAccount",firmsAccount.getLoginAccount());
            }
        }
        GenericPage<FirmsAccount> genericPage = firmsAccountManager.selectFirmsAccount(queryMap, this.limit, this.start, "id", false);
        firmInfoList = genericPage.getData();
        if (firmInfoList != null && firmInfoList.size() > 0) {
            for (FirmsAccount firmsAccount : firmInfoList) {
                firmsAccount.setFirmsSecret(null);
                firmsAccount.setUserSecretKey(null);
            }
        }
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }

    /**
     * 新增工作室用户信息
     */
    public String addFirmAccount() {
        try {
            FirmsAccount firmsAccount = new FirmsAccount();
            FirmInfo firmInfo = firmInfoManager.selectByMap(new FirmInfo(){{setFirmsName(firmsName);}});
            if(firmInfo==null) return returnError("由于工作室信息已被修改，请刷新页面后添加");
            firmsAccount.setFirmsName(firmInfo.getFirmsName());
            firmsAccount.setFirmsSecret(firmInfo.getFirmsSecret());
            SellerInfo sellerInfo = sellerManager.queryloginAccountNotLike(loginAccount);
            if (null == sellerInfo) return returnError("无法找到该卖家");
            FirmsAccount selectFirms = new FirmsAccount();
            selectFirms.setLoginAccount(loginAccount);
            selectFirms = firmsAccountManager.selectByMap(selectFirms);
            if (selectFirms != null) {
                return returnError("工作室对应的账号已存在");
            }
            firmsAccount.setEnabled(true);
            firmsAccount.setCreateTime(new Date());
            firmsAccount.setUpdateTime(new Date());
            firmsAccount.setUid(sellerInfo.getUid());
            firmsAccount.setLoginAccount(loginAccount);
            firmsAccount.setUserSecretKey(UUID.randomUUID().toString().replaceAll("-",""));
            firmsAccountManager.addFirmsAccount(firmsAccount);
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e.getMessage());
        }
    }

    /**
     * 删除工作室信息
     */
    public String deleteFirmAccount() {
        try {
            if (id != null) {
                firmsAccountManager.delFirmsAccount(id);
            }
            return returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e.getMessage());
        }
    }

    /**
     * 启用
     */
    public String ableFirm() {
        try {
            FirmsAccount firmsAccount = new FirmsAccount();
            firmsAccount.setId(id);
            firmsAccount.setEnabled(true);
            firmsAccount.setUpdateTime(new Date());
            firmsAccountManager.modifyFirmsAccount(firmsAccount);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e.getMessage());
        }
    }

    /**
     * 禁用
     *
     * @return
     */
    public String disableFirm() {
        try {
            FirmsAccount firmsAccount = new FirmsAccount();
            firmsAccount.setId(id);
            firmsAccount.setEnabled(false);
            firmsAccount.setUpdateTime(new Date());
            firmsAccountManager.modifyFirmsAccount(firmsAccount);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e.getMessage());
        }
    }

    /**
     * 更新工作室名称
     */
    public String updateFirmAccount() {
        try {
            FirmsAccount firmsAccount = new FirmsAccount();
            firmsAccount.setId(id);

            SellerInfo sellerInfo = sellerManager.queryloginAccountNotLike(loginAccount);
            if (null == sellerInfo) return returnError("无法找到该卖家");

            FirmsAccount selectFirms = new FirmsAccount();
            selectFirms.setLoginAccount(loginAccount);
            selectFirms = firmsAccountManager.selectByMap(selectFirms);
            if (selectFirms != null) {
                return returnError("该账号对应的工作室已存在");
            }
            firmsAccount.setId(id);
            firmsAccount.setLoginAccount(sellerInfo.getLoginAccount());
            firmsAccount.setUid(sellerInfo.getUid());

            firmsAccountManager.modifyFirmsAccount(firmsAccount);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e.getMessage());
        }
    }

    /**
     * 获取厂商用户授权码
     * @return
     */
    public String selectFirmUserSecret() {
        try{
            firmsAccount = firmsAccountManager.selectById(id);
            firmsAccount.setFirmsSecret(null);
            return this.returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e.getErrorMsg());
        }
    }

    /**
     * 更新厂商用户授权码
     * @return
     */
    public String refreshSecretKey() {
        try{
            firmsAccountManager.refreshSecretKey(id);
            return this.returnSuccess();
        }catch (SystemException e) {
            return this.returnError(e.getErrorMsg());
        }
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

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public void setFirmsName(String firmsName) {
        this.firmsName = firmsName;
    }

    public String getFirmsName() {
        return firmsName;
    }

    public List<FirmsAccount> getFirmInfoList() {
        return firmInfoList;
    }

    public void setFirmInfoList(List<FirmsAccount> firmInfoList) {
        this.firmInfoList = firmInfoList;
    }

    public FirmsAccount getFirmsAccount() {
        return firmsAccount;
    }

    public void setFirmsAccount(FirmsAccount firmsAccount) {
        this.firmsAccount = firmsAccount;
    }
}
