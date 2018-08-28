/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		TransferOrderRequest
 *	包	名：		com.wzitech.gamegold.facade.service.order.dto
 *	项目名称：	gamegold-facade
 *	作	者：		HeJian
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-24 下午7:48:08
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 订单移交请求
 * @author HeJian
 *
 */
public class TransferOrderRequest extends AbstractServiceRequest{
	/**
	 * 订单编号
	 */
	private String orderId;
	
	/**
	 * 签名值
	 */
	private String sign;
	
	/**
	 * 交易员Id
	 */
	private String opid;
	
	/**
	 * 交易地点(进行gb2312编码)
	 */
	private String locus;
	
	/**
	 * 买方角色名
	 */
	private String gameRole;
	
	/**
	 * 机器编号
	 */
	private String computerNumber;
	
	/**
	 * 数量(没有值)
	 */
	private String number;
	
	/**
	 * 备注信息(进行gb2312编码)
	 */
	private String remark;
	
	/**
	 * 操作员密码(des加密)
	 */
	private String oppwd;
	
	/**
	 * 原值( 0：RC2移交 10：半自动移交  20：全自动移交)
	 * 新值：( 9100：RC2移交  9110：半自动移交  
	 * 		   9120：全自动移交  9130，9140，9150备用)
	 */
	private String porcessFlag;
	
	/**
	 * 
	 */
	private String GameState;

	/**
	 * @return the orderId
	 */
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

	public String getOpid() {
		return opid;
	}

	public void setOpid(String opid) {
		this.opid = opid;
	}

	/**
	 * @return the locus
	 */
	public String getLocus() {
		return locus;
	}

	/**
	 * @param locus the locus to set
	 */
	public void setLocus(String locus) {
		this.locus = locus;
	}

	/**
	 * @return the gameRole
	 */
	public String getGameRole() {
		return gameRole;
	}

	/**
	 * @param gameRole the gameRole to set
	 */
	public void setGameRole(String gameRole) {
		this.gameRole = gameRole;
	}

	/**
	 * @return the computerNumber
	 */
	public String getComputerNumber() {
		return computerNumber;
	}

	/**
	 * @param computerNumber the computerNumber to set
	 */
	public void setComputerNumber(String computerNumber) {
		this.computerNumber = computerNumber;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the oppwd
	 */
	public String getOppwd() {
		return oppwd;
	}

	/**
	 * @param oppwd the oppwd to set
	 */
	public void setOppwd(String oppwd) {
		this.oppwd = oppwd;
	}

	/**
	 * @return the porcessFlag
	 */
	public String getPorcessFlag() {
		return porcessFlag;
	}

	/**
	 * @param porcessFlag the porcessFlag to set
	 */
	public void setPorcessFlag(String porcessFlag) {
		this.porcessFlag = porcessFlag;
	}

	/**
	 * @return the gameState
	 */
	public String getGameState() {
		return GameState;
	}

	/**
	 * @param gameState the gameState to set
	 */
	public void setGameState(String gameState) {
		GameState = gameState;
	}
}
