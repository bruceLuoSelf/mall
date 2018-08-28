package com.wzitech.gamegold.order.business;

import java.util.List;
import java.util.Map;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.entity.InsuranceSettings;
import com.wzitech.gamegold.order.entity.OrderInfoEO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 保险功能配置
 */
public interface IInsuranceSettingsManager {
    /*新增*/
    InsuranceSettings addInsuranceSettings(InsuranceSettings insuranceSettings) throws SystemException;

    /*删除*/
    void deleteInsuranceSettings(Long id) throws SystemException;

    /*修改*/
    InsuranceSettings modifyInsuranceSettings(InsuranceSettings insuranceSettings) throws SystemException;

    /**
     * 启用保险配置
     * @param id
     */
    public void enabled(Long id);

    /**
     * 禁用保险配置
     * @param id
     */
    public void disabled(Long id);

    /*查找*/
    List<InsuranceSettings> queryInsuranceSettingsList();

    GenericPage<InsuranceSettings> queryInsuranceSettings(Map<String, Object> queryMap, String orderBy, boolean isAsc, int pageSize, int start) throws SystemException;

    /**
     * 根据游戏名称，查询保险配置
     *
     * @param gameName
     * @return
     */
    InsuranceSettings queryByGameName(String gameName);
}
