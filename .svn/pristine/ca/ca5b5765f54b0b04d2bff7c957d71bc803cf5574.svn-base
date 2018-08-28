package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.ITradeRedisDao;
import com.wzitech.gamegold.shorder.entity.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/4.
 */
@Repository
public class TradeRedisDaoImpl extends AbstractRedisDAO<Trade> implements ITradeRedisDao{

   private static String TRADE_KEY="gamegold:shorder:trade:";

    @Autowired
    @Qualifier("userRedisTemplate")

    public void setTemplate(StringRedisTemplate template){
        super.template=template;
    }

    private static final JsonMapper jsonMapper=JsonMapper.nonEmptyMapper();

    /**
     * 保存
     * @param trade
     */
    @Override
    public void save(Trade trade) {
        if(trade !=null){
            if(trade.getEnabled()==true){
                BoundHashOperations<String, String, String> bhOps = template.boundHashOps(TRADE_KEY+ trade.getName());
                String json=jsonMapper.toJson(trade);
                bhOps.put(trade.getName().toString(),json);
            }else{
                delete(trade);
            }
        }
    }

    /**
     * 删除
     * @param trade
     * @return
     */
    @Override
    public int delete(Trade trade) {
        if(trade!=null){
            BoundHashOperations<String, String, String> bhOps = template.boundHashOps(TRADE_KEY+trade.getName());
            if(bhOps!=null){
                bhOps.delete(trade.getName().toString());
            }
        }
        return 0;
    }

    /**
     * 查询
     * @param name
     * @return
     */
    @Override
    public List<Trade> selectById(String name) {
        BoundHashOperations<String, String, String> bhOps = template.boundHashOps(TRADE_KEY+name);
        List<Trade> list=new ArrayList<Trade>();
        if(bhOps!=null && bhOps.size()>0){
            for(String jsonStr:bhOps.values()){
                Trade trade=jsonMapper.fromJson(jsonStr,Trade.class);
                list.add(trade);
            }
        }
        return list;
    }
}
