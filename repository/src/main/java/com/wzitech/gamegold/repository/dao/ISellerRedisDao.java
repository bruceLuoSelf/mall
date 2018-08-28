package com.wzitech.gamegold.repository.dao;

/**
 * 卖家redis dao
 * @author yemq
 */
public interface ISellerRedisDao {
    /**
     * 卖家使用下线功能
     * @param account
     * @param uid
     */
    void useOffline(String account, String uid);

    /**
     * 判断卖家是否使用了离线功能,1个小时内使用了离线功能，将返回true
     * @param account
     * @param uid
     * @return
     */
    boolean isUseOffline(String account, String uid);
}
