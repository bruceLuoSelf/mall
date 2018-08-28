package com.wzitech.gamegold.shorder.business;

import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.shorder.entity.FundStatistics;

import java.util.Map;

/**资金统计
 * Created by Administrator on 2016/12/16.
 */
public interface IFundStatisticsManager {
    /**
     * 分页查询
     * @param map
     * @param start
     * @param pageSize
     * @param orderBy
     * @param isAsc
     * @return
     */
    GenericPage<FundStatistics> queryListInPage(Map<String,Object> map, int start, int pageSize, String orderBy, boolean
            isAsc);


    /**
     * 统计每日数据
     */
    void statistics();
}
