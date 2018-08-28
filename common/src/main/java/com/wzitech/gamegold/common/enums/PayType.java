/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		PayType
 *	包	名：		com.wzitech.gamegold.common.enums
 *	项目名称：	gamegold-common
 *	作	者：		HeJian
 *	创建时间：	2014-1-15
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-15 上午11:03:24
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.enums;

/**
 * 支付类型枚举
 * @author HeJian
 *
 */
public enum PayType {
	YiBao(1,"易宝支付"),
	Alipay(2,"支付宝"),
	CaifuTong(3,"财付通"),
	YinLian(4,"银联在线");
	
    private int code;
	
	private String name;
	
	PayType(int code, String name){
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
	public static PayType getTypeByCode(int code){
		for(PayType type : PayType.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的PayType:" + code);
	}
}
