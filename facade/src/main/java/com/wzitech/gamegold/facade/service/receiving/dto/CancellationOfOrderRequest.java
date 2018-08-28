/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		CancelOrderRequest
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午8:07:53
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.receiving.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 取消订单请求
 * @author HeJian
 * ZW_C_JB_00004 yexiaokang
 *
 */
public class CancellationOfOrderRequest extends AbstractServiceRequest{
	/**
	 * 订单编号
	 */
	private String orderId;

    /**
	 * 取消原因
	 */
	private String reason;

	/**
	 *备注
	 */
	private String remarks;
	
	/**
	 * 交易员ID(loginId)
	 */
	private String opid;

	private String sign;
	
	/**
	 * 交易员登录密码(des加密)
	 */
	private String oppwd;


	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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
	 *
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 *
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 *
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 *
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
