/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		LogisticsSheetResponse
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午8:08:08
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.receiving.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 获取取消订单原因响应
 * 
 * @author HeJian
 * ZW_C_JB_00004 yexiaokang
 */
@XmlRootElement(name="Result")
public class CancellationOrderReasonResponse {
	private String msg;

	private boolean status;
	
	private String yxbMall;

	/**
	 * 取消原因
	 */
	private String reason;
	
	/**
	 * @return the yxbMall
	 */
    @XmlElement(name = "shYXBMALL")
    public String getYxbMall() {
		return yxbMall;
	}

	/**
	 * @param yxbMall
	 *            the yxbMall to set
	 */
	public void setYxbMall(String yxbMall) {
		this.yxbMall = yxbMall;
	}

	/**
	 * @return the msg
	 */
	@XmlElement(name = "Msg")
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the status
	 */
	@XmlElement(name = "Status")
	public boolean isStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
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
}
