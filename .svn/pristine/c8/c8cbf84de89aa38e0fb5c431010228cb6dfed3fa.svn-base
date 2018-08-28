package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IRefundOrderDao;
import com.wzitech.gamegold.shorder.entity.RefundOrder;
import org.springframework.stereotype.Repository;

/**
 * 退款记录
 */
@Repository
public class RefundOrderDaoImpl extends AbstractMyBatisDAO<RefundOrder, Long> implements IRefundOrderDao {
    @Override
    public RefundOrder selectByIdForUpdate(Long id) {
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByIdForUpdate", id);
    }
}
