/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		PassPortResponse
 *	包	名：		com.wzitech.gamegold.common.usermgmt.entity
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午7:16:38
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.usermgmt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 5173接入Passport返回
 * @author SunChengfei
 *
 */
public class PassPortResponse {
	/**
	 * 返回码
	 */
	private int ResultNo;
	
	/**
	 * 返回码对应信息
	 */
	private String ResultDescription;
	
	/**
	 * 返回数据
	 */
	private PassportTicket Ticket;
	
	private String Validation;
	
	private String SignNo;

	/**
	 * @return the resultNo
	 */
	public int getResultNo() {
		return ResultNo;
	}

	/**
	 * @param resultNo the resultNo to set
	 */
	@JsonProperty("ResultNo") 
	public void setResultNo(int resultNo) {
		ResultNo = resultNo;
	}

	/**
	 * @return the resultDescription
	 */
	public String getResultDescription() {
		return ResultDescription;
	}

	/**
	 * @param resultDescription the resultDescription to set
	 */
	@JsonProperty("ResultDescription") 
	public void setResultDescription(String resultDescription) {
		ResultDescription = resultDescription;
	}

	/**
	 * @return the ticket
	 */
	public PassportTicket getTicket() {
		return Ticket;
	}

	/**
	 * @param ticket the ticket to set
	 */
	@JsonProperty("Ticket") 
	public void setTicket(PassportTicket ticket) {
		Ticket = ticket;
	}

	public String getValidation() {
		return Validation;
	}

	@JsonProperty("Validation") 
	public void setValidation(String validation) {
		Validation = validation;
	}

	public String getSignNo() {
		return SignNo;
	}

	@JsonProperty("SignNo") 
	public void setSignNo(String signNo) {
		SignNo = signNo;
	}
	
}
