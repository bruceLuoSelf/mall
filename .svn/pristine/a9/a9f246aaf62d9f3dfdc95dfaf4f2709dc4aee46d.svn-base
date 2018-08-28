package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.order.dao.IInsuranceOrderDao;
import com.wzitech.gamegold.order.entity.InsuranceOrder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保单DAO
 * @author yemq
 */
@Repository
public class InsuranceOrderDaoImpl extends AbstractMyBatisDAO<InsuranceOrder, Long>  implements IInsuranceOrderDao {

    /**
     * 查询需要创建保单的记录
     *
     * @return
     */
    @Override
    public List<InsuranceOrder> queryNeedCreateBQList() {
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("type", InsuranceOrder.TYPE_CREATE_ORDER);
        return this.getSqlSession().selectList(this.getMapperNamespace() + ".queryNeedCreateBQList", queryParam);
    }

    /**
     * 查询需要提交保单结单的记录
     *
     * @return
     */
    @Override
    public List<InsuranceOrder> queryNeedModifyTransferTimeList() {
        Map<String, Object> queryParam = new HashMap<String, Object>();
        queryParam.put("type", InsuranceOrder.TYPE_MODIFY_TRANSFER_TIME);
        queryParam.put("orderState", OrderState.Statement.getCode());
        return this.getSqlSession().selectList(this.getMapperNamespace() + ".queryNeedModifyTransferTimeList", queryParam);
    }
}
