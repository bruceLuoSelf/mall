package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.entity.IUser;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.goods.business.IFirmInfoManager;
import com.wzitech.gamegold.goods.dao.IFirmInfoDBDAO;
import com.wzitech.gamegold.goods.dao.IFirmsAccountDBDAO;
import com.wzitech.gamegold.goods.entity.FirmInfo;
import com.wzitech.gamegold.goods.entity.FirmsAccount;
import com.wzitech.gamegold.goods.entity.FirmsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * Date         Name            Reason For Update
 * ----------------------------------------------
 * 2017/07/05   wangmin         新增用户cookie校验信息
 * 2017/07/17   wubiao          工作室用户逻辑修改
 */
@Component
public class FirmInfoManagerImpl extends AbstractBusinessObject implements IFirmInfoManager {
    @Autowired
    IFirmInfoDBDAO firmInfoDBDAO;

    @Autowired
    private FirmsLogManagerImpl firmsLogManager;

    @Autowired
    private IFirmsAccountDBDAO firmsAccountDBDAO;

    @Override
    @Transactional
    public FirmInfo addFirmInfo(FirmInfo firmInfo) throws SystemException {
        if (StringUtils.isEmpty(firmInfo.getFirmsName())) {
            throw new SystemException(ResponseCodes.NotEmptyFirmName.getCode(), ResponseCodes.NotEmptyFirmName.getMessage());
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        //  记录日志
        firmsLogManager.createFirmLog("新增",firmInfo);
        firmInfo.setFirmsSecret(uuid);
        //firmInfo.setEnabled(true);
        firmInfo.setCreateTime(new Date());
        firmInfo.setUpdateTime(new Date());
        firmInfoDBDAO.insert(firmInfo);
        return firmInfo;
    }

    @Override
    @Transactional
    public FirmInfo modifyFirmInfo(FirmInfo firmInfo) throws SystemException {
        if (firmInfo == null) {
            throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());
        }
        //  记录日志
        firmsLogManager.createFirmLog("权限操作",firmInfo);
        List<FirmInfo> list = new ArrayList<FirmInfo>();
        firmInfo.setUpdateTime(new Date());
        list.add(firmInfo);
        firmInfoDBDAO.batchUpdate(list);
        return firmInfo;
    }

    /**
     * 新增条件查询方法
     *
     * @param firmInfo
     * @return
     * @throws SystemException
     */
    @Override
    public FirmInfo selectByMap(FirmInfo firmInfo) throws SystemException {
        if (firmInfo == null) {
            throw new SystemException(ResponseCodes.EmptyParams.getCode(), ResponseCodes.EmptyParams.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(firmInfo.getFirmsSecret())) {
            map.put("firmsSecret", firmInfo.getFirmsSecret());
        }
        if (StringUtils.isNotBlank(firmInfo.getFirmsName())) {
            map.put("firmsName", firmInfo.getFirmsName());
        }
        List<FirmInfo> firmInfoList = firmInfoDBDAO.selectByMap(map);
        return (firmInfoList == null || firmInfoList.size() <= 0) ? null : firmInfoList.get(0);
    }

    @Override
    public FirmInfo selectById(Long id) throws SystemException {
        return firmInfoDBDAO.selectById(id);
    }

    @Override
    public void delFirmInfo(Long id) throws SystemException {
        FirmInfo firmInfo = firmInfoDBDAO.selectById(id);
        if (null != firmInfo) {
            firmInfoDBDAO.deleteById(id);
        }
        //  记录日志
        firmsLogManager.createFirmLog("删除",firmInfo);
    }

    @Override
    public GenericPage<FirmInfo> selectFirms(Map<String, Object> map, int pageSize, int start, String orderBy, Boolean isAsc) {
        return firmInfoDBDAO.selectByMap(map, pageSize, start, orderBy, isAsc);
    }
    @Override
    public void updateFimrs(Long id,String firmsName) {
        FirmInfo firmInfo=new FirmInfo();
        firmInfo.setFirmsName(firmsName);
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(firmInfo.getFirmsName())) {
            map.put("firmsName", firmInfo.getFirmsName());
        }
        List<FirmInfo> firmInfoList = firmInfoDBDAO.selectByMap(map);
        FirmInfo selectFirmInfo= (firmInfoList == null || firmInfoList.size() <= 0) ? null : firmInfoList.get(0);
        if(selectFirmInfo!=null){
            throw new SystemException(ResponseCodes.FirmHasAlreadyExist.getCode(),ResponseCodes.FirmHasAlreadyExist.getMessage());
        }
        selectFirmInfo=firmInfoDBDAO.selectById(id);
        firmInfo.setId(id);
        firmsLogManager.createFirmLog("修改名称",firmInfo);
        List<FirmInfo> list = new ArrayList<FirmInfo>();
        firmInfo.setUpdateTime(new Date());
        list.add(firmInfo);
        firmInfoDBDAO.batchUpdate(list);
        firmInfo.setFirmsSecret(selectFirmInfo.getFirmsSecret());
        firmsAccountDBDAO.batchUpdateName(firmInfo);
    }


    /**
     * 更新工作室密匙
     * @param firmInfo
     */
    @Override
    @Transactional
    public void refreshSecret(FirmInfo firmInfo) {
        if (firmInfo == null || StringUtils.isEmpty(firmInfo.getFirmsName())) {
            throw new SystemException(ResponseCodes.NotEmptyFirmName.getCode(), ResponseCodes.NotEmptyFirmName.getMessage());
        }
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //  记录日志
        firmsLogManager.createFirmLog("更新密匙", firmInfo);
        firmInfo.setFirmsSecret(uuid);
        firmInfo.setUpdateTime(new Date());
        firmInfoDBDAO.update(firmInfo);
        //同步用户工作室密匙
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("firmsName",firmInfo.getFirmsName());
        List<FirmsAccount> firmsAccountList = firmsAccountDBDAO.selectByMap(map);
        for(FirmsAccount firmsAccount:firmsAccountList){
            firmsAccount.setFirmsSecret(uuid);
            firmsAccount.setUpdateTime(new Date());
        }
        firmsAccountDBDAO.batchUpdateSecret(firmsAccountList);
    }

    @Override
    public FirmInfo getSecret(Long id) {
        if (id == null) {
            throw new SystemException(ResponseCodes.ParamNotEmpty.getCode(), ResponseCodes.ParamNotEmpty.getMessage());
        }
        FirmInfo FirmInfoResult = new FirmInfo();
        FirmInfoResult.setId(id);
        FirmInfoResult =  firmInfoDBDAO.selectById(FirmInfoResult.getId());
        firmsLogManager.createFirmLog("获取密匙", FirmInfoResult);
        return FirmInfoResult;
    }
}
