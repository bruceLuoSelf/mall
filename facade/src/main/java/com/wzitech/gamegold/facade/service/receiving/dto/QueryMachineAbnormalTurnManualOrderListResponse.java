/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		LogisticsSheetResponse
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午8:08:08
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.receiving.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 异常转人工响应
 * ZW_C_JB_00004 yexiaokang
 */
@XmlRootElement(name = "Orders")
public class QueryMachineAbnormalTurnManualOrderListResponse {
	/**
	 * 异常转人工订单集合
	 */
	private List<QueryMachineAbnormalTurnManualListItemDTO> machineAbnormalTurnManualOrderList;

	/**
	 * 状态信息
	 */
	private String msg;

	/**
	 * 状态
	 */
	private boolean status;

	@XmlElement(name = "Order")
	public List<QueryMachineAbnormalTurnManualListItemDTO> getMachineAbnormalTurnManualOrderList() {
		return machineAbnormalTurnManualOrderList;
	}

	public void setMachineAbnormalTurnManualOrderList(List<QueryMachineAbnormalTurnManualListItemDTO> machineAbnormalTurnManualOrderList) {
		this.machineAbnormalTurnManualOrderList = machineAbnormalTurnManualOrderList;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
