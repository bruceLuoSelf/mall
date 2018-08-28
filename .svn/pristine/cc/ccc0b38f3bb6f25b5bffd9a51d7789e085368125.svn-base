package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IPayDetailDao;
import com.wzitech.gamegold.shorder.entity.PayDetail;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 付款明细DAO
 */
@Repository
public class PayDetailDaoImpl extends AbstractMyBatisDAO<PayDetail, Long> implements IPayDetailDao{
    public int countBymap(Map<String,Object> queryParam){
        return this.getSqlSession().selectOne(this.mapperNamespace+".countByMap",queryParam);
    }
}
