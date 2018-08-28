package com.wzitech.gamegold.order.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.dto.SubOrderDetailDTO;

import java.util.Map;

/**
 * 子订单详情查询
 * @author yemq
 */
public interface ISubOrderDetailDao extends IMyBatisBaseDAO<SubOrderDetailDTO, Long> {

    /**
     * 查询卖家订单列表
     * @param params
     * @return
     */
    GenericPage<SubOrderDetailDTO> querySellerOrders(Map<String, Object> params, String orderBy, boolean isAsc,
                                                     int pageSize, int start);

    /**
     * 查询卖家订单列表，返回少量字段
     * @param params
     * @param orderBy
     * @param isAsc
     * @param pageSize
     * @param start
     * @return
     */
    GenericPage<SubOrderDetailDTO> querySellerSimpleOrders(Map<String, Object> params, String orderBy, boolean isAsc,
                                                     int pageSize, int start);

    /**
     * 查询卖家的订单详情
     * @param params
     * @return
     */
    SubOrderDetailDTO querySellerOrderDetail(Map<String, Object> params);

    /**
     * 查询一条订单详情
     *
     * @param params
     * @return
     */
    SubOrderDetailDTO querySubOrderDetail(Map<String, Object> params);


}
