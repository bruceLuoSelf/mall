package com.wzitech.gamegold.repository.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.repository.dao.IAutoDeliverSettingDBDAO;
import com.wzitech.gamegold.repository.entity.AutoDeliverSetting;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by 335854 on 2015/10/15.
 */
@Repository
public class AutoDeliverSettingDBDAOImpl extends AbstractMyBatisDAO<AutoDeliverSetting, Long> implements
        IAutoDeliverSettingDBDAO {
        @Override
        public AutoDeliverSetting findById(Long  uid){
                if(uid==null){
                        return null;
                }
                return selectUniqueByProp("id",uid);
        }

        @Override
        public Boolean IsExsitGameAndRegion(Map<String, Object> paramMap) {
                return countByMap(paramMap)>=1;
        }
}
