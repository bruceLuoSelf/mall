package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;

import java.util.List;
import java.util.Map;

/**
 * 收货商游戏配置Dao
 */
public interface IPurchaseGameDao extends IMyBatisBaseDAO<PurchaseGame, Long> {
    /**
     * 分页查询收货商游戏配置
     */
    List<PurchaseGame> queryPurchaseGameList(Map<String, String> map);

    /**
     * 发现 收货商游戏 配置有trade的字段
     */
    List<PurchaseGame> selectByLikeTrade(Map<String, Object> paramMap);

    /**
     * 发现 收货商游戏 配置有GoodsTypeId的字段
     *
     * @param paramMap
     * @return
     */
    List<PurchaseGame> selectByLikeGameCategoryConfig(Map<String, Object> paramMap);

//    void updatePurchaseGameByGameNameAndGoodsTypeName(PurchaseGame purchaseGame);

    void updatePurchaseGameByEnrobot(PurchaseGame purchaseGame);

    List selectPurchaseGameByGameNameAndGoodsTypeName(PurchaseGame purchaseGame);

    PurchaseGame selectByPurchaseAccount(String purchaseAccount,String gameName,String goodsTypeName);
}
