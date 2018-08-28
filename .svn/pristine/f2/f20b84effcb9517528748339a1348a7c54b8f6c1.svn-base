package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.shorder.dao.IHxAccountRedisDao;
import com.wzitech.gamegold.shorder.entity.HxAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by ${SunYang} on 2017/3/4.
 */
@Repository
public class HxAccountRedisDaoImpl extends AbstractRedisDAO<HxAccount> implements IHxAccountRedisDao {

    private static String SELLER_KEY = "gamegold:shorder:seller:";

    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @Autowired
    @Qualifier("userRedisTemplate")

    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    /**
     * 保存出货商环信账号
     *
     * @param hxAccount
     */
    @Override
    public void saveHxAccount(HxAccount hxAccount) {
        if (hxAccount != null) {
            String json=jsonMapper.toJson(hxAccount);
            valueOps.set(SELLER_KEY+hxAccount.getPurchaseName(),json);
        }
    }

    /**
     * 根据出货商账号查询
     *
     * @param sellername
     * @return
     */
    @Override
    public HxAccount selectBySellerName(String sellername) {
        String eoJson=valueOps.get(SELLER_KEY+sellername);
        HxAccount eo=jsonMapper.fromJson(eoJson,HxAccount.class);
        return eo;
    }
}
