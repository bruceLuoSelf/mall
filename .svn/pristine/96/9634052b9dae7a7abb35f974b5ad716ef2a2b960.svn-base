/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		FundInfoDBDAOImpl
 *	包	名：		com.wzitech.gamegold.funds.dao.impl
 *	项目名称：	gamegold-funds
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-23
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-23 下午1:29:30
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.funds.dao.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.mybatis.AbstractMyBatisDAO;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.funds.dao.ITransactionInfoDBDAO;
import com.wzitech.gamegold.funds.entity.TransactionInfo;
import com.wzitech.gamegold.funds.entity.gameTradingEO;

/**
 * @author ztjie
 *
 */
@Repository
public class TransactionInfoDBDAOImpl extends AbstractMyBatisDAO<TransactionInfo, Long> implements ITransactionInfoDBDAO{

	@Override
	public BigDecimal findIncome(Map<String, Object> queryMap) {
		return (BigDecimal)this.getSqlSession().selectOne(this.mapperNamespace + ".findIncome", queryMap);
	}
	
	@Override
	public int findCount(Map<String, Object> queryMap) {
		return (Integer)this.getSqlSession().selectOne(this.mapperNamespace + ".findCount", queryMap);
	}

	@Override
	public List<TransactionInfo> selectAllByMap(Map<String, Object> queryMap,
			String orderBy, boolean isAsc) {
		queryMap.put("orderBy", orderBy);
		queryMap.put("isAsc", isAsc);
		List<TransactionInfo> transactionInfos = this.getSqlSession().selectList(getMapperNamespace() + ".selectAllByMap", queryMap);
		return transactionInfos;
	}
	
	@Override
	public GenericPage<gameTradingEO>selectTradingByGame(Map<String,Object>queryParam,Integer pageSize,Integer startIndex,String orderBy,boolean isAsc){
		// 检查参数是否为null或者元素长度为0
		// 如果是抛出异常
		Validate.notBlank(orderBy);
		
		if(startIndex < 0){
			throw new IllegalArgumentException("分页startIndex参数必须大于0");
		}
		
		if(null == queryParam){
			queryParam = new HashMap<String, Object>();
		}
		queryParam.put("ORDERBY", orderBy);
		if(isAsc){
			queryParam.put("ORDER", "ASC");
		}else{
			queryParam.put("ORDER", "DESC");
		}
		
		int totalSize = (Integer)this.getSqlSession().selectOne("com.wzitech.gamegold.funds.entity.gameTradingEO" + ".selectCountByMap", queryParam);
		
		// 如果数据Count为0，则直接返回
		if(totalSize == 0){
			return new GenericPage<gameTradingEO>(startIndex, totalSize, pageSize, null);
		}
		
		if(StringUtils.isEmpty(orderBy)){
			orderBy="TRADING_NUM";
		}
		
		List<gameTradingEO> pagedData = this.getSqlSession().selectList("com.wzitech.gamegold.funds.entity.gameTradingEO" + ".selectByGameProp", 
				queryParam, new RowBounds(startIndex, pageSize));
		for(gameTradingEO gameTradingInfo : pagedData){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("gameName", gameTradingInfo.getGameName());
			map.put("region", gameTradingInfo.getRegion());
			map.put("server", gameTradingInfo.getServer());
			map.put("statementStartTime",queryParam.get("statementStartTime"));
			map.put("statementEndTime",queryParam.get("statementEndTime"));
			BigDecimal difference ;
			if(("").equals(selectDifference(map))||selectDifference(map)==null){
				 difference = new BigDecimal(0.1000);
			}
			else difference = new BigDecimal (selectDifference(map));
			gameTradingInfo.setDifference(difference);
			BigDecimal total = gameTradingInfo.getTotal();
			if((total.doubleValue()-difference.doubleValue())*0.06!=0){
				BigDecimal commission = new BigDecimal((total.doubleValue()-difference.doubleValue())*0.06);
				gameTradingInfo.setCommission(commission);
			}
			else gameTradingInfo.setCommission(new BigDecimal("0"));
			Date date=null,date1=null,date2=null ;
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				date = sdf.parse("2014-08-13 12:00:00");
				date1 = sdf.parse("2014-11-26 12:30:00");
				date2 = sdf.parse("2015-01-04 12:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
			if(gameTradingInfo.getGameName().equals("剑灵")&& ((Date)queryParam.get("statementEndTime")).after(date)){
				map.put("date", date);
				map.put("date1", date1);
				map.put("date2", date2);
				map.put("commission", gameTradingInfo.getCommission());
				gameTradingInfo.setCommission(new BigDecimal(this.countCommission(map)));
				map.remove("date");
				map.remove("date1");
				map.remove("date2");
			}	
			
		}
		
		return new GenericPage<gameTradingEO>(startIndex, totalSize, pageSize, pagedData);

	}

	@Override
	public List<gameTradingEO> selectTradingByGame1(Map<String, Object> paramMap) {
		return  this.getSqlSession().selectList("com.wzitech.gamegold.funds.entity.gameTradingEO" + ".selectByGameProp1", paramMap);
	}

	@Override
	public String selectDifference(Map<String, Object> map) {
		return  this.getSqlSession().selectOne("com.wzitech.gamegold.funds.entity.gameTradingEO" + ".selectDifferenceByMap", map);
	}

	@Override
	public double countCommission(Map<String, Object> map) {
		Date statementStartTime = (Date) map.get("statementStartTime");
		Date statementEndTime = (Date) map.get("statementEndTime");
		Date date = (Date) map.get("date");
		Date date1 = (Date) map.get("date1");
		Date date2 = (Date) map.get("date2");
		BigDecimal commission_ = (BigDecimal)map.get("commission");
		double commission = commission_.doubleValue();
		String region = (String) map.get("region");
		if(statementStartTime.after(date)){
			if(region.equals("电信传说区")||(region.equals("电信傲雪区")&&date2.after(statementEndTime))||(region.equals("网通冰魂区")&&date1.after(statementEndTime))){
				return 0;
			}
			return commission*5/6;
		}
		else{
			Double beforeCommission = this.getSqlSession().selectOne("com.wzitech.gamegold.funds.entity.gameTradingEO" + ".selectCommission", map);
			if(beforeCommission==null)  beforeCommission=(double) 0;
            if (region.equals("电信传说区") || region.equals("电信傲雪区")&&date2.after(statementEndTime) || (region.equals("网通冰魂区")&&date1.after(statementEndTime))) {
                return beforeCommission;
            } else {
                return ((commission - beforeCommission) * 5 / 6 + beforeCommission);
            }
        }
	}

    /**
     * 统计佣金和差价
     *
     * @param queryMap
     * @return
     */
    @Override
    public Map<String, BigDecimal> countCommissionAndPriceDiff(Map<String, Object> queryMap) {
        Map<String, BigDecimal> map = this.getSqlSession().selectOne(this.mapperNamespace + ".countCommissionAndPriceDiff", queryMap);
        if (map != null) {
            BigDecimal commission = map.get("commission");
            BigDecimal priceDiff = map.get("price_diff");
            if (commission != null) {
                commission = commission.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (priceDiff != null) {
                priceDiff = priceDiff.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            map.put("commission", commission);
            map.put("price_diff", priceDiff);
        }
        return map;
    }

    /**
     * 查询订单总笔数和金额
     *
     * @param queryMap
     * @return
     */
    @Override
    public Map<String, Object> queryOrderTotalCountAndAmount(Map<String, Object> queryMap) {
        Map<String, Object> map = this.getSqlSession().selectOne(this.mapperNamespace + ".queryOrderTotalCountAndAmount", queryMap);
        if (map != null) {
            BigDecimal totalAmount = (BigDecimal)map.get("TOTAL_AMOUNT");
            if (totalAmount == null) {
                totalAmount = BigDecimal.ZERO;
            }
            totalAmount = totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
            map.put("TOTAL_AMOUNT", totalAmount);
        }
        return map;
    }

	/**
	 * 查询红包和店铺券使用笔数和金额汇总
	 * @param queryMap
	 * @return
	 */
	@Override
	public Map<String, Object> queryYhqCountAndAmount(Map<String, Object> queryMap) {
		Map<String, Object> map = this.getSqlSession().selectOne(this.mapperNamespace + ".queryYhqCountAndAmount", queryMap);
		return map;
	}
}
