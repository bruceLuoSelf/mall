/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		IFundInfoDBDAO
 *	包	名：		com.wzitech.gamegold.funds.dao
 *	项目名称：	gamegold-funds
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-15
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:28:24
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.funds.dao;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.IMyBatisBaseDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.funds.entity.TransactionInfo;
import com.wzitech.gamegold.funds.entity.gameTradingEO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author ztjie
 *
 */
public interface ITransactionInfoDBDAO extends IMyBatisBaseDAO<TransactionInfo, Long>{

	/**
	 * 
	 * <p>查询收益信息</p> 
	 * @author Think
	 * @date 2014-2-25 下午8:56:21
	 * @param queryMap
	 * @return
	 * @see
	 */
	BigDecimal findIncome(Map<String, Object> queryMap);

	/**
	 * 
	 * <p>根据条件查询交易流水,用于导出的数据</p> 
	 * @author Think
	 * @date 2014-3-15 下午2:30:33
	 * @param queryMap
	 * @param orderBy
	 * @param isAsc
	 * @return
	 * @see
	 */
	List<TransactionInfo> selectAllByMap(Map<String, Object> queryMap,
			String orderBy, boolean isAsc);

	/**
	 * 
	 * <p>查询购买人数信息</p> 
	 * @author xueao
	 * @date 2014-7-3 下午18:00:21
	 * @param queryMap
	 * @return
	 * @see
	 */
	int findCount(Map<String, Object> queryMap);

	/**
	 * 
	 * <p>根据条件查询区服交易流水</p> 
	 * @author xueao
	 * @date 2014-3-15 下午2:30:33
	 * @param queryMap
	 * @param pageSize
	 * @param startIndex
	 * @param orderBy
	 * @param isAsc
	 * @return
	 * @see
	 */
	GenericPage<gameTradingEO> selectTradingByGame(Map<String, Object> queryParam, Integer pageSize, Integer startIndex,
		String orderBy, boolean isAsc);
//导出的数据
	List<gameTradingEO> selectTradingByGame1(Map<String, Object> paramMap);

	//得到一个区服的差额
	String selectDifference(Map<String, Object> map);

	//重新计算佣金
	double countCommission(Map<String, Object> map);

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
	 * 查询红包和店铺券使用笔数和金额汇总
	 * @param queryMap
	 * @return
	 */
	Map<String, Object> queryYhqCountAndAmount(Map<String, Object> queryMap);
}
