/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		OrderServiceImpl
 *	包	名：		com.wzitech.gamegold.facade.service.order.impl
 *	项目名称：	gamegold-facade
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-24
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-24 下午5:32:28
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author SunChengfei
 *
 */
@XmlRootElement(name="Order")
public class OrderListItemDTO {
	private String id;

	private String gn;

	/**
	 * 卖家所在游戏服
	 */
	private String gsn;
	/**
	 * 卖家所在游戏区
	 */
	private String gan;

	private String opId;

	private boolean isNewRC;

	private boolean isBlocked;

	private boolean isVIPkefu;

	private String wr;

	private String rdIp;

	private String yxbMall;

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

	/**
	 *商品数字ID
	 */
	private String digitalId;

	/**
	 * 游戏等级
	 */
	private Integer gameGrade;

	/**
	 * 是否跨区
	 */
	private String interregional;

	@XmlElement(name = "Interregional")
	public String getInterregional() {
		return interregional;
	}

	public void setInterregional(String interregional) {
		this.interregional = interregional;
	}

	@XmlElement(name = "DigitalId")
	public String getDigitalId() {
		return digitalId;
	}

	public void setDigitalId(String digitalId) {
		this.digitalId = digitalId;
	}
	@XmlElement(name = "GameGrade")
	public Integer getGameGrade() {
		return gameGrade;
	}

	public void setGameGrade(Integer gameGrade) {
		this.gameGrade = gameGrade;
	}

	@XmlElement(name = "GoodsTypeId")
	public Long getGoodsTypeId() {
		return goodsTypeId;
	}

	public void setGoodsTypeId(Long goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}

	@XmlElement(name = "GoodsTypeName")
	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}

	/**
	 * 是否寄售
	 */
	private boolean isCon;

	/**
	 * @return the id
	 */
	@XmlElement(name = "Id")
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the gn
	 */
	@XmlElement(name = "GN")
	public String getGn() {
		return gn;
	}

	/**
	 * @param gn
	 *            the gn to set
	 */
	public void setGn(String gn) {
		this.gn = gn;
	}

	/**
	 * @return the gsn
	 */
	@XmlElement(name = "GSN")
	public String getGsn() {
		return gsn;
	}

	/**
	 * @param gsn
	 *            the gsn to set
	 */
	public void setGsn(String gsn) {
		this.gsn = gsn;
	}

	/**
	 * @return the ganString
	 */
	@XmlElement(name = "GAN")
	public String getGan() {
		return gan;
	}

	/**
	 * @param ganString
	 *            the ganString to set
	 */
	public void setGan(String gan) {
		this.gan = gan;
	}

	/**
	 * @return the opId
	 */
	@XmlElement(name = "OpId")
	public String getOpId() {
		return opId;
	}

	/**
	 * @param opId
	 *            the opId to set
	 */
	public void setOpId(String opId) {
		this.opId = opId;
	}

	/**
	 * @return the isNewRC
	 */
	@XmlElement(name = "IsNewRC")
	public boolean isNewRC() {
		return isNewRC;
	}

	/**
	 * @param isNewRC
	 *            the isNewRC to set
	 */
	public void setNewRC(boolean isNewRC) {
		this.isNewRC = isNewRC;
	}

	/**
	 * @return the isBlocked
	 */
	@XmlElement(name = "IsBlocked")
	public boolean isBlocked() {
		return isBlocked;
	}

	/**
	 * @param isBlocked
	 *            the isBlocked to set
	 */
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	/**
	 * @return the isVIPkefu
	 */
	@XmlElement(name = "IsVIPkefu")
	public boolean isVIPkefu() {
		return isVIPkefu;
	}

	/**
	 * @param isVIPkefu
	 *            the isVIPkefu to set
	 */
	public void setVIPkefu(boolean isVIPkefu) {
		this.isVIPkefu = isVIPkefu;
	}

	/**
	 * @return the wr
	 */
	@XmlElement(name = "WR")
	public String getWr() {
		return wr;
	}

	/**
	 * @param wr
	 *            the wr to set
	 */
	public void setWr(String wr) {
		this.wr = wr;
	}

	/**
	 * @return the rdIp
	 */
	@XmlElement(name = "RDIP")
	public String getRdIp() {
		return rdIp;
	}

	/**
	 * @param rdIp
	 *            the rdIp to set
	 */
	public void setRdIp(String rdIp) {
		this.rdIp = rdIp;
	}

	/**
	 * @return the yxbMall
	 */
	@XmlElement(name = "YXBMALL")
	public String getYxbMall() {
		return yxbMall;
	}

	/**
	 * @param yxbMall
	 *            the yxbMall to set
	 */
	public void setYxbMall(String yxbMall) {
		this.yxbMall = yxbMall;
	}

	@XmlElement(name = "IsCon")
	public boolean isCon() {
		return isCon;
	}

	public void setIsCon(boolean isCon) {
		this.isCon = isCon;
	}
}
