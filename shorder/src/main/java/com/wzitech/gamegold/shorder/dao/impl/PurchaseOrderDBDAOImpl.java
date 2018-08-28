package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.shorder.dao.IPurchaseOrderDBDAO;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserData;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货采购单
 * Created by 335854 on 2016/3/29.
 */
@Repository
public class PurchaseOrderDBDAOImpl extends AbstractMyBatisDAO<PurchaseOrder, Long> implements IPurchaseOrderDBDAO {
    /*
   查找对应条件的采购单信息
    */
    @Override
    public List<PurchaseOrder> queryPurchaseOrder(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryPurchaseOrder", paramMap);
    }

    /**
     * 通过统计账号角色表，更新采购单表中的采购数量
     *
     * @param paramMap
     */
    @Override
    public void updatePurchaseOrderCount(Map<String, Object> paramMap) {
        this.getSqlSession().update(getMapperNamespace() + ".updatePurchaseOrderCount", paramMap);
    }

    /**
     * 根据采购单id和采购方的5173账号查找采购单信息
     *
     * @param paramMap
     * @return
     */
    @Override
    public PurchaseOrder selectByIdAndBuyerAccount(Map<String, Object> paramMap) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectByIdAndBuyerAccount", paramMap);
    }

    /**
     * 分页查找采购单列表用以前台列表页展示
     *
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public GenericPage<PurchaseOrder> selectOrderList(Map<String, Object> paramMap, List<SortField> sortFields, int start, int pageSize) {
        if (null == paramMap) {
            paramMap = new HashMap<String, Object>();
        }

        //检查分页参数
        if (pageSize < 1) {
            throw new IllegalArgumentException("分页pageSize参数必须大于1");
        }

        if (start < 0) {
            throw new IllegalArgumentException("分页startIndex参数必须大于0");
        }

        String orderByStr = "";
        if (sortFields != null) {
            for (int i = 0, j = sortFields.size(); i < j; i++) {
                SortField field = sortFields.get(i);
                orderByStr += "\"" + field.getField() + "\" " + field.getSort();
                if (i != (j - 1)) {
                    orderByStr += ",";
                }
            }
        }

        paramMap.put("orderBy", orderByStr);

        int totalSize = countOrder(paramMap);

        // 如果数据Count为0，则直接返回
        if (totalSize == 0) {
            return new GenericPage<PurchaseOrder>(start, totalSize, pageSize, null);
        }

        List<PurchaseOrder> pagedData = this.getSqlSession().selectList(this.mapperNamespace + ".selectOrderList",
                paramMap, new RowBounds(start, pageSize));

        return new GenericPage<PurchaseOrder>(start, totalSize, pageSize, pagedData);
    }

    @Override
    public int countOrder(Map<String, Object> queryParam) {
        return (Integer) this.getSqlSession().selectOne(this.mapperNamespace + ".countOrderByMap", queryParam);
    }

    /**
     * 获取采购商数据，并锁定
     *
     * @param id
     * @return
     */
    @Override
    public PurchaseOrder selectByIdForUpdate(Long id) {
        return getSqlSession().selectOne(mapperNamespace + ".selectByIdForUpdate", id);
    }

    /**
     * 获取采购商数据，并锁定
     *
     * @param id
     * @return
     */
    @Override
    public PurchaseOrder selectById(Long id) {
        return getSqlSession().selectOne(mapperNamespace + ".selectById", id);
    }

    /**
     * 更新采购单表中的采购数量
     *
     * @param paramMap
     */
    @Override
    public void updateManualPurchaseOrderCount(Map<String, Object> paramMap) {
        this.getSqlSession().update(getMapperNamespace() + ".updateManualPurchaseOrderCount", paramMap);
    }

    /**
     * 全部上架或者下架
     *
     * @param paramMap
     * @return
     */
    @Override
    public void onlineAll(Map<String, Object> paramMap) {
        this.getSqlSession().update(getMapperNamespace() + ".onlineAll", paramMap);
    }

    /**
     * 根据id批量删除采购单
     *
     * @param list
     */
    @Override
    public void deletePurchaseOrders(List<Long> list) {
        this.getSqlSession().delete(getMapperNamespace() + ".batchDeleteByIds", list);
    }

    @Override
    public int updateLoginAccount(String loginAccount) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerAccount", loginAccount);
        return this.getSqlSession().update(getMapperNamespace() + ".selectByLoginAccount", map);
    }


//    @Override
//    public List<PurchaseOrder> selectByLoginAccount(String loginAccount,Boolean isRobotDown) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("buyerAccount", loginAccount);
//        map.put("isRobotDown",isRobotDown);
//       return this.getSqlSession().selectList(getMapperNamespace() + ".selectByLoginAccount", map);
//    }


    /**
     * 更新采购单表中的采购数量
     *
     * @param id
     * @param count
     */
    @Override
    public void updatePurchaseOrderCountById(Long id, Long count) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", id);
        paramMap.put("count", count);
        this.getSqlSession().update(getMapperNamespace() + ".updatePurchaseOrderCountById", paramMap);
    }
}
