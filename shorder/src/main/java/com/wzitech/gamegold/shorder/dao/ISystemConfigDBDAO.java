package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.SystemConfig;

/**
 * Created by Administrator on 2016/12/15.
 */
public interface ISystemConfigDBDAO extends IMyBatisBaseDAO<SystemConfig, Long> {

    /**
     * 根据KEY查询VALUE
     * @param key
     * @return
     */
    SystemConfig selectByConfigKey(String key);


}
