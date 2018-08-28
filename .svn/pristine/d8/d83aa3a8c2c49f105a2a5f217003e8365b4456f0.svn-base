/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		CompensateConfig
 *	包	名：		com.wzitech.gamegold.common.paymgmt.config
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-3-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-3-20 下午2:14:58
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.paymgmt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wzitech.gamegold.common.utils.PayHelper;

/**
 * 赔付接口配置
 * @author SunChengfei
 *
 */
@Component
public class CompensateConfig {
	@Value("${fund.compensate.url}")
	private String url = "";
	
	@Value("${fund.compensate.sign.secret.key}")
	private String signSecretKey = "";
	
	@Value("${fund.compensate.service}")
	private String service;
	
	@Value("${fund.compensate.sign.type}")
	private String signType;
	
	@Value("${fund.compensate.input.charset}")
	private String inputCharset;
	
	@Value("${fund.compensate.client.ip}")
	private String clientIP;
	
	@Value("${fund.compensate.request.ip}")
	private String requestIP;

	@Value("${fund.compensate.version}")
	private String version;
	
	@Value("${fund.compensate.trading.type}")
	private String tradingType;
	
	@Value("${fund.compensate.attach}")
	private String attach;
	
	@Value("${fund.compensate.depname}")
	private String depName;
	
	@Value("${fund.compensate.create.internal.site.id}")
	private String createInternalSiteId;
	
	@Value("${fund.compensate.notify.url}")
	private String notifyURL;
	
	//private Map<String, String> params = null;

	public Map<String, String> getBaseParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", this.service);
		params.put("sign_type", this.signType);
		params.put("input_charset", this.inputCharset);
		params.put("client_ip", String.valueOf(PayHelper.ipToLong(this.clientIP)));
		params.put("request_ip", this.requestIP);
		params.put("version", this.version);
		params.put("trading_type", this.tradingType);
		params.put("attach", this.attach);
		params.put("dep_name", this.depName);
		params.put("create_internal_site_id", this.createInternalSiteId);
		params.put("notify_url", this.notifyURL);
		return params;
		/*if (this.params == null) {
			this.params = new HashMap<String, String>();
			this.params.put("service", this.service);
			this.params.put("sign_type", this.signType);
			this.params.put("input_charset", this.inputCharset);
			this.params.put("client_ip", String.valueOf(PayHelper.ipToLong(this.clientIP)));
			this.params.put("request_ip", this.requestIP);
			this.params.put("version", this.version);
			this.params.put("trading_type", this.tradingType);
			this.params.put("attach", this.attach);
			this.params.put("dep_name", this.depName);
			this.params.put("create_internal_site_id", this.createInternalSiteId);
			this.params.put("notify_url", this.notifyURL);
		}
		return this.params;*/
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the signSecretKey
	 */
	public String getSignSecretKey() {
		return signSecretKey;
	}

	/**
	 * @param signSecretKey the signSecretKey to set
	 */
	public void setSignSecretKey(String signSecretKey) {
		this.signSecretKey = signSecretKey;
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @return the signType
	 */
	public String getSignType() {
		return signType;
	}

	/**
	 * @param signType the signType to set
	 */
	public void setSignType(String signType) {
		this.signType = signType;
	}

	/**
	 * @return the inputCharset
	 */
	public String getInputCharset() {
		return inputCharset;
	}

	/**
	 * @param inputCharset the inputCharset to set
	 */
	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	/**
	 * @return the clientIP
	 */
	public String getClientIP() {
		return clientIP;
	}

	/**
	 * @param clientIP the clientIP to set
	 */
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	/**
	 * @return the requestIP
	 */
	public String getRequestIP() {
		return requestIP;
	}

	/**
	 * @param requestIP the requestIP to set
	 */
	public void setRequestIP(String requestIP) {
		this.requestIP = requestIP;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the tradingType
	 */
	public String getTradingType() {
		return tradingType;
	}

	/**
	 * @param tradingType the tradingType to set
	 */
	public void setTradingType(String tradingType) {
		this.tradingType = tradingType;
	}

	/**
	 * @return the attach
	 */
	public String getAttach() {
		return attach;
	}

	/**
	 * @param attach the attach to set
	 */
	public void setAttach(String attach) {
		this.attach = attach;
	}

	/**
	 * @return the depName
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * @param depName the depName to set
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/**
	 * @return the createInternalSiteId
	 */
	public String getCreateInternalSiteId() {
		return createInternalSiteId;
	}

	/**
	 * @param createInternalSiteId the createInternalSiteId to set
	 */
	public void setCreateInternalSiteId(String createInternalSiteId) {
		this.createInternalSiteId = createInternalSiteId;
	}

	/**
	 * @return the notifyURL
	 */
	public String getNotifyURL() {
		return notifyURL;
	}

	/**
	 * @param notifyURL the notifyURL to set
	 */
	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	/**
	 * @return the params
	 */
	/*public Map<String, String> getParams() {
		return params;
	}

	*//**
	 * @param params the params to set
	 *//*
	public void setParams(Map<String, String> params) {
		this.params = params;
	}*/
	
}
