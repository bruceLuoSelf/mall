package com.wzitech.gamegold.common.paymgmt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 外部部分转账接口
 * @author yemq
 */
@Component
public class DirectPartialTransferConfig {
    /**
     * 转账URL
     */
    @Value("${fund.direct_partial_transfer_outer.url}")
    private String url = "";

    @Value("${fund.direct_partial_transfer_outer.sign.secret.key}")
    private String signSecretKey = "";

    /**
     * 接口名称
     */
    @Value("${fund.direct_partial_transfer_outer.service}")
    private String service = "";

    /**
     * 签名方式
     */
    @Value("${fund.direct_partial_transfer_outer.sign.type}")
    private String signType = "MD5";

    @Value("${fund.direct_partial_transfer_outer.input.charset}")
    private String inputCharset = "gb2312";

    @Value("${fund.direct_partial_transfer_outer.client.ip}")
    private String clientIP = "";

    @Value("${fund.direct_partial_transfer_outer.version}")
    private String version = "1.0";

    @Value("${fund.direct_partial_transfer_outer.trading.type}")
    private String tradingType = "6";

    @Value("${fund.direct_partial_transfer_outer.request_ip}")
    private String requestIp = "";

    @Value("${fund.direct_partial_transfer_outer.is_op}")
    private String isOp = "0";

    @Value("${fund.direct_partial_transfer_outer.op_name}")
    private String opName = "";

    @Value("${fund.direct_partial_transfer_outer.dep.name}")
    private String depName = "";

    @Value("${fund.direct_partial_transfer_outer.create.internal.site.id}")
    private String createInterNalSiteId = "";

    @Value("${fund.direct_partial_transfer_outer.notify.url}")
    private String notifyURL = "";

    public Map<String, String> getBaseParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("service", this.service);
        params.put("sign_type", this.signType);
        params.put("input_charset", this.inputCharset);
        params.put("client_ip", this.clientIP);
        params.put("version", this.version);
        params.put("trading_type", this.tradingType);
        params.put("is_op", this.isOp);
        params.put("op_name", this.opName);
        params.put("dep_name", this.depName);
        params.put("create_internal_site_id", this.createInterNalSiteId);
        params.put("notify_url", " ");
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

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getIsOp() {
        return isOp;
    }

    public void setIsOp(String isOp) {
        this.isOp = isOp;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
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
