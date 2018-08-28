/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		PassportTicket
 *	包	名：		com.wzitech.gamegold.common.usermgmt.entity
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午7:20:06
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.usermgmt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Passport返回Ticket
 * @author SunChengfei
 *
 */
public class PassportTicket {
	private String Token;
	
	private String UserID;
	
	private String UserName;

	/**
	 * @return the token
	 */
	public String getToken() {
		return Token;
	}

	/**
	 * @param token the token to set
	 */
	@JsonProperty("Token") 
	public void setToken(String token) {
		Token = token;
	}

	/**
	 * @return the userID
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * @param userID the userID to set
	 */
	@JsonProperty("UserID") 
	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserName() {
		return UserName;
	}

	@JsonProperty("UserName") 
	public void setUserName(String userName) {
		UserName = userName;
	}
	
}
