package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.dto.orderPushVo;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.shorder.dao.IOrderPushRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chengXY on 2017/11/1.
 */
@Repository
public class OrderPushRedisDaoImpl extends AbstractRedisDAO implements IOrderPushRedisDao{
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    /**
     * 将推送到mq失败的订单保存到redis
     * */
    @Override
    public void saveOrder(orderPushVo pushVo) {
        String pushEntity = jsonMapper.toJson(pushVo);

//        BoundHashOperations<String, String, String> orderOpe = template
//                .boundHashOps(RedisKeyHelper.saveOrderList(pushEntity));
//        orderOpe.put(RedisKeyHelper.saveOrderList(""),pushEntity);
        zSetOps.add(RedisKeyHelper.saveOrderList(),pushEntity,1);

    }
/**
 * 查找redis里面推送失败的订单
 * */
    @Override
    public List findOrder() {
        List list = new ArrayList();
        Set<String> set = zSetOps.range(RedisKeyHelper.saveOrderList(), 0, zSetOps.size(RedisKeyHelper.saveOrderList()));
        if (set == null || set.size() == 0) {
            return null;
        }
        for (int i = 0;i<set.size();i++) {
            String value = new ArrayList<String>(set).get(i);
            list.add(value);
        }
        return list;
    }

    @Override
    public void removeOrder(int size) {
        zSetOps.removeRange(RedisKeyHelper.saveOrderList(),0,size);
    }


    /**
     * 环信聊天室相关操作
     * */
    @Override
    public void saveHxChatRoomId(String chatRoomId){
        zSetOps.add(RedisKeyHelper.hxChatRoom(),chatRoomId,1);
    }

    @Override
    public List findHxChatRoomId(){
        List list = new ArrayList();
        Set<String> set = zSetOps.range(RedisKeyHelper.hxChatRoom(), 0, zSetOps.size(RedisKeyHelper.hxChatRoom()));
        if (set == null || set.size() == 0) {
            return null;
        }
        for (int i = 0;i<set.size();i++) {
            String value = new ArrayList<String>(set).get(i);
            list.add(value);
        }
        return list;
    }

    @Override
    public void removeHxChatRoomId(int size){
        zSetOps.removeRange(RedisKeyHelper.hxChatRoom(),0,size);
    }
}
