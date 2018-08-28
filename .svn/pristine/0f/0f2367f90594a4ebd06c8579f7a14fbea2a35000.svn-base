package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.MyBatisPostfixConstants;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.dao.IServiceEvaluateDao;
import com.wzitech.gamegold.order.entity.ServiceEvaluate;
import com.wzitech.gamegold.order.entity.ServiceEvaluateStatistics;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客服评价记录DAO实现类
 * @author yemq
 */
@Repository
public class ServiceEvaluateDaoImpl extends AbstractMyBatisDAO<ServiceEvaluate, Long> implements IServiceEvaluateDao {
    /**
     * 根据订单号查询所有的评价记录
     *
     * @param orderId
     * @return
     */
    @Override
    public List<ServiceEvaluate> queryByOrderId(String orderId) {
        return this.getSqlSession().selectList(this.getMapperNamespace() + ".queryByOrderId", orderId);
    }

    /**
     * 根据订单号删除评价记录
     *
     * @param orderId
     */
    @Override
    public void removeByOrderId(String orderId) {
        this.getSqlSession().delete(this.getMapperNamespace() + ".removeByOrderId", orderId);
    }

    /**
     * 分页统计评价数据
     * @param params
     * @param pageSize
     * @param startIndex
     * @param orderBy
     * @param isAsc
     * @return
     */
    @Override
    public GenericPage<ServiceEvaluateStatistics> statistics(Map<String, Object> params, int pageSize, int startIndex, String orderBy, boolean isAsc) {
        // 检查参数是否为null或者元素长度为0
        // 如果是抛出异常
        Validate.notBlank(orderBy);

        //检查分页参数
        if(pageSize < 1){
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if(startIndex < 0){
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        if(null == params){
            params = new HashMap<String, Object>();
        }
        params.put("ORDERBY", orderBy);
        if(isAsc){
            params.put("ORDER", "ASC");
        }else{
            params.put("ORDER", "DESC");
        }

        int totalSize = (Integer) this.getSqlSession().selectOne(this.mapperNamespace + ".countByStatistics", params);

        // 如果数据Count为0，则直接返回
        if(totalSize == 0){
            return new GenericPage<ServiceEvaluateStatistics>(startIndex, totalSize, pageSize, null);
        }

        List<ServiceEvaluateStatistics> pagedData = this.getSqlSession().selectList(this.mapperNamespace + ".statistics",
                params, new RowBounds(startIndex, pageSize));

        return new GenericPage<ServiceEvaluateStatistics>(startIndex, totalSize, pageSize, pagedData);
    }
}
