package com.wzitech.gamegold.funds.entity;

import java.math.BigDecimal;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;

/**
 * 区服统计信息类
 * @author xueao
 *
 */
public class gameTradingEO extends BaseEntity{

	/**
	 * 总成交
	 */
	private BigDecimal total;
	
	/**
	 * 成交笔数
	 */
	private int tradingNum;
	
	/**
	 * 佣金收入
	 */
	private BigDecimal commission;

	/**
	 * 差额收入
	 */
	private BigDecimal difference;
	
	/**
	 * 一个区服的交易金币数
	 */
	private Integer goldCount;
	
	/**
	 * 游戏名称
	 */
	private String gameName;
	
	/**
	 * 所在区
	 */
	private String region;
	
	/**
	 * 所在服
	 */
	private String server;

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public int getTradingNum() {
		return tradingNum;
	}

	public void setTradingNum(int tradingNum) {
		this.tradingNum = tradingNum;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getDifference() {
		return difference;
	}

	public void setDifference(BigDecimal difference) {
		this.difference = difference;
	}
	
	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
	public Integer getGoldCount() {
		return goldCount;
	}

	public void setGoldCount(Integer goldCount) {
		this.goldCount = goldCount;
	}

}
