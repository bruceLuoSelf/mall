/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AccountManagerImpl
 *	包	名：		com.wzitech.chinabeauty.usermgmt.business.impl
 *	项目名称：	chinabeauty-usermgmt
 *	作	者：		SunChengfei
 *	创建时间：	2013-9-26
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2013-9-26 上午14:04:26
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.funds.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.gamegold.common.enums.OrderState;
import com.wzitech.gamegold.common.utils.DateUtil;
import com.wzitech.gamegold.funds.business.ITradingStatisticsManager;
import com.wzitech.gamegold.funds.dao.ITransactionInfoDBDAO;
import com.wzitech.gamegold.funds.entity.TradingStatisticsEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ztjie
 *
 */
@Component("fundInfoManagerImpl")
public class TradingStatisticsManagerImpl extends AbstractBusinessObject implements ITradingStatisticsManager{
	
	@Autowired
	ITransactionInfoDBDAO transactionInfoDBDAO;

	@Override
	public TradingStatisticsEO queryTradingStatistics() {
		TradingStatisticsEO tradingStatistics = new TradingStatisticsEO();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("orderState", OrderState.Statement.getCode());

		// 总成交
		BigDecimal totalFunds = transactionInfoDBDAO.findIncome(queryMap);
		tradingStatistics.setTotalFunds(totalFunds);
		
		Date now = new Date();

		// 今日总额
		queryMap.put("startTime", DateUtil.beforeDateStartTime(now, 0));
		queryMap.put("endTime", DateUtil.beforeDateLastTime(now, 0));
		BigDecimal todayIncome = transactionInfoDBDAO.findIncome(queryMap);
		tradingStatistics.setTodayIncome(todayIncome);

		// 昨日总额
		queryMap.put("startTime", DateUtil.beforeDateStartTime(now, -1));
		queryMap.put("endTime", DateUtil.beforeDateLastTime(now, -1));
		BigDecimal yesterdayIncome = transactionInfoDBDAO.findIncome(queryMap);
		tradingStatistics.setYesterdayIncome(yesterdayIncome);

		// 本月总额
		queryMap.put("startTime", DateUtil.monthFirstTime(now, 0));
		queryMap.put("endTime", DateUtil.monthLastTime(now, 0));
		BigDecimal thisMonthIncome = transactionInfoDBDAO.findIncome(queryMap);
		tradingStatistics.setThisMonthIncome(thisMonthIncome);

		// 上月总额
		queryMap.put("startTime", DateUtil.monthFirstTime(now, -1));
		queryMap.put("endTime", DateUtil.monthLastTime(now, -1));
		BigDecimal lastMonthIncome = transactionInfoDBDAO.findIncome(queryMap);
		tradingStatistics.setLastMonthIncome(lastMonthIncome);

        /*计算当日佣金和差价*/
        queryMap.put("statementStartTime", DateUtil.beforeDateStartTime(now, 0));
        queryMap.put("statementEndTime", DateUtil.beforeDateLastTime(now, 0));
        Map<String, BigDecimal> commissionAndPriceDiff = countCommissionAndPriceDiff(queryMap);
        if (commissionAndPriceDiff != null) {
            BigDecimal commission = commissionAndPriceDiff.get("commission");
            BigDecimal priceDiff = commissionAndPriceDiff.get("price_diff");
            tradingStatistics.setCommission(commission);
            tradingStatistics.setPriceDiff(priceDiff);
        }

		// 今日独立购买人数
		queryMap.put("startTime", DateUtil.beforeDateStartTime(now, 0));
		queryMap.put("endTime", DateUtil.beforeDateLastTime(now, 0));
		int todayBuyCount = transactionInfoDBDAO.findCount(queryMap);
		tradingStatistics.setTodayBuyCount(todayBuyCount);

		// 昨日独立购买人数
		queryMap.put("startTime", DateUtil.beforeDateStartTime(now, -1));
		queryMap.put("endTime", DateUtil.beforeDateLastTime(now, -1));
		int lastBuyCount = transactionInfoDBDAO.findCount(queryMap);
		tradingStatistics.setLastBuyCount(lastBuyCount);
		return tradingStatistics;
	}

    /**
     * 统计佣金和差价
     *
     * @param queryMap
     * @return
     */
    @Override
    public Map<String, BigDecimal> countCommissionAndPriceDiff(Map<String, Object> queryMap) {
        return transactionInfoDBDAO.countCommissionAndPriceDiff(queryMap);
    }

    /**
     * 查询订单总笔数和金额
     *
     * @param queryMap
     * @return
     */
    @Override
    public Map<String, Object> queryOrderTotalCountAndAmount(Map<String, Object> queryMap) {
        return transactionInfoDBDAO.queryOrderTotalCountAndAmount(queryMap);
    }

	/**
	 * 查询红包和店铺券使用笔数和金额汇总
	 * @param queryMap
	 * @return
	 */
	@Override
	public Map<String, Object> queryYhqCountAndAmount(Map<String, Object> queryMap)
	{
		return transactionInfoDBDAO.queryYhqCountAndAmount(queryMap);
	}
}
