/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TradeOrderManagerImpl
 *	包	名：		com.wzitech.gamegold.common.tradeorder.impl
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-4-9
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-4-9 下午4:06:55
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.tradeorder.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.tradeorder.ITradeOrderManager;
import com.wzitech.gamegold.common.tradeorder.client.GeneBizOffer;
import com.wzitech.gamegold.common.tradeorder.client.YxbBizOfferDataIn;
import com.wzitech.gamegold.common.tradeorder.client.YxbMallServiceSoap;
import com.wzitech.gamegold.common.tradeorder.header.ServerHeaderOutProcessor;

/**
 * @author SunChengfei
 * 
 */
@Component
public class TradeOrderManagerImpl implements ITradeOrderManager {
	/**
	 * 日志记录器
	 */
	protected final Logger logger = LoggerFactory
			.getLogger(TradeOrderManagerImpl.class);

	@Value("${tradeservice.request.url}")
	private String requestUrl;

	@Value("${tradeservice.request.uid}")
	private String requestUid;

	@Value("${tradeservice.request.password}")
	private String requestPassword;

	private JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

	@Override
	public GeneBizOffer queryTradeOrder(String gameName, String region,
			String server, String gameRace, int pageIndex, int pageSize,
			BigDecimal minPrice, BigDecimal maxPrice, int serviceType) {
		try {
			JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
			factoryBean.getOutInterceptors().add(new ServerHeaderOutProcessor(requestUid,requestPassword));
			factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());

			factoryBean.setServiceClass(YxbMallServiceSoap.class);
			factoryBean.setAddress(requestUrl);
			YxbMallServiceSoap service = (YxbMallServiceSoap) factoryBean.create();

			YxbBizOfferDataIn dataIn = new YxbBizOfferDataIn();
			dataIn.setGameAreaName(region);
			dataIn.setGameName(gameName);
			dataIn.setGameRaceName(gameRace);
			dataIn.setGameServerName(server);
			dataIn.setMaxPrice(maxPrice);
			dataIn.setMinPrice(minPrice);
			dataIn.setPageIndex(pageIndex);
			dataIn.setPageSize(pageSize);
			dataIn.setServiceType(serviceType);
			return jsonMapper.fromJson(service.getYxbBizOfferList(dataIn),
					GeneBizOffer.class);
		} catch (Exception e) {
			logger.error("寄售、担保查询发布单发生异常:{}", ExceptionUtils.getStackTrace(e));
		}

		return null;
	}
}
