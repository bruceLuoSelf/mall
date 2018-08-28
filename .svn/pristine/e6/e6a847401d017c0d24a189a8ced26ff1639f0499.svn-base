package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IHotRecommendConfigDBDAO;
import com.wzitech.gamegold.goods.entity.HotRecommendConfig;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 前台安心买热卖商品配置
 * @author yemq
 */
@Repository
public class HotRecommendConfigDBDAOImpl extends AbstractMyBatisDAO<HotRecommendConfig, Long>  implements IHotRecommendConfigDBDAO {

    @Override
    public HotRecommendConfig selectByProp(Map<String, Object> map) {
        return this.getSqlSession().selectOne(this.mapperNamespace+".selectByProp",map);
    }
}
