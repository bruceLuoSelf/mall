package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.SplitRepositorySubRequest;

import java.util.List;
import java.util.Map;

/**
 * @author ljn
 * @date 2018/6/22.
 */
public interface ISplitRepositorySubRequestManager {

    /**
     * 根据主订单号查询子订单列表
     * @param orderId
     * @return
     */
    List<SplitRepositorySubRequest> getSubOrderList(String orderId);



    /**
     * 分页查找分仓订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<SplitRepositorySubRequest> queryListInPage(Map<String, Object> map, int pageSize, int start, String orderBy, boolean isAsc);

}
