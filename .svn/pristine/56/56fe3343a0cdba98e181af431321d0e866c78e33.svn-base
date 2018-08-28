package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IHxAccountDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.HxAccount;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/2/19.
 */
@Repository
public class HxAccountDaoImpl extends AbstractMyBatisDAO<HxAccount, Long> implements IHxAccountDao{
    //查询出货商环信账号信息
    public HxAccount selectHxAccountByAccount(String account){
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectHxAccountByAccount",account);
    }

    public void insertInto(HxAccount hxAccount){
        this.getSqlSession().insert(getMapperNamespace() + ".insertIntoById",hxAccount);
    }

    public void insertAccount(HxAccount hxAccount){
        this.getSqlSession().insert(getMapperNamespace() + ".insertAccount",hxAccount);
    }

}
