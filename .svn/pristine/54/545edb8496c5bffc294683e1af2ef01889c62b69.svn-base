package com.wzitech.gamegold.goods.business.impl;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.business.IReferencePriceManager;
import com.wzitech.gamegold.goods.dao.IReferencePriceDao;
import com.wzitech.gamegold.goods.entity.ReferencePrice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/21.
 */
@Component
public class ReferencePriceManagerImpl implements IReferencePriceManager{

    @Autowired
    IReferencePriceDao referencePriceDao;

    public GenericPage<ReferencePrice> queryReferencePriceList(Map<String, Object> queryMap, int limit, int start,
                                                               String sortBy, boolean isAsc){

        if(StringUtils.isEmpty(sortBy)){
            sortBy = "id";
        }
        return referencePriceDao.selectByMap(queryMap,limit,start,sortBy,isAsc);
    }
}
