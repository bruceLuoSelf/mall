/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QuerySellerInfoResponse
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.repository.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午12:25:19
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository.dto;

import java.util.List;

import com.wzitech.chaos.framework.server.common.AbstractServiceResponse;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;
import com.wzitech.gamegold.repository.entity.SellerInfo;

/**
 * 根据登录账号和uid查找卖家信息响应
 * @author HeJian
 *
 */
public class QuerySellerInfoResponse extends AbstractServiceResponse{
	/**
	 * 卖家实体
	 */
	private SellerInfo sellerInfo;
	
	/**
	 * 卖家密保卡信息
	 */
	private List<RepositoryInfo> repositoryInfos;

	/**
	 * 授权码
	 */
	private String copyRight;

	public String getCopyRight() {
		return copyRight;
	}

	public void setCopyRight(String copyRight) {
		this.copyRight = copyRight;
	}

	/**
	 * @return the sellerInfo
	 */
	public SellerInfo getSellerInfo() {
		return sellerInfo;
	}

	/**
	 * @param sellerInfo the sellerInfo to set
	 */
	public void setSellerInfo(SellerInfo sellerInfo) {
		this.sellerInfo = sellerInfo;
	}

	/**
	 * @return the repositoryInfos
	 */
	public List<RepositoryInfo> getRepositoryInfos() {
		return repositoryInfos;
	}

	/**
	 * @param repositoryInfos the repositoryInfos to set
	 */
	public void setRepositoryInfos(List<RepositoryInfo> repositoryInfos) {
		this.repositoryInfos = repositoryInfos;
	}

}
