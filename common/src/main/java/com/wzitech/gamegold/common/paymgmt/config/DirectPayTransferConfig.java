package com.wzitech.gamegold.common.paymgmt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共资金服务通用付款接口配置
 *
 * @author yemq
 */
@Component
public class DirectPayTransferConfig {
    /**
     * 转账URL
     */
    @Value("${fund.direct_pay_transfer.url}")
    private String url = "";

    @Value("${fund.direct_pay_transfer.sign.secret.key}")
    private String signSecretKey = "";

    /**
     * 接口名称
     */
    @Value("${fund.direct_pay_transfer.service}")
    private String service = "";

    /**
     * 签名方式
     */
    @Value("${fund.direct_pay_transfer.sign.type}")
    private String signType = "MD5";

    @Value("${fund.direct_pay_transfer.input.charset}")
    private String inputCharset = "gb2312";

    @Value("${fund.direct_pay_transfer.client.ip}")
    private String clientIP = "";

    @Value("${fund.direct_pay_transfer.version}")
    private String version = "1.0";

    @Value("${fund.direct_pay_transfer.trading.type}")
    private String tradingType = "3";

    @Value("${fund.direct_pay_transfer.buyer_id}")
    private String buyerId = "";

    @Value("${fund.direct_pay_transfer.buyer_name}")
    private String buyerName = "";

    @Value("${fund.direct_pay_transfer.platform_lang}")
    private String platformLang = "java";

    @Value("${fund.direct_pay_transfer.dep.name}")
    private String depName = "";

    @Value("${fund.direct_pay_transfer.create.internal.site.id}")
    private String createInterNalSiteId = "";

    @Value("${fund.direct_pay_transfer.notify.url}")
    private String notifyURL = "";

    @Value("${fund.direct_pay_transfer.action_type}")
    private String actionType = "";

    @Value("${fund.direct_pay_transfer.allow_original_back}")
    private String allowOriginalBack = "";

    @Value("${fund.direct_pay_transfer.source_type}")
    private String sourceType;

    public Map<String, String> getBaseParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("service", this.service);
        params.put("sign_type", this.signType);
        params.put("input_charset", this.inputCharset);
        params.put("client_ip", this.clientIP);
        params.put("version", this.version);
        params.put("trading_type", this.tradingType);
        params.put("buyer_id", this.buyerId);
        params.put("buyer_name", this.buyerName);
        params.put("platform_lang", this.platformLang);
        params.put("dep_name", this.depName);
        params.put("create_internal_site_id", this.createInterNalSiteId);
        params.put("notify_url", this.notifyURL);
        params.put("action_type", this.actionType);
        params.put("allow_original_back", this.allowOriginalBack);
        params.put("source_type", this.sourceType);
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

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getAllowOriginalBack() {
        return allowOriginalBack;
    }

    public void setAllowOriginalBack(String allowOriginalBack) {
        this.allowOriginalBack = allowOriginalBack;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
