/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		PasspodResponse
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		SunChengfei
 *	创建时间：	2014-3-7
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-3-7 下午1:59:49
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import javax.xml.bind.annotation.XmlElement;

/**
 * 获取密保卡返回
 * @author SunChengfei
 *
 */
public class PasspodResponse {
	private String msg;

	private boolean status;
	
	private String yxbMall;
	
	@XmlElement(name = "YXBMALL")
	public String getYxbMall() {
		return yxbMall;
	}

	public void setYxbMall(String yxbMall) {
		this.yxbMall = yxbMall;
	}

	@XmlElement(name = "Msg")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@XmlElement(name = "Status")
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
