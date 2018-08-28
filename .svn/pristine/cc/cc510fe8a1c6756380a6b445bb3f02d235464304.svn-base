package com.wzitech.gamegold.shorder.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.shorder.business.IHxAccountManager;
import com.wzitech.gamegold.shorder.dao.IHxAccountDao;
import com.wzitech.gamegold.shorder.entity.HxAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/2/19.
 */
@Component
public class HxAccountManagerImpl extends AbstractBusinessObject implements IHxAccountManager{

    @Autowired
    private IHxAccountDao hxAccountDao;

    //查询出货商环信账号信息
    public HxAccount selectHxAccountByAccount(String account){
        return hxAccountDao.selectHxAccountByAccount(account);
    }

    public void insert(HxAccount hxAccount){
        hxAccountDao.insertAccount(hxAccount);
    }

    public void insertInto(HxAccount hxAccount){
        hxAccountDao.insertInto(hxAccount);
    };
}
