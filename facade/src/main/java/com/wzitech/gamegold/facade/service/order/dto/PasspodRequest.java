/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		PasspodRequest
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		SunChengfei
 *	创建时间：	2014-3-7
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-3-7 下午1:59:36
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 获取密保卡请求
 * @author SunChengfei
 *
 */
public class PasspodRequest extends AbstractServiceRequest {
	/**
	 * 订单或者发布单Id
	 */
	private String orderid;
	
	/**
	 * 交易员Id
	 */
	private String opid;
	
	/**
	 * 操作员密码
	 */
	private String oppwd;
	
	/**
	 * 签名值
	 */
	private String sign;

	/**
	 * @return the orderid
	 */
	public String getOrderid() {
		return orderid;
	}

	/**
	 * @param orderid the orderid to set
	 */
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	/**
	 * @return the opid
	 */
	public String getOpid() {
		return opid;
	}

	/**
	 * @param opid the opid to set
	 */
	public void setOpid(String opid) {
		this.opid = opid;
	}

	/**
	 * @return the oppwd
	 */
	public String getOppwd() {
		return oppwd;
	}

	/**
	 * @param oppwd the oppwd to set
	 */
	public void setOppwd(String oppwd) {
		this.oppwd = oppwd;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}
