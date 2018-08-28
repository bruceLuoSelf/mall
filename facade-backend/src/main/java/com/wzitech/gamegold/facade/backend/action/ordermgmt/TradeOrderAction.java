package com.wzitech.gamegold.facade.backend.action.ordermgmt;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.wzitech.gamegold.common.tradeorder.ITradeOrderManager;
import com.wzitech.gamegold.common.tradeorder.client.BizOffer;
import com.wzitech.gamegold.common.tradeorder.client.GeneBizOffer;
import com.wzitech.gamegold.facade.backend.extjs.AbstractAction;
import com.wzitech.gamegold.facade.backend.interceptor.ExceptionToJSON;

@Controller
@Scope("prototype")
@ExceptionToJSON
public class TradeOrderAction extends AbstractAction {

	private List<BizOffer> bizOfferList;
	
	private String gameName;
	
	private String region;
	
	private String server;
	
	private String gameRace;
	
	private BigDecimal minPrice;
	
	private BigDecimal maxPrice;
	
	private Integer serviceType;
	
	@Autowired
	ITradeOrderManager tradeOrderManager;

	/**
	 * 查询担保收货单信息列表
	 * @return
	 */
	public String queryTradeOrderList() {
		GeneBizOffer geneBizOffer = tradeOrderManager.queryTradeOrder(gameName, region, server, gameRace, this.page, this.limit, minPrice, maxPrice, serviceType);
		if(geneBizOffer!=null){
			bizOfferList = geneBizOffer.getBizOfferList();
			totalCount = (long) geneBizOffer.getTotalRecords();			
		}
		return this.returnSuccess();
	}

	public List<BizOffer> getBizOfferList() {
		return bizOfferList;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void setGameRace(String gameRace) {
		this.gameRace = gameRace;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = new BigDecimal(minPrice);
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = new BigDecimal(maxPrice);
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}
	
}