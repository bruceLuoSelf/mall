package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.ISplitRepositorySubRequestDao;
import com.wzitech.gamegold.shorder.entity.SplitRepositorySubRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 339928 on 2018/6/19.
 */
@Repository
public class SplitRepositorySubRequestDaoImple extends AbstractMyBatisDAO<SplitRepositorySubRequest, Long> implements ISplitRepositorySubRequestDao {

    @Override
    public List<SplitRepositorySubRequest> getSubOrderList(String orderId) {
        return getSqlSession().selectList(getMapperNamespace() + ".getSubOrderList", orderId);
    }

    @Override
    public List<SplitRepositorySubRequest> selectRealCountZeroWithId(Long id) {
        return getSqlSession().selectList(getMapperNamespace()+".selectRealCountZeroWithId",id);
    }
}
