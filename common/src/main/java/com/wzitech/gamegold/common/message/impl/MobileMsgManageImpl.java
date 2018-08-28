/************************************************************************************
 * Copyright 2014 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		EUCPMobileMsgImpl
 * 包	名：		com.wzitech.gamegold.common.message.impl
 * 项目名称：	gamegold-common
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-9
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-9 下午12:19:03
 ************************************************************************************/
package com.wzitech.gamegold.common.message.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.security.Digests;
import com.wzitech.chaos.framework.server.common.security.HexBinrary;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.httpClientPool.HttpClientPool;
import com.wzitech.gamegold.common.message.IMobileMsgManager;
import com.wzitech.gamegold.common.message.dto.SendMobileMsgResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URLEncoder;

/**
 * 亿美短信(EUCP)
 *
 * @author SunChengfei
 *
 */
@Component("eucpMobileMsgImpl")
public class MobileMsgManageImpl extends AbstractBusinessObject implements IMobileMsgManager {


    @Autowired
    private HttpClientPool httpClientPool;
    /**
     * 客户端IP
     */
    @Value("${message.client.ip}")
    private String clientIP = "112.65.128.122";

    /**
     * 客户端key
     */
    @Value("${message.client.key}")
    private String key = "@dada#";

    /**
     * 短信类型
     */
    @Value("${message.client.category}")
    private String category = "7035";

    /**
     * web服务请求url
     */
    @Value("${message.client.requesturl}")
    private String requestUrl = "http://mobile.5173.com/MobileAPI/SendSingleMessage?" +
            "m_sign=%s&m_clientIP=%s&category=%s&mobile=%s&content=%s";

    /**
     * 签名
     */
    private String sign;

    private CloseableHttpClient httpClient;

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @PostConstruct
    private void afterInitialization() throws Exception {
        // 临时解决方案，根据内网IP判断该使用哪个公网IP
        InetAddress ia = InetAddress.getLocalHost();
        clientIP = ia.getHostAddress();
        logger.info("当前服务器IP：{}", clientIP);

        String str = key + clientIP;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes("gb2312"));
        byte[] byteArray = Digests.md5(inputStream);
        sign = HexBinrary.encodeHexBinrary(byteArray).toLowerCase();
        logger.debug("5173短信平台客户端clientIP:{},key:{},category:{},weburl:{},sign:{}",
                new Object[]{clientIP, key, category, requestUrl, sign});

//		httpClient = httpClientPool.getHttpClient();
    }

    /*
     * (non-Javadoc)
     * @see com.wzitech.shuabao.common.message.IMobileMsgManager#sendMessage(java.lang.String, java.lang.String)
     */
    @Override
    public void sendMessage(String mobilePhoneNub, String context) throws Exception {
        CloseableHttpResponse response = null;
        InputStream content = null;
        try {
            httpClient = httpClientPool.getHttpClient();
            String request = String.format(requestUrl, sign, clientIP, category, mobilePhoneNub, URLEncoder.encode("【5173网】" + context, "UTF-8"));
            logger.info("5173短信平台发送URL:{}", request);
            HttpGet httpGet = new HttpGet(request);
            HttpClientPool.config(httpGet);
            response = httpClient.execute(httpGet);
            content = response.getEntity().getContent();
            String json = IOUtils.toString(content);
            logger.info("5173短信平台发送给手机终端{}结果：{}", new Object[]{mobilePhoneNub, json});
            SendMobileMsgResponse msgResponse = jsonMapper.fromJson(json, SendMobileMsgResponse.class);
            if (msgResponse.getResultNo() != null && msgResponse.getResultNo() != 0) {
                throw new SystemException(ResponseCodes.SendMessageErr.getCode(), msgResponse.getResultDescription());
            }
        } catch (Exception ex) {
            logger.error("5173短信平台发送给手机终端发生异常：{}", new Object[]{ex});
        } finally {
            if (content != null) {
                content.close();
            }
            if (response != null) {
                response.close();
            }
        }
    }
}
