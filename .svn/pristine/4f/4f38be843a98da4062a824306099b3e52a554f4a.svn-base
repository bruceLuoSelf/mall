package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IPurchaseGameDao;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收货商游戏配置Dao实现类
 */
@Repository
public class PurchaseGameDaoImpl extends AbstractMyBatisDAO<PurchaseGame, Long> implements IPurchaseGameDao {

    @Override
    public List<PurchaseGame> queryPurchaseGameList(Map<String, String> map) {
        return null;
    }

    /**
     * 进行Trade的模糊查询
     */
    @Override
    public List<PurchaseGame> selectByLikeTrade(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectByLikeTrade", paramMap);
    }

    /**
     * 进行GameCategoryConfigId的模糊查询
     */
    @Override
    public List<PurchaseGame> selectByLikeGameCategoryConfig(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectByLikeGameCategoryConfig", paramMap);
    }

    //    public void updatePurchaseGameByGameNameAndGoodsTypeName(PurchaseGame purchaseGame){
//        this.getSqlSession().update(getMapperNamespace() + ".updateByGameNameAndGoodsTypeName",purchaseGame);
//    }
    @Override
    public void updatePurchaseGameByEnrobot(PurchaseGame purchaseGame) {
        this.getSqlSession().update(getMapperNamespace() + ".updateByEnrobot", purchaseGame);
    }

    @Override
    public List selectPurchaseGameByGameNameAndGoodsTypeName(PurchaseGame purchaseGame) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectPurchaseGameByGameNameAndGoodsTypeName", purchaseGame);
    }

    @Override
    public PurchaseGame selectByPurchaseAccount(String purchaseAccount,String gameName,String goodsTypeName) {
        Map<String, Object> paramMap=new HashMap<String, Object>();
        paramMap.put("purchaseAccount",purchaseAccount);
        paramMap.put("gameName",gameName);
        paramMap.put("goodsTypeName",goodsTypeName);
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectByPurchaseAccount", paramMap);
    }

}
