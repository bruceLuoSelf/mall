package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.entity.SortField;
import com.wzitech.gamegold.shorder.entity.GameAccount;
import com.wzitech.gamegold.shorder.entity.PurchaseConfig;
import com.wzitech.gamegold.shorder.entity.PurchaseOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 采购收货单
 * Created by 335854 on 2016/3/29.
 */
public interface IPurchaseOrderManager {
    /**
     * 通过上传采购单新增/修改采购单
     *
     * @param gameAccountList
     * @return
     */
    void addPurchaseOrderInUpload(List<GameAccount> gameAccountList, Integer deliveryType) throws Exception;

    /**
     * 查找采购单
     *
     * @param queryMap
     * @param limit
     * @param start
     * @param sortBy
     * @param isAsc
     * @return
     */
    GenericPage<PurchaseOrder> queryPurchaseOrder(Map<String, Object> queryMap, int limit, int start, String sortBy, boolean isAsc);


    /**
     * 批量设置采购单的上下架
     *
     * @param ids
     * @param isOnline
     */
    String setPurchaseOrderOnline(List<Long> ids, Boolean isOnline, String buyerAccount);

    /**
     * 全部上架或者下架
     *
     * @param paramMap
     */
    void onlineAll(Map<String, Object> paramMap);

    /**
     * 发布单上架
     */
    void online(Long id);


    /**
     * 发布单下架
     */
    void offline(Long id);

    /**
     * 自动上架
     */
    void grounding(String account, BigDecimal availableAmount);

    /**
     * 老流程下架操作
     *
     * @param id
     */
    public void oldOffline(Long id);


    /**
     * 修改当前采购单的采购单价和采购量
     *
     * @param id
     * @param price
     * @param count
     * @param gameAccountMap
     */
    Boolean updatePurchaseOrderPriceAndCount(Long id, BigDecimal price, Long count, Map<String, Long> gameAccountMap);


    /**
     * 修改当前采购单的采购单价和采购量和最低采购数量
     *
     * @param id
     * @param price
     * @param count
     */
    Boolean updatePurchaseOrderPriceAndCountAndNum(Long id, BigDecimal price, Long count, Long mainCount);

    /**
     * 批量修改
     *
     * @param orderList
     * @return
     */
    public boolean batchUpdate(List<PurchaseOrder> orderList);

    /**
     * 更新采购单和对应账号角色的采购数量
     *
     * @param buyerAccount
     * @param gameName
     * @param region
     * @param server
     * @param gameRace
     * @param gameAccount
     * @param roleName
     * @param count
     * @param status
     * @param isAdd
     * @return boolean 是否更新成功
     */
    boolean updatePurchaseOrderCount(String buyerAccount, String gameName, String region, String server, String gameRace, String gameAccount, String roleName, Long count, int status, Boolean isAdd, String orderId);


    /**
     * 申诉单不撤数量，仅释放角色状态
     */
    boolean updatePurchaseOrderCountForAppealOrder(String buyerAccount, String gameName, String region, String server, String gameRace,
                                                   String gameAccount, String roleName, int status);

    /**
     * 分页查找采购单列表用以前台列表页展示
     *
     * @param paramMap
     * @param sortFields
     * @param start
     * @param pageSize
     * @return
     */
    GenericPage<PurchaseOrder> selectOrderList(Map<String, Object> paramMap, List<SortField> sortFields, int start, int pageSize);

    /**
     * 根据 采购单id查找 采购单以及采购商信息
     *
     * @param id
     * @return
     */
    PurchaseOrder selectPurchaseOrderAndCgDataById(Long id);

    /**
     * 获取采购商数据，并锁定
     *
     * @param id
     * @return
     */
    PurchaseOrder selectByIdForUpdate(Long id);

    /**
     * 更新采购单采购数量
     *
     * @param buyerAccount
     * @param gameName
     * @param region
     * @param server
     * @param gameRace
     * @param count
     * @return
     */
    boolean updatePurchaseOrderCount(String buyerAccount, String gameName, String region, String server, String gameRace, Long count);

    /**
     * 获取交易信息
     *
     * @param gameName
     * @param goodsTypeId
     * @param deliveryTypeId
     * @return
     */
    PurchaseConfig getPurchaseConfig(String gameName, int goodsTypeId, int deliveryTypeId);

    /**
     * 更新采购单采购数量
     *
     * @param id
     * @param count
     * @return
     */
    boolean updatePurchaseOrderCountById(Long id, Long count);
}
