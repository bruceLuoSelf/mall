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
 * 资金明细查询类型
 * @author SunChengfei
 *
 */
public enum FundsQueryType {
	Payment("13", "支付查询资金类型"),
	Refund("14","退款查询资金类型"),
	Transfer("3","转账查询资金类型"),
	Compensate("100","赔付查询资金类型"),
	Withdrawals("5","获得转账资金类型");
	
    private String code;
	
	private String name;
	
	FundsQueryType(String code, String name){
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
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
	public static FundsQueryType getTypeByCode(String code){
		for(FundsQueryType type : FundsQueryType.values()){
			if(type.getCode().equals(code)){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的FundsQueryType:" + code);
	}
}
