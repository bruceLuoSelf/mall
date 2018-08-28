package com.wzitech.gamegold.facade.frontend.service.shorder.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.dto.RobotResponse;
import com.wzitech.gamegold.common.utils.SignHelper;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import com.wzitech.gamegold.facade.frontend.service.shorder.IOrderOutRobotService;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单出货
 * Created by 335854 on 2016/4/5.
 */
@Service("OrderOutService")
public class OrderOutServiceRobotImpl extends AbstractBaseService implements IOrderOutRobotService {
    /**
     * 设置失败重试次数
     */
    @Value("${httpclient.connection.maxretry}")
    private int maxRetry = 3;

    /**
     * 下载多线程管理器
     */
    private static PoolingClientConnectionManager connMger;

    /**
     * 下载参数
     */
    private static HttpParams connParams;

    /**
     * 设置每个路由最大连接数
     */
    @Value("${httpclient.connection.maxrouteconnection}")
    private int maxRouteConneciton = 50;

    /**
     * 设置最大连接数
     */
    @Value("${httpclient.connection.maxconnection}")
    private int maxConnection = 200;

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
     * 设置接收缓冲
     */
    @Value("${httpclient.connection.socketbuffersize}")
    private int sockeBufferSize = 8192;

    @Value("${shrobot.interface.url}")
    private String interfaceUrl = "";

    @Value("${shrobot.sign_key}")
    private String signKey = "";

    @Value("${shrobot.app_id}")
    private String appId = "";

    @Value("${shrobot.order.cancel.service}")
    private String orderCancelService;

    private JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    @PostConstruct
    private void afterInitialization() {
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

    /**
     * 出货方主动撤单、交易完成通知
     *
     * @param orderId
     * @return
     */
    @Override
    public void cancelGoodsReceipt(String orderId) {
        try {
            //参数
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = time.format(new Date());

            Map<String, String> map = new HashMap<String, String>();
            map.put("app_id", appId);
            map.put("method", orderCancelService);
            map.put("timestamp", dateTime);
            map.put("order_id", orderId);

            //请求url
            String url = SignHelper.formatURL(interfaceUrl, map, signKey, "UTF-8");
            logger.info("生成提前结束收货单接口URL:{}", url);

            //机器人交互撤单
            HttpClient httpClient = getHttpClient();
            HttpGet getMethod = new HttpGet(url);
            HttpResponse response = httpClient.execute(getMethod);
            String responseStr = StreamIOHelper.inputStreamToStr(response.getEntity().getContent(), "UTF-8");
            logger.info("提前结束收货单接口返回数据:{}", responseStr);

            //处理机器人返回信息
            RobotResponse robotResponse = jsonMapper.fromJson(responseStr, RobotResponse.class);
            if ("error".equals(robotResponse.getStatus())) {
                logger.error("出货方主动撤单失败,orderId:{},code:{},msg:{}",
                        new Object[]{orderId, robotResponse.getCode(), robotResponse.getMessage()});
            }
        } catch (SystemException e) {
            logger.error("出货方主动撤单发生异常:{}", e);
        } catch (Exception e) {
            // 捕获未知异常
            logger.error("出货方主动撤单发生未知异常:{}", e);
        }
    }

    public HttpClient getHttpClient() {
        DefaultHttpClient client = new DefaultHttpClient(connMger, connParams);
        client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(maxRetry, false));
        return client;
    }

}
