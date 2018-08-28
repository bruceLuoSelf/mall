package com.wzitech.gamegold.goods.dao;


/**
 * Update History
 * Date        Name                Reason for change
 * ----------  ----------------    ----------------------
 * 2017/07/7  wurongfan           ZW_J_JB_00072金币商城对外接口优化
 *
 */
public interface ICheckRespostitoryManageRedisDAO {
    /**
     * 通过从cookie中取到的uuid在redis中查询对应的厂家与登录账号字符串
     *
     */
    Integer saveFirmAndLoginAccount(String uuid,String firmsSecret,String loginAccount,String uid);

    /**
     * get token from redis with loginAccount
     * @param loginAccount 5173 user's loginAccount  it's unique
     * @return limit time
     */
    String saveFirmAndLoginAccount(String loginAccount);

    /**
     * 新增方法獲取redis數據
     * @param redisKey
     * @return
     */
    String getRedisValue(String redisKey);

    /**
     * get token with 5173 loginAccount for 1771
     * @param loginAccount  5173 loginAccount
     * @return
     */
    String getToken(String loginAccount);

}
