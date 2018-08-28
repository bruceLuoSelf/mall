package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.GameAccount;

import java.util.Map;

/**
 * @author ljn
 * @date 2018/6/13.
 */
public interface IRepositoryInfoManager {

    /**
     * 查询销售角色中是否存在此角色
     * @param account
     * @return
     */
    boolean countByGameAccount(GameAccount account);

    void decreaseSellableCount(Map<String,Object> queryMap);

//    /**
//     * 根据条件查询二级密码
//     * @param queryMap
//     * @return
//     */
//    String selectSecondPassWord(Map<String,Object> queryMap);
}
