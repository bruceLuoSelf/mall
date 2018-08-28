package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.ISplitRepositoryRequestDao;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分仓请求DAO
 */
@Repository
public class SplitRepositoryRequestDaoImpl extends AbstractMyBatisDAO<SplitRepositoryRequest, Long> implements ISplitRepositoryRequestDao {
    /**
     * 查询等待推送的订单
     *
     * @return
     */
    @Override
    public List<SplitRepositoryRequest> queryWaitForPushOrders() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", SplitRepositoryRequest.S_NOT_PUSH);
        return selectByMap(params);
    }

    /**
     * 根据分仓订单号查询
     *
     * @param orderId
     * @return
     */
    @Override
    public SplitRepositoryRequest selectByOrderId(String orderId, boolean isLocked) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", orderId);
        params.put("isLocked", isLocked);
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByOrderId", params);
    }

    @Override
    public int countByAccountList(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".countByAccountList", map);
    }
}
