package com.wzitech.gamegold.common.paymgmt.config;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wzitech.gamegold.common.utils.PayHelper;

/**
 * 5173支付接口配置信息
 *
 * @author yunhai.Wu
 */
@Component
public class PaymentConfig {
    /**
     * 支付接口URL
     */
    @Value("${fund.payment.url}")
    private String url = "";

    /**
     * m站支付接口URL
     */
    @Value("${fund.mpayment.url}")
    private String murl = "";

    public String getMurl() {
        return murl;
    }

    public void setMurl(String murl) {
        this.murl = murl;
    }

    @Value("${fund.payment.sign.secret.key}")
    private String signSecretKey = "";

    /**
     * 5173密钥
     */
    @Value("${fund.payment.sign.encret.key}")
    private String singEncretKey = "";

    /**
     * 接口名称
     */
    @Value("${fund.payment.service}")
    private String service = "direct_pay_order_by_user_outer";

    /**
     * 签名方式
     */
    @Value("${fund.payment.sign.type}")
    private String signType = "MD5";

    /**
     * 参数编码字符集
     */
    @Value("${fund.payment.input.charset}")
    private String inputCharset = "gb2312";

    @Value("${fund.payment.client.ip}")
    private String clientIP = "127.0.0.1";
    /**
     * 接口版本号
     */
    @Value("${fund.payment.version}")
    private String version = "1.3";

    @Value("${fund.payment.type}")
    private String paymentType = "3";

    /**
     * 交易类型
     */
    @Value("${fund.payment.trading.type}")
    private String tradingType = "2";

    /**
     * m部门名称
     */
    @Value("${fund.mpayment.dep.name}")
    private String mdepName = "";

    /**
     * 部门名称
     */
    @Value("${fund.payment.dep.name}")
    private String depName = "";

    /**
     * 接入方的部点id
     */
    @Value("${fund.payment.create.internal.site.id}")
    private String createInterNalSiteId = "";

    /**
     * 订单创建ip
     * 1.2 之后版本使用
     */
    @Value("${fund.payment.create.trade.no.ip}")
    private String createTradeNoIP = "";

    /**
     * 交易方式
     */
    @Value("${fund.payment.trade.method}")
    private String tradingMethod = "CurrentTrade";

    /**
     * 异步回调URL
     */
    @Value("${fund.payment.notify.url}")
    private String notifyURL = "";

    /**
     * 完成支付后调用URL
     */
    @Value("${fund.payment.return.url}")
    private String returnURL = "";

    /**
     * 付款通知处理完后给用户显示的URL地址
     */
    @Value("${fund.payment.redirect.url}")
    private String redirectURL = "";

    /**
     * 付款通知处理完后给用户显示的URL地址,m站的回调地址
     */
    @Value("${fund.mpayment.redirect.url}")
    private String redirectMURL = "";

    public String getRedirectMURL() {
        return redirectMURL;
    }

    public void setRedirectMURL(String redirectMURL) {
        this.redirectMURL = redirectMURL;
    }

    /**
     * 收货系统-异步回调URL
     */
    @Value("${fund.payment.sh.notify.url}")
    private String shNotifyURL = "";

    /**
     * 收货系统-完成支付后调用URL
     */
    @Value("${fund.payment.sh.return.url}")
    private String shReturnURL = "";

    /**
     * 收货系统-付款通知处理完成后给用户显示的URL地址
     */
    @Value("${fund.payment.sh.redirect.url}")
    private String shRedirectURL = "";


    public Map<String, String> getMBaseParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("service", this.service);
        params.put("sign_type", this.signType);
        params.put("input_charset", this.inputCharset);
        params.put("client_ip", String.valueOf(PayHelper.ipToLong(this.clientIP)));
//		params.put("client_ip", String.valueOf(PayHelper.ipToLong("192.168.40.244")));
        params.put("version", this.version);
        params.put("payment_type", this.paymentType);
        params.put("trading_type", this.tradingType);
        params.put("dep_name", this.mdepName);
//        params.put("dep_name", this.mdepName);
        params.put("create_internal_site_id", this.createInterNalSiteId);
        params.put("create_trade_no_ip", String.valueOf(PayHelper.ipToLong(this.createTradeNoIP)));
//		params.put("create_trade_no_ip", String.valueOf(PayHelper.ipToLong("192.168.40.244")));
        params.put("trading_method", this.tradingMethod);
        params.put("notify_url", this.notifyURL);
        params.put("return_url", this.returnURL);
        return params;
    }

    //private Map<String, String> params = null;

    public Map<String, String> getBaseParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("service", this.service);
        params.put("sign_type", this.signType);
        params.put("input_charset", this.inputCharset);
        params.put("client_ip", String.valueOf(PayHelper.ipToLong(this.clientIP)));
        params.put("version", this.version);
        params.put("payment_type", this.paymentType);
        params.put("trading_type", this.tradingType);
        params.put("dep_name", this.depName);
        params.put("create_internal_site_id", this.createInterNalSiteId);
        params.put("create_trade_no_ip", String.valueOf(PayHelper.ipToLong(this.createTradeNoIP)));
        params.put("trading_method", this.tradingMethod);
        params.put("notify_url", this.notifyURL);
        params.put("return_url", this.returnURL);
        return params;
        /*if (this.params == null) {
			this.params = new HashMap<String, String>();
			this.params.put("service", this.service);
			this.params.put("sign_type", this.signType);
			this.params.put("input_charset", this.inputCharset);
			this.params.put("client_ip", String.valueOf(PayHelper.ipToLong(this.clientIP)));
			this.params.put("version", this.version);
			this.params.put("payment_type", this.paymentType);
			this.params.put("trading_type", this.tradingType);
			this.params.put("dep_name", this.depName);
			this.params.put("create_internal_site_id", this.createInterNalSiteId);
			this.params.put("create_trade_no_ip", String.valueOf(PayHelper.ipToLong(this.createTradeNoIP)));
			this.params.put("trading_method", this.tradingMethod);
			this.params.put("notify_url", this.notifyURL);
			this.params.put("return_url", this.returnURL);
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

    /**
     * @return the singEncretKey
     */
    public String getSingEncretKey() {
        return singEncretKey;
    }

    /**
     * @param singEncretKey the singEncretKey to set
     */
    public void setSingEncretKey(String singEncretKey) {
        this.singEncretKey = singEncretKey;
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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public String getCreateTradeNoIP() {
        return createTradeNoIP;
    }

    public void setCreateTradeNoIP(String createTradeNoIP) {
        this.createTradeNoIP = createTradeNoIP;
    }

    public String getTradingMethod() {
        return tradingMethod;
    }

    public void setTradingMethod(String tradingMethod) {
        this.tradingMethod = tradingMethod;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public String getShRedirectURL() {
        return shRedirectURL;
    }

    public void setShRedirectURL(String shRedirectURL) {
        this.shRedirectURL = shRedirectURL;
    }

    public String getShNotifyURL() {
        return shNotifyURL;
    }

    public void setShNotifyURL(String shNotifyURL) {
        this.shNotifyURL = shNotifyURL;
    }

    public String getShReturnURL() {
        return shReturnURL;
    }

}