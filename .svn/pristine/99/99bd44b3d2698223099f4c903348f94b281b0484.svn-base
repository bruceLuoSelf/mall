package com.wzitech.gamegold.common.paymgmt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 5173 转账配置信息
 * @author yunhai.Wu
 *
 */
@Component
public class TransferConfig {
	
	/**
	 * 转账URL
	 */
	@Value("${fund.transfer.url}")
	private String url = "";
	
	@Value("${fund.transfer.sign.secret.key}")
	private String signSecretKey = "";
	
	/**
	 * 接口名称
	 */
	@Value("${fund.transfer.service}")
	private String service = "";
	
	/**
	 * 签名方式
	 */
	@Value("${fund.transfer.sign.type}")
	private String signType = "MD5";
	
	@Value("${fund.transfer.input.charset}")
	private String inputCharset = "gb2312";
	
	@Value("${fund.transfer.client.ip}")
	private String clientIP = "";
	
	@Value("${fund.transfer.version}")
	private String version = "1.0";
	
	@Value("${fund.transfer.trading.type}")
	private String tradingType = "3";
	
	@Value("${fund.transfer.dep.name}")
	private String depName = "";
	
	@Value("${fund.transfer.create.internal.site.id}")
	private String createInterNalSiteId = "";
	
	@Value("${fund.transfer.notify.url}")
	private String notifyURL = "";
	
	//private Map<String, String> params = null;
	
	public Map<String, String> getBaseParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", this.service);
		params.put("sign_type", this.signType);
		params.put("input_charset", this.inputCharset);
		params.put("client_ip", this.clientIP);
		params.put("version", this.version);
		params.put("trading_type", this.tradingType);
		params.put("dep_name", this.depName);
		params.put("create_internal_site_id", this.createInterNalSiteId);
		params.put("notify_url", this.notifyURL);
		return params;
		/*if (this.params == null) {
			this.params = new HashMap<String, String>();
			this.params.put("service", this.service);
			this.params.put("sign_type", this.signType);
			this.params.put("input_charset", this.inputCharset);
			this.params.put("client_ip", this.clientIP);
			this.params.put("version", this.version);
			this.params.put("trading_type", this.tradingType);
			this.params.put("dep_name", this.depName);
			this.params.put("create_internal_site_id", this.createInterNalSiteId);
			this.params.put("notify_url", this.notifyURL);
		}
		return this.params;*/
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSignSecretKey() {
		return signSecretKey;
	}

	public void setSignSecretKey(String signSecretKey) {
		this.signSecretKey = signSecretKey;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getInputCharset() {
		return inputCharset;
	}

	public void setInputCharset(String inputCharset) {
		this.inputCharset = inputCharset;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTradingType() {
		return tradingType;
	}

	public void setTradingType(String tradingType) {
		this.tradingType = tradingType;
	}

//	public String getRequestIP() {
//		return requestIP;
//	}
//
//	public void setRequestIP(String requestIP) {
//		this.requestIP = requestIP;
//	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getCreateInterNalSiteId() {
		return createInterNalSiteId;
	}

	public void setCreateInterNalSiteId(String createInterNalSiteId) {
		this.createInterNalSiteId = createInterNalSiteId;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	/*public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}*/
	

	
	
}