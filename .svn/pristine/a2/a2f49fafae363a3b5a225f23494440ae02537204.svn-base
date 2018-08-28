package com.wzitech.gamegold.common.entity;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by 339928 on 2017/11/24.
 */
@Repository
public class ImageRedisDAOImpl extends AbstractRedisDAO implements IImageRedisDAO {

    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    @Override
    public String getImage(String gameId) {
        return (String) valueOps.get(RedisKeyHelper.ImageUrl(gameId));
    }

    @Override
    public void setImageUrl(String gameId, String imageUrl) {
        valueOps.set(RedisKeyHelper.ImageUrl(gameId), imageUrl);
    }
}
