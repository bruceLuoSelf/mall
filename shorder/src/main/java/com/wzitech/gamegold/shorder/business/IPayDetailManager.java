package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.PayDetail;
import com.wzitech.gamegold.shorder.entity.PayOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 付款明细管理
 */
public interface IPayDetailManager {
    /**
     * 创建付款明细
     *
     * @param payOrderId 关联的支付单号
     * @param chOrderId  关联的出货单号
     * @param amount     付款金额
     * @return
     */
    PayDetail create(String payOrderId, String chOrderId, BigDecimal amount);

    /**
     * 分页查询付款信息
     */
    GenericPage<PayDetail> queryListInPage(Map<String,Object> map, int pageSize, int startIndex,
                                                String orderBy, boolean isAsc);

    /**
     * 查询付款明细记录
     * @param paramMap
     * @return
     */
    List<PayDetail> queryByMap(Map<String, Object> paramMap);

    void update(PayDetail payDetail);
}
