/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameCompanyInfos
 *	包	名：		com.wzitech.gamegold.common.game.entity
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-21
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-21 下午3:48:58
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.game.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 游戏厂商信息
 * @author SunChengfei
 *
 */
@XmlRootElement(name="Companies")
public class GameCompanyInfo {
	
	/**
	 * 游戏厂商列表
	 */
	private List<String> companyNameList;

	/**
	 * @return the companyNameList
	 */
	@XmlElement(name="Company")
	public List<String> getCompanyNameList() {
		return companyNameList;
	}

	/**
	 * @param companyNameList the companyNameList to set
	 */
	public void setCompanyNameList(List<String> companyNameList) {
		this.companyNameList = companyNameList;
	}
	
}
