package com.wzitech.gamegold.common.utils;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.dto.orderPushVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by chengXY on 2017/11/1.
 */
@Component
public class RedisDaoUtil extends AbstractRedisDAO {
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
    public void saveOrder(String pushVo) {
        //String pushEntity = jsonMapper.toJson(pushVo);
        logger.info("推送失败报错redis：{}",pushVo);
//        BoundHashOperations<String, String, String> orderOpe = template
//                .boundHashOps(RedisKeyHelper.saveOrderList(pushEntity));
//        orderOpe.put(RedisKeyHelper.saveOrderList(""),pushEntity);
        zSetOps.add(RedisKeyHelper.saveOrderList(),pushVo,1);

    }
}
