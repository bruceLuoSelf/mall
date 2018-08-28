package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.goods.business.IFirmsAccountManager;
import com.wzitech.gamegold.goods.business.IFirmsLogManager;
import com.wzitech.gamegold.goods.dao.IFirmsAccountDBDAO;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import com.wzitech.gamegold.goods.entity.FirmsLog;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * Date         Name            Reason For Update
 * ----------------------------------------------
 * 2017/07/05   wangmin         新增用户cookie校验信息
 */
@Component
public class FirmsAccountManagerImpl extends AbstractBusinessObject implements IFirmsAccountManager {
    @Autowired
    IFirmsAccountDBDAO firmsAccountDBDAO;

    @Autowired
    private IFirmsLogManager firmsLogManager;

    @Override
    public void addFirmsAccount(FirmsAccount firmsAccount) throws SystemException {
        firmsAccountDBDAO.insert(firmsAccount);
    }

    @Override
    public void modifyFirmsAccount(FirmsAccount firmsAccount) throws SystemException {
        List<FirmsAccount> firmsAccountList = new ArrayList<FirmsAccount>();
        firmsAccountList.add(firmsAccount);
        firmsAccountDBDAO.batchUpdate(firmsAccountList);
    }

    @Override
    public void modifyFirmsNames(FirmInfo firmInfo) throws SystemException {
        firmsAccountDBDAO.batchUpdateName(firmInfo);
    }

    @Override
    public void delFirmsAccount(Long id) throws SystemException {
        FirmsAccount firmsAccount = firmsAccountDBDAO.selectById(id);
        if (firmsAccount == null) {
            return;
        }
        firmsAccountDBDAO.deleteById(id);
        this.createFirmAccountLog(firmsAccount,3);
    }

    @Override
    public GenericPage<FirmsAccount> selectFirmsAccount(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc) {
        return firmsAccountDBDAO.selectByMap(map, pageSize, start, orderBy, isAsc);
    }

    @Override
    public FirmsAccount selectByMap(FirmsAccount firmsAccount) {
        if (firmsAccount == null) {
            throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(firmsAccount.getLoginAccount())) {
            map.put("loginAccount", firmsAccount.getLoginAccount());
        }
        if (StringUtils.isNotBlank(firmsAccount.getFirmsSecret())) {
            map.put("firmsSecret", firmsAccount.getFirmsSecret());
        }
        if (StringUtils.isNotBlank(firmsAccount.getUid())) {
            map.put("uid", firmsAccount.getUid());
        }
        if (StringUtils.isNotBlank(firmsAccount.getFirmsName())) {
            map.put("firmsName", firmsAccount.getFirmsName());
        }
        if (StringUtils.isNotBlank(firmsAccount.getLoginAccount())) {
            map.put("loginAccount", firmsAccount.getLoginAccount());
        }
        List<FirmsAccount> firmsAccountList = firmsAccountDBDAO.selectByMap(map);
        return (firmsAccountList == null || firmsAccountList.size() <= 0) ? null : firmsAccountList.get(0);
    }

    @Override
    public void delAccountByfirmsSecret(String firmsSecret) {
        firmsAccountDBDAO.delAccountByfirmsSecret(firmsSecret);
    }

    /**
     * 新增方法查询厂商
     *
     * @param map
     * @return
     */
    @Override
    public FirmsAccount getFirmsAccountByMap(Map<String, Object> map) {
        return firmsAccountDBDAO.getFirmsAccountByMap(map);
    }

    /**
     * 根据id查询厂商用户
     *
     * @param id
     * @return
     */
    @Override
    public FirmsAccount selectById(Long id) {
        FirmsAccount firmsAccount = firmsAccountDBDAO.selectById(id);
        if (firmsAccount == null) {
            throw new SystemException(ResponseCodes.NoConfig.getCode(), ResponseCodes.NoConfig.getMessage());
        }
        this.createFirmAccountLog(firmsAccount, 1);
        return firmsAccount;
    }

    /**
     * 更新厂商用户授权码
     */
    @Override
    public void refreshSecretKey(Long id) {
        FirmsAccount firmsAccount = firmsAccountDBDAO.selectById(id);
        if (firmsAccount == null) {
            throw new SystemException(ResponseCodes.NoConfig.getCode(), ResponseCodes.NoConfig.getMessage());
        }
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        firmsAccount.setUserSecretKey(uuid);
        firmsAccount.setUpdateTime(new Date());
        firmsAccountDBDAO.update(firmsAccount);
        this.createFirmAccountLog(firmsAccount, 2);

    }

    /**
     * 写日志
     *
     * @param firmsAccount
     */
    private void createFirmAccountLog(FirmsAccount firmsAccount, Integer sign) {
        IUser user = CurrentUserContext.getUser();
        FirmsLog firmsLog = new FirmsLog();
        firmsLog.setUserId(user.getId());
        firmsLog.setModifyFirmName(firmsAccount.getFirmsName());
        firmsLog.setUpdateTime(new Date());
        firmsLog.setUserType(user.getUserType());
        firmsLog.setUserAccount(user.getLoginAccount());
        String log = null;
        if (sign == 1) {
            log = user.getLoginAccount() + "对" + firmsAccount.getLoginAccount() + "工作室用户进行获取授权码操作";
        } else if (sign == 2) {
            log = user.getLoginAccount() + "对" + firmsAccount.getLoginAccount() + "工作室用户进行更新授权码操作";
        }else if (sign == 3) {
            log = user.getLoginAccount() + "对" +firmsAccount.getLoginAccount() + "工作室用户进行删除操作";
        }
        firmsLog.setLog(log);
        firmsLogManager.addLog(firmsLog);
    }

    /**
     * 使用用户信息去查询到对应的用户秘钥
     *
     * @return
     */
    @Override
    public FirmsAccount getFirmsAccountSecretKey() {
        UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(user.getLoginAccount())) {
            map.put("loginAccount", user.getLoginAccount());
        }
        if (StringUtils.isNotBlank(user.getUid())) {
            map.put("uid", user.getUid());
        }
        FirmsAccount firmsAccount = firmsAccountDBDAO.getFirmsAccountByMap(map);
        if (firmsAccount == null) {
            throw new SystemException(ResponseCodes.NotOpenFunctrion.getCode(), ResponseCodes.NotOpenFunctrion.getMessage());
        }
        firmsLogManager.createFirmsAccountLog("手动获取秘钥", firmsAccount);
        if (firmsAccount.getUserSecretKey() == null) {
            firmsAccount = refreshFirmsAccountSecretKey();
        }
        return firmsAccount;
    }

    /**
     * 使用用户信息去查询到对应的用户秘钥
     *
     * @return
     */
    @Override
    public FirmsAccount refreshFirmsAccountSecretKey() {
        UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(user.getLoginAccount())) {
            map.put("loginAccount", user.getLoginAccount());
        }
        if (StringUtils.isNotBlank(user.getUid())) {
            map.put("uid", user.getUid());
        }
        FirmsAccount firmsAccount = firmsAccountDBDAO.getFirmsAccountByMap(map);
        if (firmsAccount == null) {
            throw new SystemException(ResponseCodes.NotOpenFunctrion.getCode(), ResponseCodes.NotOpenFunctrion.getMessage());
        }
        firmsAccount.setUserSecretKey(UUID.randomUUID().toString().replaceAll("-", ""));
        this.modifyFirmsAccount(firmsAccount);
        firmsLogManager.createFirmsAccountLog("手动刷新秘钥", firmsAccount);
        return firmsAccount;
    }

    /**
     * 用户手动设置秘钥
     *
     * @param key
     * @return
     */
    @Override
    public FirmsAccount setFirmsAccountSecretKey(String key) {
        UserInfoEO user = (UserInfoEO) CurrentUserContext.getUser();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(user.getLoginAccount())) {
            map.put("loginAccount", user.getLoginAccount());
        }
        if (StringUtils.isNotBlank(user.getUid())) {
            map.put("uid", user.getUid());
        }
        FirmsAccount firmsAccount = firmsAccountDBDAO.getFirmsAccountByMap(map);
        if (firmsAccount == null) {
            throw new SystemException(ResponseCodes.NotOpenFunctrion.getCode(), ResponseCodes.NotOpenFunctrion.getMessage());
        }
        firmsAccount.setUserSecretKey(key);
        this.modifyFirmsAccount(firmsAccount);
        firmsLogManager.createFirmsAccountLog("手动设置秘钥", firmsAccount);
        return firmsAccount;
    }

    @Override
    public String queryFirmsAccountByLoginAccount(String loginAccount) {
        return firmsAccountDBDAO.queryFirmsSecretByLoginAccount(loginAccount);
    }
}
