/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderState
 *	包	名：		com.wzitech.gamegold.common.enums
 *	项目名称：	gamegold-common
 *	作	者：		HeJian
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-1-14 下午4:23:22
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.enums;

/**
 * 订单状态枚举
 * @author HeJian
 *
 */
public enum OrderState {
	/**
	 * 等待付款
	 */
	WaitPayment(1, "待付款"),
	
	/**
	 * 已付款
	 */
	Paid(2, "已付款"),
	
	/**
	 * 待发货
	 */
	WaitDelivery(3, "待发货"),
	
	/**
	 * 已发货
	 */
	Delivery(4, "已发货"),
	
	/**
	 * 结单
	 */
	Statement(5, "结单"),
	
	/**
	 * 已退款
	 */
	Refund(6, "已退款"),
	
	/**
	 * 已取消
	 */
	Cancelled(7, "已取消"),
	
	/**
	 * 已收获
	 */
	Receive(8, "已收货");
	
	/**
	 * 类型码
	 */
	private int code;
	
	private String name;
	
	OrderState(int code, String name){
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	public String getName(){
		return name;
	}
	
	/**
	 * 根据code值获取对应的枚举
	 * @param code
	 * @return
	 */
	public static OrderState getTypeByCode(int code){
		for(OrderState type : OrderState.values()){
			if(type.getCode() == code){
				return type;
			}
		}
		
		throw new IllegalArgumentException("未能找到匹配的OrderState:" + code);
	}
}
