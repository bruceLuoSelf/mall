package com.wzitech.gamegold.goods.dao.impl;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.gamegold.goods.dao.IReferencePriceDao;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * Created by jhlcitadmin on 2017/3/17.
 */
@Repository
public class ReferencePriceDaoImpl extends AbstractMyBatisDAO<ReferencePrice, Long> implements IReferencePriceDao {

    @Override
    public ReferencePrice selectByGameNameAndRegionNameAndServerNameAndRaceName(ReferencePrice referencePrice) {

        HashMap<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("gameName", referencePrice.getGameName());

        paramMap.put("regionName", referencePrice.getRegionName());

//        if(referencePrice.getRaceName()!=null)
        paramMap.put("raceName", referencePrice.getRaceName());

        paramMap.put("serverName", referencePrice.getServerName());

        paramMap.put("goodsTypeName",referencePrice.getGoodsTypeName());

        return this.getSqlSession().selectOne(this.getMapperNamespace() + ".selectByMap", paramMap);
    }
}
