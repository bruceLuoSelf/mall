package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.goods.dao.IRepositoryConfigRedisDAO;
import com.wzitech.gamegold.goods.entity.RepositoryConfine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by jhlcitadmin on 2017/3/18.
 */
@Repository
public class RepositoryConfigRedisDAOImpl extends AbstractRedisDAO<RepositoryConfine> implements IRepositoryConfigRedisDAO {


    private static String SELLER_KEY = "gamegold:repository:repositoryConfig:";

    private static final JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }
    //保存库存限制配置信息
    public void saveRepositoryConfig(RepositoryConfine repositoryConfine){
        if (repositoryConfine != null) {
            String json=jsonMapper.toJson(repositoryConfine);
            valueOps.set(SELLER_KEY+repositoryConfine.getGameName()+":"+repositoryConfine.getGoodsTypeName(),json);
        }
    }
    public RepositoryConfine selectRepositoryByGameName(String gameName,String goodsTypeName){
        String eoJson=valueOps.get(SELLER_KEY+gameName+":"+goodsTypeName);
        RepositoryConfine repositoryConfine =jsonMapper.fromJson(eoJson,RepositoryConfine.class);
        return repositoryConfine;
    }

    public void  deleteRepositoryConfig(RepositoryConfine repositoryConfine) {
         template.delete(SELLER_KEY+repositoryConfine.getGameName()+":"+repositoryConfine.getGoodsTypeName());
    }

}
