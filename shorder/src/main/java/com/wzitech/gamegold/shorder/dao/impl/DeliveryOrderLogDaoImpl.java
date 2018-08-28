package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderLogDao;
import com.wzitech.gamegold.shorder.entity.DeliverySubOrder;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 出货单日志DAO
 */
@Repository
public class DeliveryOrderLogDaoImpl extends AbstractMyBatisDAO<OrderLog, Long> implements IDeliveryOrderLogDao {

    @Override
    public boolean selectSellerDeliveryTimeOut(Map<String,Object> selectMap) {
        Boolean b = getSqlSession().selectOne(getMapperNamespace() + ".selectSellerDeliveryTimeOut", selectMap);
        return b == null ? false : b;
    }

}
