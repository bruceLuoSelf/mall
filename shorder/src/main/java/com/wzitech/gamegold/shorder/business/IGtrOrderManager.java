package com.wzitech.gamegold.shorder.business;

import com.wzitech.gamegold.shorder.entity.GtrShOrder;
import com.wzitech.gamegold.shorder.entity.RoboutOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by 汪俊杰 on 2016/12/14.
 */
public interface IGtrOrderManager {
    /**
     * 分页查找GTR收货订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
     List<GtrShOrder> queryShOrderListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc);

    /**
     * 分页查找GTR分仓订单
     *
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
     List<GtrShOrder> querySplitOrderListInPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc);

    /**
     * 分页查询出货子订单
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    List<RoboutOrder> queryShOrderListPage(Map<String, Object> map, int start, int pageSize, String orderBy, boolean isAsc);
}
