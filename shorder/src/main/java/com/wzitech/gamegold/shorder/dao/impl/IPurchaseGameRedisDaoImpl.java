package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IPurchaseGameRedisDao;
import com.wzitech.gamegold.shorder.entity.PurchaseGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by jhlcitadmin on 2017/2/16.
 */
public class IPurchaseGameRedisDaoImpl extends AbstractRedisDAO<PurchaseGame> implements IPurchaseGameRedisDao{

    private static String PURCHASE_GAME_KEY="gamegold:shorder:purchasegame:";
    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    @Override
    public void save(PurchaseGame purchaseGame) {

    }

    @Override
    public int delete(PurchaseGame purchaseGame) {
        return 0;
    }
}
