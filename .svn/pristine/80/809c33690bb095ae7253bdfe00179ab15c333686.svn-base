package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IStockDao;
import com.wzitech.gamegold.shorder.entity.Stock;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 盘存表dao
 * Created by 340032 on 2018/6/20.
 */
@Repository
public class StockDaoImpl extends AbstractMyBatisDAO<Stock,Long> implements IStockDao {
    @Override
    public Stock queryByOrderIdAnSubId(String orderId, Long subId) {
        Map<String,Object>  queryMap=new HashMap<String, Object>();
        queryMap.put("orderId",orderId);
        queryMap.put("subOrderId",subId);
        return this.getSqlSession().selectOne(getMapperNamespace()+".queryByOrderIdAnSubId",queryMap);
    }
}
