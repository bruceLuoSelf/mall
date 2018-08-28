/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		CheckState
 *	包	名：		com.wzitech.gamegold.common.enums
 *	项目名称：	gamegold-common
 *	作	者：		HeJian
 *	创建时间：	2014-2-22
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-22 下午10:58:32
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.enums;

/**
 * 卖家申请审核状态枚举
 * @author HeJian
 *
 */
public enum CheckState {
	/**
	 * 未审核
	 */
	UnAudited(0),
	
	/**
	 * 审核通过
	 */
	PassAudited(1),
	
	/**
	 * 审核不通过
	 */
	UnPassAudited(2);
	
	/**
	 * 类型码
	 */
	private int code;
	
	CheckState(int code){
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
}
