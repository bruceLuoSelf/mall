/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		AddRepositoryRequest
 *	包	名：		com.wzitech.gamegold.facade.frontend.service.repository.dto
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		HeJian
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. HeJian 创建于 2014-2-20 下午3:40:09
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.frontend.service.repository.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.wzitech.chaos.framework.server.common.AbstractServiceRequest;

/**
 * 添加库存请求
 * @author HeJian
 *
 */
public class AddRepositoryRequest extends AbstractServiceRequest{
	/**
	 * 所属客服id
	 */
	private Long servicerId;
	
	/**
	 * 卖家登录5173账号
	 */
	private String loginAccount;
	
	/**
	 * 卖家登录5173账号uid
	 */
	private Long accountUid;
	
	/**
	 * 游戏账户
	 */
	private String gameAccount;
	
	/**
	 * 游戏密码
	 */
	private String gamePassWord;
	
	/**
	 * 游戏名称
	 */
	private String gameName;
	
	/**
	 * 卖家游戏角色名
	 */
	private String sellerGameRole;
	
	/**
	 * 卖家所售金币名
	 */
	private String moneyName;
	
	/**
	 * 所在区
	 */
	private String region;
	
	/**
	 * 所在服
	 */
	private String server;
	
	/**
	 * 游戏币数目
	 */
	private Integer goldCount;
	
	/**
	 * 单价(1游戏币兑换多少元)
	 */
	private BigDecimal unitPrice;
	
	/**
	 * 最后更新时间
	 */
	private Date lastUpdateTime;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 是否删除
	 */
	private Boolean isDeleted;

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
	 * @return the loginAccount
	 */
	public String getLoginAccount() {
		return loginAccount;
	}

	/**
	 * @return the sellerGameRole
	 */
	public String getSellerGameRole() {
		return sellerGameRole;
	}

	/**
	 * @param sellerGameRole the sellerGameRole to set
	 */
	public void setSellerGameRole(String sellerGameRole) {
		this.sellerGameRole = sellerGameRole;
	}

	/**
	 * @return the moneyName
	 */
	public String getMoneyName() {
		return moneyName;
	}

	/**
	 * @param moneyName the moneyName to set
	 */
	public void setMoneyName(String moneyName) {
		this.moneyName = moneyName;
	}

	/**
	 * @param loginAccount the loginAccount to set
	 */
	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	/**
	 * @return the accountUid
	 */
	public Long getAccountUid() {
		return accountUid;
	}

	/**
	 * @param accountUid the accountUid to set
	 */
	public void setAccountUid(Long accountUid) {
		this.accountUid = accountUid;
	}

	/**
	 * @return the gameAccount
	 */
	public String getGameAccount() {
		return gameAccount;
	}

	/**
	 * @param gameAccount the gameAccount to set
	 */
	public void setGameAccount(String gameAccount) {
		this.gameAccount = gameAccount;
	}

	/**
	 * @return the gamePassWord
	 */
	public String getGamePassWord() {
		return gamePassWord;
	}

	/**
	 * @param gamePassWord the gamePassWord to set
	 */
	public void setGamePassWord(String gamePassWord) {
		this.gamePassWord = gamePassWord;
	}

	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}

	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the goldCount
	 */
	public Integer getGoldCount() {
		return goldCount;
	}

	/**
	 * @param goldCount the goldCount to set
	 */
	public void setGoldCount(Integer goldCount) {
		this.goldCount = goldCount;
	}

	/**
	 * @return the unitPrice
	 */
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the lastUpdateTime
	 */
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
