package com.wzitech.gamegold.repository.business;

import com.wzitech.gamegold.repository.entity.RepositoryInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 安心买栏目2、3库存查询
 * @author yemq
 */
public interface ICategoryRepositoryManager {

    /**
     * 查询安心买栏目最便宜库存
     * @param queryMap
     * @param biggestUnitPrice 最大单价,大于这个单价的库存不显示,可以为空,为空不作为判断条件
     * @return
     */
    List<RepositoryInfo> queryCategoryRepository(Map<String, Object> queryMap, BigDecimal biggestUnitPrice);

    /**
     * 对卖家库存单价进行分组，按单价由低到高，库存由多到少排序
     * @param queryParam
     * @param start
     * @param pageSize
     * @return
     */
    List<RepositoryInfo> queryGroupRepository(Map<String, Object> queryParam, int start, int pageSize);

    List<RepositoryInfo> queryMinPriceRepositoryMixedServer(Map<String,Object> queryMap,int start,int pageSize);
}
