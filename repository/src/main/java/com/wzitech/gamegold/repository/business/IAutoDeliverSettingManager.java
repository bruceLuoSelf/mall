package com.wzitech.gamegold.repository.business;

import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.repository.entity.AutoDeliverSetting;

import java.util.List;
import java.util.Map;

/**
 * Created by 335854 on 2015/10/15.
 */
public interface IAutoDeliverSettingManager {
    /**
     * 根据条件分页查询配置
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     * @throws BusinessException
     */
    GenericPage<AutoDeliverSetting> queryAutoDeliverSetting(Map<String, Object> queryMap, int limit, int start,
                                                  String sortBy, boolean isAsc);

    /**
     * 新增自动配单
     * @param autoDeliverSetting
     * @return
     * @throws BusinessException
     */
    public Boolean addAutoDeliverSetting(AutoDeliverSetting autoDeliverSetting);

    /**
     * 修改自动配单
     * @param autoDeliverSetting
     * @return
     * @throws BusinessException
     */
    public void modifyAutoDeliverSetting(AutoDeliverSetting autoDeliverSetting);

    /**
     * 删除自动配单
     * @param ids
     * @return
     * @throws BusinessException
     */
    public void deleteAutoDeliverSetting(List<Long> ids);

    /**
     * 设置自动配单的可用与否
     * @param ids
     * @param isDeleted
     * @return
     * @throws BusinessException
     */
    public void settingEnable(List<Long> ids,Boolean isDeleted);

    /**
     * 根据游戏区服查看是否开通寄售自动化
     * @param gameName
     * @param region
     * @return
     */
    public boolean isEnableSetting(String gameName,String region);
}
