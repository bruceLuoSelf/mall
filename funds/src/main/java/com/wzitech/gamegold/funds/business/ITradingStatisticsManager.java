package com.wzitech.gamegold.funds.business;

import com.wzitech.gamegold.funds.entity.TradingStatisticsEO;

import java.math.BigDecimal;
import java.util.Map;


/**
 * 资金管理
 * @author ztjie
 *
 */
public interface ITradingStatisticsManager {
	
	/**
	 * 
	 * <p>得到交易统计信息</p> 
	 * @author ztjie
	 * @date 2013-11-29 下午1:07:23
	 * @return
	 * @see
	 */
	public TradingStatisticsEO queryTradingStatistics();

    /**
     * 统计佣金和差价
     * @param queryMap
     * @return
     */
    Map<String, BigDecimal> countCommissionAndPriceDiff(Map<String, Object> queryMap);

    /**
     * 查询订单总笔数和金额
     * @param queryMap
     * @return
     */
    Map<String, Object> queryOrderTotalCountAndAmount(Map<String, Object> queryMap);

    /**
     *查询红包和店铺券使用笔数和金额汇总
     * @param queryMap
     * @return
     */
    Map<String, Object> queryYhqCountAndAmount(Map<String, Object> queryMap);
}
