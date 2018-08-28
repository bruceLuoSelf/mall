package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.ILevelCarryLimitDao;
import com.wzitech.gamegold.shorder.entity.LevelCarryLimitEO;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by 339939 on 2018/4/3.
 */
@Repository
public class LevelCarryLimitDaoImpl extends AbstractMyBatisDAO<LevelCarryLimitEO,Long> implements ILevelCarryLimitDao {

    @Override
    public LevelCarryLimitEO selectCarryUpperLimit(Map<String,Object> map) {
        return this.getSqlSession().selectOne(this.getMapperNamespace() + ".selectCarryUpperLimit",map);
    }
}
