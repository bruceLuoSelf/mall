package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.mybatis.MyBatisPostfixConstants;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.order.dao.ISubOrderDetailDao;
import com.wzitech.gamegold.order.dto.SubOrderDetailDTO;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 子订单详情查询
 *
 * @author yemq
 */
@Repository
public class SubOrderDetailDaoImpl extends AbstractMyBatisDAO<SubOrderDetailDTO, Long> implements ISubOrderDetailDao {
    /**
     * 查询卖家订单列表
     *
     * @param params
     * @return
     */
    @Override
    public GenericPage<SubOrderDetailDTO> querySellerOrders(Map<String, Object> params, String orderBy, boolean isAsc,
                                                            int pageSize, int start) {
        // 检查参数是否为null或者元素长度为0
        // 如果是抛出异常
        Validate.notBlank(orderBy);

        //检查分页参数
        if(pageSize < 1){
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if(start < 0){
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

        int totalSize = countByMap(params);

        // 如果数据Count为0，则直接返回
        if(totalSize == 0){
            return new GenericPage<SubOrderDetailDTO>(start, totalSize, pageSize, null);
        }

        List<SubOrderDetailDTO> pagedData = getSqlSession().selectList(mapperNamespace + ".selectSellerOrder",
                params, new RowBounds(start, pageSize));

        return new GenericPage<SubOrderDetailDTO>(start, totalSize, pageSize, pagedData);
    }

    /**
     * 查询卖家订单列表，返回少量字段
     *
     * @param params
     * @param orderBy
     * @param isAsc
     * @param pageSize
     * @param start
     * @return
     */
    @Override
    public GenericPage<SubOrderDetailDTO> querySellerSimpleOrders(Map<String, Object> params, String orderBy, boolean isAsc, int pageSize, int start) {
        // 检查参数是否为null或者元素长度为0
        // 如果是抛出异常
        Validate.notBlank(orderBy);

        //检查分页参数
        if(pageSize < 1){
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if(start < 0){
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

        int totalSize = countByMap(params);

        // 如果数据Count为0，则直接返回
        if(totalSize == 0){
            return new GenericPage<SubOrderDetailDTO>(start, totalSize, pageSize, null);
        }

        List<SubOrderDetailDTO> pagedData = getSqlSession().selectList(mapperNamespace + ".selectSellerSimpleOrder",
                params, new RowBounds(start, pageSize));

        return new GenericPage<SubOrderDetailDTO>(start, totalSize, pageSize, pagedData);
    }

    /**
     * 查询卖家的订单详情
     *
     * @param params
     * @return
     */
    @Override
    public SubOrderDetailDTO querySellerOrderDetail(Map<String, Object> params) {
        return getSqlSession().selectOne(getMapperNamespace() + ".selectSellerOrder", params);
    }

    /**
     * 查询一条订单详情
     *
     * @param params
     * @return
     */
    @Override
    public SubOrderDetailDTO querySubOrderDetail(Map<String, Object> params) {
        return getSqlSession().selectOne(getMapperNamespace() + ".selectByMap", params);
    }

}
