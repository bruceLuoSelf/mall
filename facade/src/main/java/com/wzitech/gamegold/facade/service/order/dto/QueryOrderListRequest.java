/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		QueryOrderListRequest
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午7:39:03
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 查询订单列表请求
 * @author HeJian
 *
 */
public class QueryOrderListRequest extends AbstractServiceRequest{
	/**
	 * 请求的数量
	 */
	private Integer requestNum;
	
	/**
	 * 查询类型(999:查询全部
	 *          1:查询支持新版RC功能的订单
	 *          0:查询非支持新版RC功能的订单)
	 */
	private Integer queryType;
	
	/**
	 * 签名值
	 */
	private String sign;
	
	/**
	 * 订单状态(0-交易中,1-撤单申请,2-已撤单,
	 *         3-卖家确认移交,4-移交给买家,5-交易成功)
	 */
	private Integer orderStatus ;

	/**
	 * 通货类型ID
	 * lvchengsheng 5.16新增 ZW_C_JB_00008
	 */
	private Long goodsTypeId;

	/**
	 * 通货类型名称
	 * lvchengsheng 5.16新增 ZW_C_JB_00008
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
	 * @return the requestNum
	 */
	public Integer getRequestNum() {
		return requestNum;
	}

	/**
	 * @param requestNum the requestNum to set
	 */
	public void setRequestNum(Integer requestNum) {
		this.requestNum = requestNum;
	}

	/**
	 * @return the queryType
	 */
	public Integer getQueryType() {
		return queryType;
	}

	/**
	 * @param queryType the queryType to set
	 */
	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the orderStatus
	 */
	public Integer getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

}
