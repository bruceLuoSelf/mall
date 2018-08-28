package com.wzitech.gamegold.common.paymgmt.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 退款配置
 * @author yunhai.Wu
 *
 */
@Component
public class RefundConfig {
	
	/**
	 * 转账URL
	 */
	@Value("${fund.refund.url}")
	private String url = "";
	
	/**
	 * 签名密钥
	 */
	@Value("${fund.refund.sign.secret.key}")
	private String signSecretKey = "";
	
	/**
	 * 接口名称
	 */
	@Value("${fund.refund.service}")
	private String service = "diect_refund_outer";
	/**
	 * 签名方式
	 */
	@Value("${fund.refund.sign.type}")
	private String signType = "MD5";
	
	/**
	 * 参数编码字符集
	 */
	@Value("${fund.refund.input.charset}")
	private String inputCharset = "gb2312";
	
	/**
	 * 客户端ip
	 */
	@Value("${fund.refund.client.ip}")
	private String clientIP = "127.0.0.1";
	
	/**
	 * 请求IP
	 */
	@Value("${fund.refund.request.ip}")
	private String requestIP = "127.0.0.1";
	
	/**
	 * 接口版本
	 */
	@Value("${fund.refund.version}")
	private String version = "1.0";
	
	/**
	 * 交易类型
	 */
	@Value("${fund.refund.trading.type}")
	private String tradingType = "3";

	/**
	 * 部门名称
	 */
	@Value("${fund.refund.dep.name}")
	private String depName = "";
	/**
	 * 接入方的部点id
	 */
	@Value("${fund.refund.create.internal.site.id}")
	private String createInterNalSiteId = "";
	/**
	 * 异步通知的回掉url
	 */
	@Value("${fund.refund.notify.url}")
	private String notifyURL = "";
	
	//private Map<String, String> params = null;
	
	public Map<String, String> getBaseParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", this.service);
		params.put("sign_type", this.signType);
		params.put("input_charset", this.inputCharset);
		params.put("client_ip", this.clientIP);
		params.put("request_ip", this.requestIP);
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
			this.params.put("request_ip", this.requestIP);
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
	
	
	
	
}