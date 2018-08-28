/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TransferOrderResponse
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午7:48:36
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import com.wzitech.gamegold.facade.serializer.JaxbDateSerializer;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 订单移交响应
 * 
 * @author HeJian
 * 
 */
@XmlRootElement(name="Result")
public class TransferOrderResponse {
	private String msg;

	private boolean status;

	/**
	 * 移交时间
	 */
	private Date deliverTime;
	
	private String yxbMall;

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
	 * @return the deliverTime
	 */
	@XmlElement(name = "DeliverTime")
    @XmlJavaTypeAdapter(JaxbDateSerializer.class)
	public Date getDeliverTime() {
		return deliverTime;
	}

	/**
	 * @param deliverTime
	 *            the deliverTime to set
	 */
	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}
	
	/**
	 * @return the yxbMall
	 */
	@XmlElement(name = "YXBMALL")
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

}
