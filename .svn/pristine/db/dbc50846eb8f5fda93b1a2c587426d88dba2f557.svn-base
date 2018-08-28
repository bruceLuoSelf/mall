package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IPurchaserGameTradeDao;
import com.wzitech.gamegold.shorder.entity.PurchaserGameTrade;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/4/10.
 */
@Repository
public class PurchaserGameTradeDaoImpl extends AbstractMyBatisDAO<PurchaserGameTrade, Long> implements IPurchaserGameTradeDao{

    @Override
    public void deleteByPurchaserId(Long purchaserId) {
        this.getSqlSession().delete(this.getMapperNamespace()+".deleteByPurchaserId",purchaserId);
    }

    @Override
    public List<PurchaserGameTrade> selectByPurchaserId(Long purchaserId) {
        return this.getSqlSession().selectList(this.getMapperNamespace() + ".selectByPurchaserId",purchaserId);
    }

    @Override
    public void deleteByGameTrade(Map<String,Object> map) {
        this.getSqlSession().delete(this.getMapperNamespace() + ".deleteByGameTrade",map);
    }
}
