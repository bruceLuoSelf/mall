package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IPayOrderDao;
import com.wzitech.gamegold.shorder.entity.PayOrder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货商支付订单DAO
 */
@Repository
public class PayOrderDaoImpl extends AbstractMyBatisDAO<PayOrder, Long> implements IPayOrderDao {
    /**
     * 根据订单号查询支付单
     *
     * @param orderId  支付订单号
     * @param isLocked 是否锁定
     * @return
     */
    @Override
    public PayOrder selectByOrderId(String orderId, Boolean isLocked) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId);
        paramMap.put("isLocked", isLocked);
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByOrderId", paramMap);
    }

    public int countBymap(Map<String,Object> queryParam){
        return this.getSqlSession().selectOne(this.mapperNamespace+".countByMap",queryParam);
    }
    public void batchUpdateByIds(List<PayOrder> list){
        this.getSqlSession().update(this.mapperNamespace+".batchUpdateByIds",list);
    }
}
