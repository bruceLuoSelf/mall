package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;

import java.util.List;
import java.util.Map;

/**
 * 收货采购单
 * Created by 335854 on 2016/3/29.
 */
public interface IPurchaseOrderDBDAO extends IMyBatisBaseDAO<PurchaseOrder, Long> {
    /**
     * 查找对应条件的采购单信息
     *
     * @param paramMap
     * @return
     */
    List<PurchaseOrder> queryPurchaseOrder(Map<String, Object> paramMap);

    /**
     * 通过统计账号角色表，更新采购单表中的采购数量
     *
     * @param paramMap
     */
    void updatePurchaseOrderCount(Map<String, Object> paramMap);

    /**
     * 根据采购单id和采购方的5173账号查找采购单信息
     *
     * @param paramMap
     * @return
     */
    PurchaseOrder selectByIdAndBuyerAccount(Map<String, Object> paramMap);

    /**
     * 分页查找采购单列表用以前台列表页展示
     *
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    GenericPage<PurchaseOrder> selectOrderList(Map<String, Object> paramMap, List<SortField> sortFields,
                                               int start, int pageSize);

    int countOrder(Map<String, Object> queryParam);

    /**
     * 获取采购商数据，并锁定
     *
     * @param id
     * @return
     */
    PurchaseOrder selectByIdForUpdate(Long id);

    /**
     * 更新采购单表中的采购数量
     *
     * @param paramMap
     */
    void updateManualPurchaseOrderCount(Map<String, Object> paramMap);

    /**
     * 全部上架或者下架
     *
     * @param paramMap
     * @return
     */
    void onlineAll(Map<String, Object> paramMap);

    /**
     * 根据id批量删除采购单
     *
     * @param list
     */
    void deletePurchaseOrders(List<Long> list);


//    /**
//     * 查用户信息
//     */
//    List<PurchaseOrder> selectByLoginAccount(String loginAccount,Boolean isRobotDown);
//

    int updateLoginAccount(String loginAccount);

    /**
     * 更新采购单表中的采购数量
     *
     * @param id
     * @param count
     */
    void updatePurchaseOrderCountById(Long id, Long count);
}
