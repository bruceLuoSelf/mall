package com.wzitech.gamegold.common.userinfo.impl;

import com.wzitech.chaos.framework.server.common.security.Base64;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.userinfo.IUserInfoService;
import com.wzitech.gamegold.common.userinfo.entity.UserInfo;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
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
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * 获取5173用户信息
 * @author yemq
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {
    /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

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

    @Value("${userInfo.getUserInfo.request}")
    private String userInfoRequest="";

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

    /**
     * 获取用户信息
     *
     * @param uid
     * @return
     */
    @Override
    public UserInfo getUserInfo(String uid) throws IOException {
        String requestUrl = String.format(userInfoRequest, uid);
        HttpResponse response = getHttpClient().execute(new HttpGet(requestUrl));
        InputStream content = null;
        String responseStr = null;
        try {
            content = response.getEntity().getContent();
            responseStr = StreamIOHelper.inputStreamToStr(content, "UTF-8");
        }finally {
            if(content!= null){
                content.close();
            }
        }
        logger.info("5173获取用户信息请求url:{},返回:{}", new Object[]{requestUrl, responseStr});
        // 解析json
        UserInfo userInfo = jsonMapper.fromJson(responseStr, UserInfo.class);
        if (userInfo != null) {
            if (StringUtils.isNotBlank(userInfo.getRealName())) {
                String realName = new String(Base64.decodeBase64Binrary(userInfo.getRealName()), "UTF-16LE");
                userInfo.setRealName(realName);
            }
            if (StringUtils.isNotBlank(userInfo.getIdCard())) {
                String idCard = new String(Base64.decodeBase64Binrary(userInfo.getIdCard()), "UTF-16LE");
                userInfo.setIdCard(idCard);
            }
        }

        return userInfo;
    }


}
