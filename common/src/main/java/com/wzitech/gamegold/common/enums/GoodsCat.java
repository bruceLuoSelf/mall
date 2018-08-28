/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GoodsCat
 *	包	名：		com.wzitech.gamegold.common.enums
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 上午11:03:24
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.enums;

/**
 * 商品所属类目
 * @author SunChengfei
 *
 */
public enum GoodsCat {
	/**
	 * 商城最低价商品
	 */
	Cat1(1,"栏目1"),
	/**
	 * 商城自定义商品
	 */
	Cat2(2,"栏目2"),
	/**
	 * 卖家店铺商品
	 */
	Cat3(3,"栏目3");
	
    private int code;
	
	private String name;
	
	GoodsCat(int code, String name){
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
	public static GoodsCat getTypeByCode(int code){
		for(GoodsCat type : GoodsCat.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的GoodsCat:" + code);
	}
}
