/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryBuyerListRequest
 *	包	名：		com.wzitech.gamegold.facade.service.complaint.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-25
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-25 下午5:33:47
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.complaint.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 获取买家订单列表请求
 * @author HeJian
 *
 */
public class QueryBuyerListRequest extends AbstractServiceRequest{
	/**
	 * 用户ID
	 */
	private String uid;
	
	/**
	 * 起始时间
	 */
	private String minDate;
	
	/**
	 * 结束时间
	 */
	private String maxDate;
	
	/**
	 * 页码(从1开始计算)
	 */
	private Integer p;
	
	/**
	 * 每页条数(默认10)
	 */
	private Integer ps;
	
	/**
	 * 类型：buyer(买家)/seller(卖家)
	 */
	private String type;

	/**
	 * 通货类型ID
	 * lvchengsheng 5.16新增 ZW_C_JB_00008 商城增加通货
	 */
	private Long goodsTypeId;

	/**
	 * 通货类型名称
	 * lvchengsheng 5.16新增 ZW_C_JB_00008 商城增加通货
	 */
	private String goodsTypeName;

	public Long getGoodsTypeId() {
		return goodsTypeId;
	}

	public void setGoodsTypeId(Long goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getMinDate() {
		return minDate;
	}

	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	public String getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * @return the p
	 */
	public Integer getP() {
		return p;
	}

	/**
	 * @param p the p to set
	 */
	public void setP(Integer p) {
		this.p = p;
	}

	/**
	 * @return the ps
	 */
	public Integer getPs() {
		return ps;
	}

	/**
	 * @param ps the ps to set
	 */
	public void setPs(Integer ps) {
		this.ps = ps;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
