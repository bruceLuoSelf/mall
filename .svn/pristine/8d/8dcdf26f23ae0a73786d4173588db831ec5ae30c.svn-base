package com.wzitech.gamegold.order.business.impl;

import com.wzitech.gamegold.order.business.IInsuranceOrderManager;
import com.wzitech.gamegold.order.dao.IInsuranceOrderDao;
import com.wzitech.gamegold.order.entity.InsuranceOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 保单管理
 *
 * @author yemq
 */
@Component
public class InsuranceOrderManagerImpl implements IInsuranceOrderManager {
    @Autowired
    private IInsuranceOrderDao insuranceOrderDao;

    /**
     * 新建一个订单
     *
     * @param order
     */
    @Override
    public void addOrder(InsuranceOrder order) {
        Date now = new Date();

        if (order.getType() == null) {
            order.setType(InsuranceOrder.TYPE_CREATE_ORDER);
        }

        order.setCreateTime(now);
        order.setLastUpdateTime(now);
        insuranceOrderDao.insert(order);
    }

    /**
     * 修改订单
     *
     * @param order
     */
    public void updateOrder(InsuranceOrder order) {
        order.setLastUpdateTime(new Date());
        insuranceOrderDao.update(order);
    }

    /**
     * 查询需要创建保单的记录
     *
     * @return
     */
    public List<InsuranceOrder> queryNeedCreateBQList() {
        return insuranceOrderDao.queryNeedCreateBQList();
    }

    /**
     * 查询需要提交保单结单的记录
     *
     * @return
     */
    public List<InsuranceOrder> queryNeedModifyTransferTimeList() {
        return insuranceOrderDao.queryNeedModifyTransferTimeList();
    }

    /**
     * 根据ID，删除订单
     *
     * @param id
     */
    @Transactional
    public void removeOrderById(Long id) {
        insuranceOrderDao.deleteById(id);
    }
}
