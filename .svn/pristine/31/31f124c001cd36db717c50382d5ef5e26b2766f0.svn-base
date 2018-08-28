package com.wzitech.gamegold.order.business.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import org.apache.commons.lang3.StringUtils;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.order.business.IInsuranceSettingsManager;
import com.wzitech.gamegold.order.dao.IInsuranceSettingsDao;
import com.wzitech.gamegold.order.entity.InsuranceSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 保险功能配置
 */
@Component
public class InsuranceSettingsManager implements IInsuranceSettingsManager {

    @Autowired
    IInsuranceSettingsDao insuranceSettingsDao;

    /*新增*/
    @Transactional
    public InsuranceSettings addInsuranceSettings(InsuranceSettings insuranceSettings) throws SystemException {
        if (StringUtils.isEmpty(insuranceSettings.getGameName())) {
            throw new SystemException(
                    ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getCode());
        }
        if (BigDecimal.ZERO.compareTo(insuranceSettings.getRate()) == 0) {
            throw new SystemException(ResponseCodes.RateCannotBeZero.getCode(), ResponseCodes.RateCannotBeZero.getMessage());
        }
        insuranceSettings.setUpdateTime(new Date());
        insuranceSettingsDao.insert(insuranceSettings);
        return insuranceSettings;
    }

    /*删除*/
    @Transactional
    public void deleteInsuranceSettings(Long id) throws SystemException {
        insuranceSettingsDao.deleteById(id);
    }

    /*修改*/
    @Transactional
    public InsuranceSettings modifyInsuranceSettings(InsuranceSettings insuranceSettings) throws SystemException {
        if (BigDecimal.ZERO.compareTo(insuranceSettings.getRate()) == 0) {
            throw new SystemException(ResponseCodes.RateCannotBeZero.getCode(), ResponseCodes.RateCannotBeZero.getMessage());
        }
        insuranceSettings.setUpdateTime(new Date());
        insuranceSettingsDao.update(insuranceSettings);
        return insuranceSettings;

    }

    /**
     * 启用保险配置
     * @param id
     */
    @Transactional
    public void enabled(Long id) {
        InsuranceSettings insuranceSettings = new InsuranceSettings();
        insuranceSettings.setId(id);
        insuranceSettings.setEnabled(true);
        insuranceSettings.setUpdateTime(new Date());
        insuranceSettingsDao.update(insuranceSettings);
    }

    /**
     * 禁用保险配置
     * @param id
     */
    @Transactional
    public void disabled(Long id) {
        InsuranceSettings insuranceSettings = new InsuranceSettings();
        insuranceSettings.setId(id);
        insuranceSettings.setEnabled(false);
        insuranceSettings.setUpdateTime(new Date());
        insuranceSettingsDao.update(insuranceSettings);
    }

    /*查找*/
    public List<InsuranceSettings> queryInsuranceSettingsList() {
        return null;

    }

    @Override
    public GenericPage<InsuranceSettings> queryInsuranceSettings(Map<String, Object> queryMap, String orderBy,
                                                                 boolean isAsc, int pageSize,
                                                                 int start) throws SystemException {
        return insuranceSettingsDao.selectByMap(queryMap, pageSize, start, orderBy, isAsc);
    }

    /**
     * 根据游戏名称，查询保险配置
     *
     * @param gameName
     * @return
     */
    @Override
    public InsuranceSettings queryByGameName(String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            throw new SystemException(
                    ResponseCodes.EmptyGameName.getCode(), ResponseCodes.EmptyGameName.getCode());
        }
        InsuranceSettings settings = insuranceSettingsDao.selectUniqueByProp("gameName", gameName);
        return settings;
    }
}
