package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IGameAccountCountRedisDao;
import com.wzitech.gamegold.shorder.entity.Trade;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * ZW_C_JB_00004 jiyx
 */
@Repository
public class GameAccountCountRedisDaoImpl extends AbstractRedisDAO<Trade> implements IGameAccountCountRedisDao {

    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
    private static String TRADE_KEY = "gamegold:shorder:gameAccountCount:";

    @Autowired
    @Qualifier("userRedisTemplate")

    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    /**
     * 保存
     *
     * @param
     */
    @Override
    public void save(String orderId, Map gameCountMap) {
        if (orderId != null) {
            BoundValueOperations<String, String> bhOps = template.boundValueOps(TRADE_KEY + orderId);
            String json = jsonMapper.toJson(gameCountMap);
            bhOps.set(json);
        }
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    @Override
    public int delete(String orderId) {
        if (orderId != null) {
            template.delete(TRADE_KEY + orderId);
            return 1;
        }
        return 0;
    }

    /**
     * 查询
     *
     * @param
     * @return
     */
    @Override
    public Map selectByOrderId(String orderId) {
        BoundValueOperations<String, String> bhOps = template.boundValueOps(TRADE_KEY + orderId);
        String json = bhOps.get();
        JSONObject jasonObject = JSONObject.fromObject(json);
        Map map = (Map) jasonObject;
        //同步进行删除操作
        delete(orderId);
        return map;
    }
}
