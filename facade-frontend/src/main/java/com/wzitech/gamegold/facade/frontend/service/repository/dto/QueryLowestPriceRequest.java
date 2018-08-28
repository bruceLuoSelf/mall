package com.wzitech.gamegold.facade.frontend.service.repository.dto;

public class QueryLowestPriceRequest{
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
	
	/**
	 * 所在阵营
	 */
	private String Race;
	
	/**
	 * 价格数量
	 */
	private String Counter;
	
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

	public String getRace() {
		return Race;
	}

	public void setRace(String race) {
		Race = race;
	}

	public String getCounter() {
		return Counter;
	}

	public void setCounter(String counter) {
		Counter = counter;
	}
}
