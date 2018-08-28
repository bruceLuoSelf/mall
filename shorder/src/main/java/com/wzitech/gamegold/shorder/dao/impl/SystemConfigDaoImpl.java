package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.ISystemConfigDBDAO;
import com.wzitech.gamegold.shorder.entity.SystemConfig;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/12/15.
 */
@Repository
public class SystemConfigDaoImpl extends AbstractMyBatisDAO<SystemConfig, Long> implements ISystemConfigDBDAO {

    @Override
    public  SystemConfig selectByConfigKey(String key) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectByConfigKey", key);
    }


}
