package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IOrderLogRedisDao;
import com.wzitech.gamegold.shorder.entity.OrderLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * sunyang
 * Created by Administrator on 2017/2/17.
 */
@Repository
public class OrderLogRedisDaoImpl extends AbstractRedisDAO<OrderLog> implements IOrderLogRedisDao {

    private static String SHGAMECONFIG_KEY = "gamegold:shorder:OrderLog:";

    @Autowired
    @Qualifier("userRedisTemplate")
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Override
    public void save(OrderLog orderLog, String orderId, String userAccount) {
//        if (orderLog != null) {
//            BoundHashOperations<String, String, String> bhOps = template.boundHashOps(SHGAMECONFIG_KEY + orderId + ":" + userAccount);
//            String json = jsonMapper.toJson(orderLog);
//            bhOps.put(orderId +":"+ userAccount, json);
//        }
        String qqOnLineJson=jsonMapper.toJson(orderLog);
        zSetOps.add(SHGAMECONFIG_KEY + orderId + ":" + userAccount,qqOnLineJson,System.currentTimeMillis());

//        String qqOnLineJson=jsonMapper.toJson(orderLog);
//        setOps.add(SHGAMECONFIG_KEY + orderId + ":" + userAccount,qqOnLineJson);
    }

    @Override
    public List<OrderLog> getByOrderLog(String key){
//        BoundHashOperations<String, String, String> bhOps = template.boundHashOps(SHGAMECONFIG_KEY+key);
//        List<OrderLog> list = new ArrayList<OrderLog>();
//        if(bhOps!=null && bhOps.size()>0){
//            for(String jsonStr:bhOps.values()){
//                OrderLog config = jsonMapper.fromJson(jsonStr, OrderLog.class);
//                list.add(config);
//            }
//        }
//        return list;

//        Set<String> values =setOps.members(key);
//        List<OrderLog> list=new ArrayList<OrderLog>();
//        for (String articleJson:values){
//            OrderLog eo= jsonMapper.fromJson(articleJson,OrderLog.class);
//            list.add(eo);
//        }
//        return list;

        Set<String> values= zSetOps.range(key,0,-1);
        List<OrderLog> articleList=new ArrayList<OrderLog>();
        for(String articleJson:values){
            OrderLog eo = jsonMapper.fromJson(articleJson,OrderLog.class);
            articleList.add(eo);
        }
        return  articleList;
    }

    @Override
    public void deleteChatRecord(String orderId,String userAccount,Object o) {
        zSetOps.remove("gamegold:shorder:OrderLog:"+orderId + ":" + userAccount,jsonMapper.toJson(o));
    }


}
