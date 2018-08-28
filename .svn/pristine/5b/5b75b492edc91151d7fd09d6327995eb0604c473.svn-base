package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.common.insurance.dto.ObjectFactory;
import com.wzitech.gamegold.order.dao.IServiceSortDao;
import com.wzitech.gamegold.order.entity.ServiceSort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客服分单排序
 * @author yemq
 */
@Repository
public class ServiceSortDaoImpl extends AbstractMyBatisDAO<ServiceSort, Long> implements IServiceSortDao {
    /**
     * 根据客服ID查询客服分单排序数据
     *
     * @param serviceId
     * @param lockMode
     * @return
     */
    @Override
    public ServiceSort selectByServiceId(Long serviceId, boolean lockMode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("serviceId", serviceId);
        params.put("lockMode", lockMode);
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByServiceId", params);
    }

    /**
     * 根据待发货订单数由少到多，获取已排序的客服数据
     *
     * @param userType
     * @param gameName
     * @return
     */
    @Override
    public List<ServiceSort> selectSortedServiceList(int userType, String gameName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userType", userType);
        params.put("gameName", gameName);
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectSortedServiceList", params);
    }

    /**
     * 获取下一个该分配的客服ID
     * @param userType
     * @param gameName
     * @return
     */
    @Override
    public Long selectNextServiceId(int userType, String gameName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userType", userType);
        params.put("gameName", gameName);
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectNextServiceId", params);
    }

    /**
     * 客服待发货订单数量+1
     *
     * @param serviceId
     */
    @Override
    public void incOrderCount(Long serviceId) {
        this.getSqlSession().update(getMapperNamespace() + ".incOrderCount", serviceId);
    }

    /**
     * 客服待发货订单数量-1
     *
     * @param serviceId
     */
    @Override
    public void decOrderCount(Long serviceId) {
        this.getSqlSession().update(getMapperNamespace() + ".decOrderCount", serviceId);
    }
}
