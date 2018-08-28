/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GameUserManagerImpl
 *	包	名：		com.wzitech.gamegold.common.usermgmt.impl
 *	项目名称：	gamegold-common
 *	作	者：		SunChengfei
 *	创建时间：	2014-2-20
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-2-20 下午4:51:47
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.common.usermgmt.impl;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.usermgmt.IGameUserManager;
import com.wzitech.gamegold.common.usermgmt.entity.GameUserInfo;
import com.wzitech.gamegold.common.usermgmt.entity.PassPortResponse;
import com.wzitech.gamegold.common.usermgmt.entity.PassportTicket;

import javax.annotation.PostConstruct;

/**
 * 5173注册用户管理
 * @author SunChengfei
 *
 */
@Component
public class GameUserManagerImpl extends AbstractBusinessObject implements IGameUserManager {
    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(GameUserManagerImpl.class);

    /**
     * 设置连接超时时间
     */
    @Value("${httpclient.connection.timeout}")
    private int connectionTimeout = 30000;

    /**
     * 设置读取超时时间
     */
    @Value("${httpclient.connection.sotimeout}")
    private int SoTimeout = 120000;

    /**
     * 设置最大连接数
     */
    @Value("${httpclient.connection.maxconnection}")
    private int maxConnection = 200;

    /**
     * 设置每个路由最大连接数
     */
    @Value("${httpclient.connection.maxrouteconnection}")
    private int maxRouteConneciton = 50;

    /**
     * 设置接收缓冲
     */
    @Value("${httpclient.connection.socketbuffersize}")
    private int sockeBufferSize = 8192;

    /**
     * 设置失败重试次数
     */
    @Value("${httpclient.connection.maxretry}")
    private int maxRetry = 5;

    /**
     * 下载多线程管理器
     */
    private static PoolingClientConnectionManager connMger;

    /**
     * 下载参数
     */
    private static HttpParams connParams;


    private JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();
	
	@Value("${passport.analysisCookie.request}")
	private String analysisCookieRequest="";

    @PostConstruct
    private void afterInitialization(){
        // 初始化下载线程池
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(
                new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        connMger = new PoolingClientConnectionManager(schemeRegistry);
        connMger.setDefaultMaxPerRoute(maxRouteConneciton);
        connMger.setMaxTotal(maxConnection);

        // 初始化下载参数
        connParams = new BasicHttpParams();
        connParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectionTimeout);
        connParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, SoTimeout);
        connParams.setParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, sockeBufferSize);
    }

    public HttpClient getHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient(connMger, connParams);
        client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(maxRetry, false));
        return client;
    }

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.usermgmt.IGameUserManager#analysisCookie(java.lang.String)
	 */
	@Override
	public GameUserInfo analysisCookie(String cookie) throws ClientProtocolException, IOException, SystemException {
		if(StringUtils.isEmpty(cookie)){
			logger.debug("请求未包含cookie，返回null");
			return null;
		}
		String requestUrl = String.format(analysisCookieRequest, cookie);
		HttpResponse response = getHttpClient().execute(new HttpGet(requestUrl));
		String responseStr = IOUtils.toString(response.getEntity().getContent());
		logger.debug("5173解析cookie请求url:{},返回:{}", new Object[]{requestUrl, responseStr});
		
		// 解析json
		PassPortResponse passPortResponse = jsonMapper.fromJson(responseStr, PassPortResponse.class);
		// 请求成功
		if(passPortResponse.getResultNo() == 0 && passPortResponse.getTicket() != null){
			
			 PassportTicket passportTicket = passPortResponse.getTicket();
			 
			 return new GameUserInfo(passportTicket.getUserID(), passportTicket.getUserName());
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wzitech.gamegold.common.usermgmt.IGameUserManager#getUserById(java.lang.Long)
	 */
	@Override
	public GameUserInfo getUserById(Long uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
