/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ServiceType
 *	包	名：		com.wzitech.gamegold.common.enums
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-4-11
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-4-11 下午12:47:20
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.enums;

/**
 * @author SunChengfei
 *
 */
public enum ServiceType {
	DanBao(1,"担保"),
	JiShou(2,"寄售");
	
    private int code;
	
	private String name;
	
	ServiceType(int code, String name){
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 根据code值获取对应的枚举
	 * @param code
	 * @return
	 */
	public static ServiceType getTypeByCode(int code){
		for(ServiceType type : ServiceType.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的ServiceType:" + code);
	}
}
