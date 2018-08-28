package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.order.entity.InsuranceOrder;

import java.util.List;

/**
 * 保单DAO
 * @author yemq
 */
public interface IInsuranceOrderDao extends IMyBatisBaseDAO<InsuranceOrder, Long> {
    /**
     * 查询需要创建保单的记录
     * @return
     */
    List<InsuranceOrder> queryNeedCreateBQList();

    /**
     * 查询需要提交保单结单的记录
     * @return
     */
    List<InsuranceOrder> queryNeedModifyTransferTimeList();
}
