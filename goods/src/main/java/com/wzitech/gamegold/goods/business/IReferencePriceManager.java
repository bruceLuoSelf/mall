package com.wzitech.gamegold.goods.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.goods.entity.ReferencePrice;

import java.util.Map;

/**
 * Created by jhlcitadmin on 2017/3/21.
 */
public interface IReferencePriceManager {
    //查询全部
    public GenericPage<ReferencePrice> queryReferencePriceList(Map<String, Object> queryMap, int limit, int start,
                                                               String sortBy, boolean isAsc);
}
