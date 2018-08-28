package com.wzitech.gamegold.order.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.order.dao.ICurrencyConfigDBDAO;
import com.wzitech.gamegold.order.entity.CurrencyConfigEO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 340032 on 2018/3/15.
 */
@Repository
public class CurrencyConfigDBDAOImpl extends AbstractMyBatisDAO<CurrencyConfigEO,Long> implements ICurrencyConfigDBDAO{
    @Override
    public void removeByid(Long id) {
         this.getSqlSession().selectOne(this.getMapperNamespace() + ".removeByOrderId", id);
    }

    @Override
    public List<CurrencyConfigEO> queryCurrencyConfig(String gameName,String goodsType) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("gameName",gameName);
        params.put("goodsType",goodsType);
        return this.getSqlSession().selectList(getMapperNamespace() + ".queryCurrencyConfig", params);
    }

    @Override
    public CurrencyConfigEO selectConfig(Long id) {
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectConfig",id);
    }

    @Override
    public CurrencyConfigEO selectCurrency(CurrencyConfigEO currencyConfigEO) {
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("gameName",currencyConfigEO.getGameName());
        map.put("goodsType",currencyConfigEO.getGoodsType());
        map.put("field",currencyConfigEO.getField());
        return this.getSqlSession().selectOne(getMapperNamespace() + ".selectCurrency",map);
    }


}
