package com.wzitech.gamegold.common.paymgmt.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 5173查询明细接口配置信息
 * @author yunhai.Wu
 *
 */
@Component
public class QueryConfig {

	@Value("${fund.query.url}")
	private String url = "";
	
	@Value("${fund.query.sign.secret.key}")
	private String signSecretKey = "";
	
	@Value("${fund.query.service}")
	private String service = "";
	
	@Value("${fund.query.sign.type}")
	private String signType = "MD5";
	
	@Value("${fund.query.input.charset}")
	private String inputCharset = "gb2312";
	
	@Value("${fund.query.client.ip}")
	private String clientIP = "";
	
	@Value("${fund.query.version}")
	private String version = "";
	
	@Value("${fund.query.trading.type}")
	private String tradingType = "";
	
	@Value("${fund.query.dep.name}")
	private String depName = "";
	
	@Value("${fund.query.create.internal.site.id}")
	private String createInterNalSiteId = "";
	
	@Value("${fund.query.notify.url}")
	private String notifyURL = "";
	
	@Value("${fund.query.platform.lang}")
	private String platformLang;

	@Value("${va.fund.query.url}")
	private String vaUrl = "";

	@Value("${va.fund.query.sign.secret.key}")
	private String vaSignSecretKey = "";

	@Value("${va.fund.query.site.domain.name}")
	private String vaSiteDomainName = "";

	@Value("${va.fund.query.client.ip}")
	private String vaClientIp = "";

	@Value("${va.fund.query.input.charset}")
	private String vaInputCharset = "";

	@Value("${va.fund.query.version}")
	private String vaVersion = "";
	
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
		params.put("platform_lang", this.platformLang);
		params.put("is_need_time", "0");
		params.put("is_need_original_trade_no", "1");
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
			this.params.put("platform_lang", this.platformLang);
            this.params.put("is_need_time", "0");
            this.params.put("is_need_original_trade_no", "1");
		}
		return this.params;*/
	}

	public Map<String, String> getBaseWithdrawalsParams() {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("VerSion", this.vaVersion);
		params.put("SiteDomainName", this.vaSiteDomainName);
		params.put("ClientIP", this.vaClientIp);
		params.put("CharSet", this.vaInputCharset);
		return params;
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

	public String getPlatformLang() {
		return platformLang;
	}

	public void setPlatformLang(String platformLang) {
		this.platformLang = platformLang;
	}

	public String getVaUrl() {
		return vaUrl;
	}

	public void setVaUrl(String vaUrl) {
		this.vaUrl = vaUrl;
	}

	public String getVaSignSecretKey() {
		return vaSignSecretKey;
	}

	public void setVaSignSecretKey(String vaSignSecretKey) {
		this.vaSignSecretKey = vaSignSecretKey;
	}

	public String getVaSiteDomainName() {
		return vaSiteDomainName;
	}

	public void setVaSiteDomainName(String vaSiteDomainName) {
		this.vaSiteDomainName = vaSiteDomainName;
	}

	public String getVaClientIp() {
		return vaClientIp;
	}

	public void setVaClientIp(String vaClientIp) {
		this.vaClientIp = vaClientIp;
	}

	public String getVaInputCharset() {
		return vaInputCharset;
	}

	public void setVaInputCharset(String vaInputCharset) {
		this.vaInputCharset = vaInputCharset;
	}

	public String getVaVersion() {
		return vaVersion;
	}

	public void setVaVersion(String vaVersion) {
		this.vaVersion = vaVersion;
	}
}
