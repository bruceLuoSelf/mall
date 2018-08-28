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
package com.wzitech.gamegold.facade.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 取消订单请求
 * @author HeJian
 *
 */
public class CancelOrderRequest extends AbstractServiceRequest{
	/**
	 * 订单编号
	 */
	private String orderId;
	
	/**
	 * 交易员ID(loginId)
	 */
	private String opid;
	
	/**
	 * 取消原因(base64运算后的值)
	 */
	private String cancelRemark;
	
	private String sign;
	
	/**
	 * 交易员登录密码(des加密)
	 */
	private String oppwd;
	
	/**
	 * 取消责任ID(0：卖家，1：买家，2：第三方)
	 */
	private Integer duty;
	
	/**
	 * =1：重新配置库存
	 */
	private Integer reconfig;

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

	/**
	 * @return the cancelRemark
	 */
	public String getCancelRemark() {
		return cancelRemark;
	}

	/**
	 * @param cancelRemark the cancelRemark to set
	 */
	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
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
	 * @return the duty
	 */
	public Integer getDuty() {
		return duty;
	}

	/**
	 * @param duty the duty to set
	 */
	public void setDuty(Integer duty) {
		this.duty = duty;
	}

	/**
	 * @return the reconfig
	 */
	public Integer getReconfig() {
		return reconfig;
	}

	/**
	 * @param reconfig the reconfig to set
	 */
	public void setReconfig(Integer reconfig) {
		this.reconfig = reconfig;
	}
	
	
}
