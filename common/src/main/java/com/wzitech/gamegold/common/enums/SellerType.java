/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		SellerType
 *	包	名：		com.wzitech.gamegold.common.enums
 *	项目名称：	gamegold-common
 *	作	者：		Yubaihai
 *	创建时间：	2014-5-26
 *	描	述：		
 *	更新纪录：	1. Yubaihai 创建于 2014-5-26 下午17:00:12
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.enums;

public enum SellerType {
	
	PersonSeller(1,"个人玩家"),
	StudioSeller(2,"工作室");
	
    private int code;
	
	private String name;
	
	SellerType(int code, String name){
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
	public static SellerType getTypeByCode(int code){
		for(SellerType type : SellerType.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的SellerType:" + code);
	}
	
	
	
}
