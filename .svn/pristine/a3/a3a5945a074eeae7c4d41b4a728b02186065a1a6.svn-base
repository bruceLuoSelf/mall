package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.shorder.dao.IAutomaticQueuingRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by 340082 on 2017/12/22.
 * job 排队队列
 */
@Repository
public class AutomaticQueuingRedisImpl implements IAutomaticQueuingRedis{
    protected static final Logger logger = LoggerFactory.getLogger(AutomaticQueuingRedisImpl.class);

    public static final String START_STRING = "订单排队生成开始";

    /**
     * 字符类型模板
     */
    @Resource(name="userRedisTemplate")
    private StringRedisTemplate template;

    /**
     * 添加订单号队列
     * @param orderId
     */
    @Override
    public void pushOrderId(String orderId){
        logger.debug("自动配单结束，订单继续排队，订单号:{}", orderId);
        BoundListOperations<String, String> orderIdList = template.boundListOps(RedisKeyHelper.queuingredis());
        orderIdList.leftPush(orderId);
    }

    /**
     * 获取订单号队列
     * @return
     */
    @Override
    public String getLastOrderid(){
        BoundListOperations<String, String> orderIdList = template.boundListOps(RedisKeyHelper.queuingredis());
        return orderIdList.rightPop();
    }

    /**
     * 获取订单号队列
     * @return
     */
    @Override
    public String remOrderid(String orderId){
        BoundListOperations<String, String> orderIdList = template.boundListOps(RedisKeyHelper.queuingredis());
        return orderIdList.remove(0,orderId).toString();
    }


}
