/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryOrderInfoRequest
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午8:16:03
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 获取订单信息请求
 * @author HeJian
 *
 */
@XmlRootElement
public class QueryOrderInfoRequest extends AbstractServiceRequest{
	/**
	 * 订单编号
	 */
	private String orderId;
	
	/**
	 * 操作员ID(loginId)
	 */
	private String opid;
	
	/**
	 * 操作员密码(des加密)
	 */
	private String oppwd;
	
	/**
	 * 签名值
	 */
	private String sign;
	
	/**
	 * 公网IP
	 */
	private String publIP;
	
	/**
	 * 部门
	 */
	private String dept;
	
	/**
	 * 私网IP
	 */
	private String priIp;

	/**
	 * @return the orderId
	 */
	@XmlElement(name = "orderId")
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
	@XmlElement(name = "opid")
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
	@XmlElement(name = "oppwd")
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
	@XmlElement(name = "sign")
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the publIP
	 */
	@XmlElement(name = "publIP")
	public String getPublIP() {
		return publIP;
	}

	/**
	 * @param publIP the publIP to set
	 */
	public void setPublIP(String publIP) {
		this.publIP = publIP;
	}

	/**
	 * @return the dept
	 */
	@XmlElement(name = "dept")
	public String getDept() {
		return dept;
	}

	/**
	 * @param dept the dept to set
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}

	/**
	 * @return the priIp
	 */
	@XmlElement(name = "priIp")
	public String getPriIp() {
		return priIp;
	}

	/**
	 * @param priIp the priIp to set
	 */
	public void setPriIp(String priIp) {
		this.priIp = priIp;
	}
}
