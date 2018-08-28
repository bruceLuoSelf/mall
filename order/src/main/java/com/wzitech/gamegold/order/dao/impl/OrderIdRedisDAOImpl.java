/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderIdRedisDAOImpl
 *	包	名：		com.wzitech.gamegold.order.dao.impl
 *	项目名称：	gamegold-order
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-14 下午3:50:46
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.order.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.order.dao.IOrderIdRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

/**
 * @author SunChengfei
 *
 */
@Repository
public class OrderIdRedisDAOImpl extends AbstractRedisDAO<UserInfoEO> implements
IOrderIdRedisDAO {
	@Autowired
	@Qualifier("userRedisTemplate")
	@Override
	public void setTemplate(StringRedisTemplate template) {
		super.template = template;
	};
	/*
	 * (non-Javadoc)
	 * @see com.wzitech.gamegold.order.dao.IOrderIdRedisDAO#getOrderId()
	 */
	@Override
	public String getOrderId() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		Date now = new Date();
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(now);
		long id = valueOps.increment(ORDER_ID_SEQ, 1);
		if(id==1L){
			//设置缓存数据最后的失效时间为当天的最后一秒
			nowCal = Calendar.getInstance();
			nowCal.setTime(now);
			Calendar lastCal = Calendar.getInstance();
			lastCal.set(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
			lastCal.set(Calendar.MILLISECOND, 999);
			template.expireAt(ORDER_ID_SEQ, lastCal.getTime());
		}
		String orderId = StringUtils.leftPad(String.valueOf(id), 7, '0');
		StringBuffer sb = new StringBuffer("YX");
		sb.append(sdf.format(nowCal.getTime())).append(orderId);
		return sb.toString();
	}

}
