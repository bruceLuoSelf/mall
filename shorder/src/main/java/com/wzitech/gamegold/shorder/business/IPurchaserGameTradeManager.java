package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.PurchaseOrder;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;

import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/4/10.
 */
public interface IPurchaserGameTradeManager  {

    void addPurchaserGameTrade(PurchaserGameTrade purchaserGameTrade);

    void deletePurchaserGameTrade(Long id);

    void batchInsert(List<PurchaserGameTrade> list);

    void batchDelete(List<Long> ids);

    void deleteByPurchaserId(Long id);

    List<PurchaserGameTrade> selectByPurchaserId(Long id);

    void deleteByGameTrade(Map<String,Object> map);

    boolean setParameter(PurchaseOrder purchase);

}
