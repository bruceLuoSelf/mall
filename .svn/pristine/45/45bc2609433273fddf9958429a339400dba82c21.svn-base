package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.shorder.dao.IShGameConfigDao;
import com.wzitech.gamegold.shorder.entity.ShGameConfig;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/4.
 */
@Repository
public class ShGameConfigDaoImpl extends AbstractMyBatisDAO<ShGameConfig, Long> implements IShGameConfigDao {

    //进行Trade的模糊查询
    @Override
    public List<ShGameConfig> selectByLikeTrade(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectByLikeTrade", paramMap);
    }

    @Override
    public List<ShGameConfig> selectForAllCondition(Map<String, Object> paramMap) {
        return this.getSqlSession().selectList(this.mapperNamespace + ".selectByMapForAll", paramMap);
    }

    public ShGameConfig selectShGame(ShGameConfig s){
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectShGameConfig",s);
    }

    @Override
    public List<ShGameConfig> selectGoodsTypeByGameAndShMode(Map<String, Object> map) {
        return this.getSqlSession().selectList(getMapperNamespace() + ".selectGoodsTypeByGameAndShMode",map);
    }

    @Override
    public BigDecimal getPoundage(Map<String,Object> map) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".getPoundage",map);
    }
}
