package com.wzitech.gamegold.order.business;

import com.wzitech.gamegold.order.entity.InsuranceOrder;

import java.util.List;

/**
 * 保单管理
 *
 * @author yemq
 */
public interface IInsuranceOrderManager {

    /**
     * 新建一个订单(创建保单或修改转账时间)
     *
     * @param order
     */
    void addOrder(InsuranceOrder order);

    /**
     * 修改订单
     *
     * @param order
     */
    public void updateOrder(InsuranceOrder order);

    /**
     * 查询需要创建保单的记录
     *
     * @return
     */
    public List<InsuranceOrder> queryNeedCreateBQList();

    /**
     * 查询需要提交保单结单的记录
     *
     * @return
     */
    public List<InsuranceOrder> queryNeedModifyTransferTimeList();
}
