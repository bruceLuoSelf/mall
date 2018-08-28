package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;

import java.util.List;
import java.util.Map;

/**
 * 收货商游戏配置管理
 */
public interface IPurchaseGameManager {

    /**
     * 通过总表查询交易方式并置入用户个人游戏配置的查询结果中
     */
    List<PurchaseGame> getPublicTradeTypeForUpdateData(String loginAccount);

    List<PurchaseGame> getGameTradeConfig(String loginAccount);

    /**
     * 更新出货商游戏配置
     */
    void addPurchaseGame(PurchaseGame purchaseGame);

    /**
     * 更新出货商游戏配置
     */
    void updatePurchaseGame(Map<String, String> map);

    /**
     * 分页查询收货商游戏配置
     */
    GenericPage<PurchaseGame> queryListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc);

    /**
     * 删除出货商游戏配置
     */
    void deletePurchaseGame(Long id);

    List selectPurchaseGameByGameNameAndGoodsTypeName(PurchaseGame purchaseGame);


    //同步问题，同步收获游戏属性配置表
    void update(ShGameConfig shGameConfig);

    void addTradeLogoData();

    PurchaseGame selectByPurchaseAccount(String purchaseAccount, String gameName, String goodsTypeName);
}
