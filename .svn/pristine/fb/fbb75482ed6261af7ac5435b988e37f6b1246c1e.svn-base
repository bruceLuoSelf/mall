package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;

import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/4/10.
 */
public interface IPurchaserGameTradeDao extends IMyBatisBaseDAO<PurchaserGameTrade, Long> {

    void deleteByPurchaserId(Long purchaserId);

    List<PurchaserGameTrade> selectByPurchaserId(Long id);

    void deleteByGameTrade(Map<String,Object> map);
}
