package com.wzitech.gamegold.repository.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.repository.entity.AutoDeliverSetting;

import java.util.Map;

/**
 * Created by 335854 on 2015/10/15.
 */
public interface IAutoDeliverSettingDBDAO extends IMyBatisBaseDAO<AutoDeliverSetting, Long> {
    public AutoDeliverSetting findById(Long  uid);

    /**
     *
     * 是否存在
     * @author wangjunjie
     * @param paramMap
     * @return
     * @see
     */
    public Boolean IsExsitGameAndRegion(Map<String, Object> paramMap);
}
