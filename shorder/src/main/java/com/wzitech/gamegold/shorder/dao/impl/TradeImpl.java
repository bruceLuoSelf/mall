package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.ITradeDao;
import com.wzitech.gamegold.shorder.entity.Trade;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
@Repository
public class TradeImpl extends AbstractMyBatisDAO<Trade,Long> implements ITradeDao {

    @Override
    public List<Trade> selectTradeByGameGoodsTypeAndShMode(Map<String, Object> map) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectTradeByGameGoodsTypeAndShMode",map);
    }
}
