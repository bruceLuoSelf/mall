/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		ISellerService
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.repository
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-23
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-23 上午11:17:03
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository;

import javax.servlet.http.HttpServletRequest;

import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.ApplySellerRequest;
import com.wzitech.gamegold.facade.frontend.service.repository.dto.QuerySellerInfoRequest;

/**
 * 卖家服务接口
 * @author HeJian
 *
 */
public interface ISellerService {
	/**
	 * 申请成为卖家
	 * @param applySellerRequest
	 * @param request
	 * @return
	 */
	IServiceResponse applySeller(ApplySellerRequest applySellerRequest, HttpServletRequest request);
	
	/**
	 * 卖家上传密保卡
	 * @param uploadPasspodRequest
	 * @param file
	 * @param request
	 * @return
	 */
	IServiceResponse uploadPasspod(byte[] file, String gameName, String gameAccount, HttpServletRequest request);
	
	/**
	 * 根据登录账号和uid查找卖家信息
	 * @param querySellerInfoRequest
	 * @param request
	 * @return
	 */
	IServiceResponse querySellerInfo(QuerySellerInfoRequest querySellerInfoRequest, HttpServletRequest request);

	/**
	 * 修改卖家客服信息
	 * @param querySellerInfoRequest
	 * @param request
	 * @return
	 */
	IServiceResponse alterServicer(QuerySellerInfoRequest querySellerInfoRequest,HttpServletRequest request);
}
