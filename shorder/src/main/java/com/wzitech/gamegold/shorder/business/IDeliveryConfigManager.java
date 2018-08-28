package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.DeliveryConfig;

import java.util.Map;

/**
 * Created by 汪俊杰 on 2018/1/4.
 */
public interface IDeliveryConfigManager {
    /**
     * 根据条件获取分页数据
     *
     * @param paramMap
     * @param pageSize
     * @param start
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<DeliveryConfig> queryPage(Map<String, Object> paramMap, int pageSize, int start, String orderBy, Boolean isAsc);

    /**
     * 根据id删除记录
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 添加记录
     *
     * @param deliveryConfig
     */
    void insert(DeliveryConfig deliveryConfig);

    /**
     * 修改记录
     *
     * @param deliveryConfig
     */
    void update(DeliveryConfig deliveryConfig);

    /**
     * 根据多条件查询当前收货配置
     *
     * @param gameName
     * @param goodsTypeId
     * @param deliveryTypId
     * @param tradeTypeId
     * @return
     */
    DeliveryConfig getDeliveryConfigByCondition(String gameName, Integer goodsTypeId, Integer deliveryTypId, Integer tradeTypeId);
}
