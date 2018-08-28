package com.wzitech.gamegold.goods.business;

import com.wzitech.gamegold.goods.entity.FirmsAccount;

/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/7  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */
public interface ICheckRepositoryManageRedisManager {
    /**
     * 保存数据到redis
     * @param uuid
     * @param firmsSecret
     * @param loginAccount
     * @param uid
     */
    Integer addFirmAndLoginAccountAndUid(String uuid,String firmsSecret,String loginAccount,String uid);

    /**
     * 新增方法查詢redis數據
     * @param redisKey
     * @return
     */
    FirmsAccount getRedisValue(String redisKey);

    String checkIn(String firms,String copyRight,String loginAccount);

    Boolean checkOut(String loginAccount,String token);
}
