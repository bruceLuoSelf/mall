package com.wzitech.gamegold.shorder.business.impl;

import java.math.BigDecimal;

/**
 * Created by 340082 on 2018/6/12.
 */
public interface ISyncRepositoryManager {

     void syncAddRepository(Long gameAccountId, BigDecimal price);

     void modifyRepositoryGoldCount(Long repositoryId,Long count);


     void modifyRepositoryGoldCountByInfo(String loginAccount, String gameAccount, String gameRole, String gameName, String regionName, String serverName, String gameRace, Long count, Long stockCount);

     void modifyGameAccountCountByInfo(String gameAccount, String loginAccount, String gameName, String region, String server, String gameRace, String sellerGameRole, Long goldCount);

     void modifyGameAccountStockCountByInfo(int stockType, String orderId, Integer inComeType, String gameAccount, String loginAccount, String gameName, String region, String server, String gameRace, String sellerGameRole, Long modifyCount, Long stockCount);

     void modifyRepositoryGoldCount(Long repositoryId, Long count, Long stockCount);

     void modifyGameAccountGoldCount(Long gameAccountId, Long count);

     void updateStockCountByRepositoryId(Long repositoryId, Long count);

     void inventoryStockByGameAccountId(Long gameAccountId, Long count, Long stockCount);

     void inventoryStockByRepositoryId(Long repositoryId, Long count, Long stockCount);

     void deleteRepositorysSyncShgameAccountIsSaleFalse(String gameAccount, String loginAccount, String gameName, String region, String server, String gameRace, String sellerGameRole);
}
