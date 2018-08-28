/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		MerchandiseState
 *	包	名：		com.wzitech.gamegold.common.enums
 *	项目名称：	gamegold-goodsmgmt
 *	作	者：		SunChengfei
 *	创建时间：	2014-01-29
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-01-29 下午2:21:27
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.enums;

/**
 * 交易类型
 * @author ztjie
 *
 */
public enum TradeType {
	/**
	 * 1 当面交易
	 */
	NoDivid(1, "当面交易"),
	
	/**
	 * 2  游戏内邮寄
	 */
	Divided(2, "游戏内邮寄"),

	/**
	 * 3 公会交易
	 */
	UnionTrading(3, "公会交易");
	
	/**
	 * 类型码
	 */
	private int code;
	
	TradeType(int code, String name){
		this.code = code;
		this.name = name;
	}
	
	private String name;

	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * 根据code值获取对应的枚举
	 * @param code
	 * @return
	 */
	public static TradeType getTypeByCode(int code){
		for(TradeType type : TradeType.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的DividedState:" + code);
	}
}
