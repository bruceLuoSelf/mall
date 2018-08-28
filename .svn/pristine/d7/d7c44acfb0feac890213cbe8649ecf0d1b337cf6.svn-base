package com.wzitech.gamegold.common.paymgmt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wzitech.gamegold.common.utils.PayHelper;

/**
 * 5173 p2p转账 接口配置信息
 * @author yunhai.Wu
 *
 */
@Component
public class BatchTransferConfig {
	
	@Value("${fund.batch.transfer.url}")
	private String url = "";
	
	@Value("${fund.batch.transfer.sign.secret.key}")
	private String signSecretKey = "";
	
	@Value("${fund.batch.transfer.service}")
	private String service;
	
	@Value("${fund.batch.transfer.sign.type}")
	private String signType;
	
	@Value("${fund.batch.transfer.input.charset}")
	private String inputCharset;
	
	@Value("${fund.batch.transfer.client.ip}")
	private String clientIP;
	
	@Value("${fund.batch.transfer.request.ip}")
	private String requestIP;

	@Value("${fund.batch.transfer.version}")
	private String version;

	@Value("${fund.batch.transfer.platform.lang}")
	private String platformLang;
	
	@Value("${fund.batch.transfer.trading.type}")
	private String tradingType;
	
	@Value("${fund.batch.transfer.dep.name}")
	private String depName;
	
	@Value("${fund.batch.transfer.create.internal.site.id}")
	private String createInternalSiteId;
	
	@Value("${fund.batch.transfer.notify.url}")
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
		params.put("platform_lang", this.platformLang);
		params.put("trading_type", this.tradingType);
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
			this.params.put("platform_lang", this.platformLang);
			this.params.put("trading_type", this.tradingType);
			this.params.put("dep_name", this.depName);
			this.params.put("create_internal_site_id", this.createInternalSiteId);
			this.params.put("notify_url", this.notifyURL);
		}
		return this.params;*/
	}
	
	
	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
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


	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPlatformLang() {
		return platformLang;
	}

	public void setPlatformLang(String platformLang) {
		this.platformLang = platformLang;
	}

	public String getTradingType() {
		return tradingType;
	}

	public void setTradingType(String tradingType) {
		this.tradingType = tradingType;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getCreateInternalSiteId() {
		return createInternalSiteId;
	}

	public void setCreateInternalSiteId(String createInternalSiteId) {
		this.createInternalSiteId = createInternalSiteId;
	}

	public String getNotifyURL() {
		return notifyURL;
	}

	public void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}
	
	
	
}