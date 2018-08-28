package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.repository.business.IAutoDeliverSettingManager;
import com.wzitech.gamegold.repository.dao.IAutoDeliverSettingDBDAO;
import com.wzitech.gamegold.repository.entity.AutoDeliverSetting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 335854 on 2015/10/15.
 */
@Component
public class AutoDeliverSettingManager extends AbstractBusinessObject implements IAutoDeliverSettingManager {
    @Autowired
    IAutoDeliverSettingDBDAO autoDeliverSettingDBDAO;

    @Override
    public GenericPage<AutoDeliverSetting> queryAutoDeliverSetting(
            Map<String, Object> queryMap, int limit, int start, String sortBy,
            boolean isAsc) {
        if (StringUtils.isEmpty(sortBy)) {
            sortBy = "ID";
        }

        GenericPage<AutoDeliverSetting> genericPage = autoDeliverSettingDBDAO.selectByMap(queryMap, limit,
                start, sortBy, isAsc);

        return genericPage;
    }

    @Override
    @Transactional
    public Boolean addAutoDeliverSetting(AutoDeliverSetting autoDeliverSetting) throws SystemException {
        if(autoDeliverSetting==null){
            throw new SystemException(ResponseCodes.SellerSettingEmptyWrong.getCode());
        }

        Map<String, Object> queryParam=new HashMap<String, Object>();
        queryParam.put("gameName",autoDeliverSetting.getGameName());
        queryParam.put("region",autoDeliverSetting.getRegion());
        if(autoDeliverSettingDBDAO.IsExsitGameAndRegion(queryParam))
        {
            return false;
        }
        else
        {
            autoDeliverSetting.setEnabled(false);
            autoDeliverSettingDBDAO.insert(autoDeliverSetting);
            return true;
        }
    }

    @Override
    @Transactional
    public void modifyAutoDeliverSetting(AutoDeliverSetting autoDeliverSetting) throws SystemException {
        // 更新DB
        autoDeliverSettingDBDAO.update(autoDeliverSetting);
    }

    @Override
    @Transactional
    public void deleteAutoDeliverSetting(List<Long> ids) throws SystemException {
        if(ids==null||ids.size()==0){
            throw new SystemException(ResponseCodes.UserIdEmptyWrong.getCode());
        }

        autoDeliverSettingDBDAO.batchDeleteByIds(ids);
    }

    @Override
    public void settingEnable(List<Long> ids,Boolean enabled) {
        List<AutoDeliverSetting> list=new ArrayList<AutoDeliverSetting>();
        for (Long id : ids) {
            AutoDeliverSetting autoDeliverSetting = autoDeliverSettingDBDAO.findById(id);
            if(autoDeliverSetting==null){
                throw new SystemException(ResponseCodes.UserNotExitedWrong.getCode());
            }
            autoDeliverSetting.setEnabled(enabled);
            list.add(autoDeliverSetting);
        }
        autoDeliverSettingDBDAO.batchUpdate(list);
    }

    @Override

    public boolean isEnableSetting(String gameName,String region)
    {
        Map<String, Object> queryParam=new HashMap<String, Object>();
        queryParam.put("gameName",gameName);
        queryParam.put("region",region);
        queryParam.put("enabled",true);
        return autoDeliverSettingDBDAO.IsExsitGameAndRegion(queryParam);
    }
}
