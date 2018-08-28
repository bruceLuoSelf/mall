package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IShGameTradeDao;
import com.wzitech.gamegold.shorder.entity.ShGameTrade;
import com.wzitech.gamegold.shorder.entity.Trade;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by ljn on 2018/3/13.
 */
@Repository
public class ShGameTradeDaoImpl extends AbstractMyBatisDAO<ShGameTrade,Long> implements IShGameTradeDao{

    @Override
    public List<Integer> getShModeByGame(Long gameTableId) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".getShModeByGame",gameTableId);
    }

    @Override
    public List<Trade> getTradeByGameAndShMode(Map<String, Object> map) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".getTradeByGameAndShMode",map);
    }
}
