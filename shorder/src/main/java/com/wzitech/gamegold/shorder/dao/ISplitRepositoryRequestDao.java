package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.SplitRepositoryRequest;

import java.util.List;
import java.util.Map;

/**
 * 分仓请求DAO
 */
public interface ISplitRepositoryRequestDao extends IMyBatisBaseDAO<SplitRepositoryRequest, Long> {
    /**
     * 查询等待推送的订单
     * @return
     */
    List<SplitRepositoryRequest> queryWaitForPushOrders();

    /**
     * 根据分仓订单号查询
     * @param orderId
     * @param isLocked
     * @return
     */
    SplitRepositoryRequest selectByOrderId(String orderId, boolean isLocked);

    int countByAccountList(Map<String,Object> map);

}
