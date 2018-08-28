/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ApplySellerRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.repository.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-23
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-23 上午11:18:33
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;
import com.wzitech.gamegold.repository.entity.SellerGame;

import java.util.List;

/**
 * 申请成为卖家请求
 * @author HeJian
 *
 */
public class ApplySellerRequest extends AbstractServiceRequest{
	/**
	 * 联系人姓名
	 */
	private String name;
	
	/**
	 * 联系电话
	 */
	private String phoneNumber;
	
	/**
	 * 客服ID
	 */
	private Long servicerId;
	
	/**
	 * QQ
	 */
	private String Qq;
	
	/**
	 * 卖家类型
	 */
	private Integer sellerType;

	/**
	 * 关联的游戏
	 */
	private String games;

	/*
	是否开通收货
	 */
	private boolean isOpenSh;

	/**
	 * @return the servicerId
	 */
	public Long getServicerId() {
		return servicerId;
	}

	/**
	 * @param servicerId the servicerId to set
	 */
	public void setServicerId(Long servicerId) {
		this.servicerId = servicerId;
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
	 * @return the qq
	 */
	public String getQq() {
		return Qq;
	}

	/**
	 * @param qq the qq to set
	 */
	public void setQq(String qq) {
		Qq = qq;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getSellerType() {
		return sellerType;
	}

	public void setSellerType(Integer sellerType) {
		this.sellerType = sellerType;
	}

	public String getGames() {
		return games;
	}

	public void setGames(String games) {
		this.games = games;
	}

	public boolean getIsOpenSh() {
		return isOpenSh;
	}

	public void setIsOpenSh(boolean isOpenSh) {
		this.isOpenSh = isOpenSh;
	}
}
