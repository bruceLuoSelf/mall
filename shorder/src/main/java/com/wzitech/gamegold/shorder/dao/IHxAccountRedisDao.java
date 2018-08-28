package com.wzitech.gamegold.shorder.dao;

import com.wzitech.gamegold.shorder.entity.HxAccount;

/**
 * Created by ${SunYang} on 2017/3/4.
 */
public interface IHxAccountRedisDao  {

    /**
     * 保存出货商环信账号
     * @param hxAccount
     */
    public void saveHxAccount(HxAccount hxAccount);

    /**
     * 根据出货商账号查询
     * @param sellername
     * @return
     */
    public HxAccount selectBySellerName(String sellername);

}
