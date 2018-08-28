/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryOrderRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.order.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		LuChangkai
 *	创建时间：	2014-1-14
 *	描	述：		
 *	更新纪录：	1. LuChangkai 创建于 2014-1-14 下午6:11:21
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 查询订单请求DTO
 * @author LuChangkai
 *
 */
@XmlRootElement
public class QueryOrderRequest extends AbstractServiceRequest{
	
	/**
	 * 用户ID
	 */
	private Long uid;
	
	/**
	 * 订单号
	 */
	private String orderId;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 排序字段
	 */
	private String orderBy;
	
	/**
	 * 是否升序
	 */
	private Boolean isAsc;
	
	/**
	 * 页面大小
	 */
	private int pageSize;
	
	/**
	 * index
	 */
	private int start;

	/**
	 * @return the uid
	 */
	@XmlElement(name = "uid")
	public Long getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * @return the orderId
	 */
	@XmlElement(name = "orderId")
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the orderState
	 */
	@XmlElement(name = "orderState")
	public Integer getOrderState() {
		return orderState;
	}

	/**
	 * @param orderState the orderState to set
	 */
	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}

	/**
	 * @return the orderBy
	 */
	@XmlElement(name = "orderBy")
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the isAsc
	 */
	@XmlElement(name = "isAsc")
	public Boolean getIsAsc() {
		return isAsc;
	}

	/**
	 * @param isAsc the isAsc to set
	 */
	public void setIsAsc(Boolean isAsc) {
		this.isAsc = isAsc;
	}

	/**
	 * @return the pageSize
	 */
	@XmlElement(name = "pageSize")
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the start
	 */
	@XmlElement(name = "start")
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	
}
