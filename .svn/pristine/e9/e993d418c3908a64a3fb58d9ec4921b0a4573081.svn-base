/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryBuyerListResponse
 *	包	名：		com.wzitech.gamegold.facade.service.complaint.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-25
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-25 下午5:34:46
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.complaint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 获取买家订单列表响应
 * @author HeJian
 *
 */
public class QueryBuyerListResponse{
	/**
	 * 买家订单列表
	 */
    @JsonProperty("OrderList")
	private List<OrderInfoItem> orderList;
	
	/**
	 * 总共记录数
	 */
    @JsonProperty("TotalCount")
	private Long totalCount;

    @JsonProperty("TotalPage")
	private Long totalPage;

    @JsonProperty("CurrentPage")
	private int currentPage;

    @JsonProperty("CurrentPageSize")
	private int currentPageSize;

	public List<OrderInfoItem> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<OrderInfoItem> orderList) {
		this.orderList = orderList;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public Long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getCurrentPageSize() {
		return currentPageSize;
	}

	public void setCurrentPageSize(int currentPageSize) {
		this.currentPageSize = currentPageSize;
	}

	
}
