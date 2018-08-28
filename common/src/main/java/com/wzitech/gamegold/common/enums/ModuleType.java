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
 * 模块类型枚举
 * @author ztjie
 *
 */
public enum ModuleType {
	/**
	 * 订单
	 */
	ORDER(1,"订单"),
	
	/**
	 * 用户
	 */
	USER(2,"用户"),
	
	/**
	 * 商品
	 */
	GOODS(3,"商品"),
	
	/**
	 * 库存
	 */
	REPOSITORY(4,"库存"),
	
	/**
	 * 资金
	 */
	FUND(4,"资金");
	
    private int code;
	
	private String name;
	
	ModuleType(int code, String name){
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
	public static ModuleType getTypeByCode(int code){
		for(ModuleType type : ModuleType.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的ModuleType:" + code);
	}
}
