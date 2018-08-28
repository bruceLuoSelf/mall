package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IPurchaserDataDao;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 采购商数据管理dao
 */
@Repository
public class PurchaserDataDaoImpl extends AbstractMyBatisDAO<PurchaserData, Long> implements IPurchaserDataDao {
    @Override
    public PurchaserData selectByIdForUpdate(long id) {
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByIdForUpdate", id);
    }

    /**
     * 根据采购商账号进行查找
     *
     * @param account
     * @return
     */
    @Override
    public PurchaserData selectByAccountForUpdate(String account) {
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByAccountForUpdate", account);
    }

    /**
     *
     * 进行Trade的模糊查询
     */
    @Override
    public List<PurchaserData> selectByLikeTrade(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectByLikeTrade", paramMap);
    }

    /**
     * 根据采购商账号进行查找
     *
     * @param account
     * @return
     */
    @Override
    public PurchaserData selectByAccount(String account) {
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByAccount", account);
    }

}
