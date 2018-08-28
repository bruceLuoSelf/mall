package com.wzitech.gamegold.shorder.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.HxAccount;

/**
 * Created by Administrator on 2017/2/19.
 */
public interface IHxAccountDao extends IMyBatisBaseDAO<HxAccount,Long> {
    //查询出货商环信账号信息
    public HxAccount selectHxAccountByAccount(String account);
    //向表中插入数据根据id cha
    public void insertInto(HxAccount hxAccount);

    public void insertAccount(HxAccount hxAccount);
}
