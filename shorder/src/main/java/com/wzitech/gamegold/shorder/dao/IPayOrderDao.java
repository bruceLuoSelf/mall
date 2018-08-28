package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.PayOrder;

import java.util.List;
import java.util.Map;

/**
 * 收货商支付订单DAO
 */
public interface IPayOrderDao extends IMyBatisBaseDAO<PayOrder, Long> {
    /**
     * 根据订单号查询支付单
     * @param orderId 支付订单号
     * @param isLocked 是否锁定
     * @return
     */
    PayOrder selectByOrderId(String orderId, Boolean isLocked);
    public int countBymap(Map<String,Object> queryParam);

    public void batchUpdateByIds(List<PayOrder> list);
}
