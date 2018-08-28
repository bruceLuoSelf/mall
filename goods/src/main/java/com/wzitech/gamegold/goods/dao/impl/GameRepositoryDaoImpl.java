package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IGameRepositoryDao;
import com.wzitech.gamegold.goods.entity.RepositoryConfineInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by sunyang on 2017/3/27.
 */
@Repository
public class GameRepositoryDaoImpl extends AbstractMyBatisDAO<RepositoryConfineInfo, Long> implements IGameRepositoryDao {

    @Override
    public RepositoryConfineInfo selectRepositoryByName(String gameName) {
        return this.getSqlSession().selectOne(this.getMapperNamespace()+".selectRepositoryByName",gameName);
    }
}
