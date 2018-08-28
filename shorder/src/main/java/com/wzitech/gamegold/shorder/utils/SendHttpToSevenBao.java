package com.wzitech.gamegold.shorder.utils;

import com.wzitech.gamegold.common.httpClientPool.HttpClientPool;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by chengXY on 2017/8/21.
 */
@Component
public class SendHttpToSevenBao implements ISendHttpToSevenBao {
    protected static final Log log = LogFactory.getLog(SendHttpToSevenBao.class);
    @Autowired
    private HttpClientPool httpClientPool;

    /**
     * post请求
     * 参数：url请求地址
     * jsonParam
     */
    public JSONObject httpPost(String url, JSONObject jsonParam) {
        log.info("请求7bao地址{}" + url);
        CloseableHttpClient httpClient = httpClientPool.getHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        HttpClientPool.config(method);
        //utf-8格式编码，防止乱码
        CloseableHttpResponse result = null;
        try {
            if (null != jsonParam) {
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("utf-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            //发起请求
            result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");

            //判断请求结果是否是200响应成功
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                //读取7bao接口返回过来的json字符串
                str = EntityUtils.toString(result.getEntity());
                log.info("发送至7bao返回信息：" + str);
                //将json字符串转为json对象
                jsonResult = JSONObject.fromObject(str);
            }
            log.info("发送至7bao返回statusCode：" + result.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发送至7bao的http请求出错：{}", e);
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("发送至7bao的http请求成功");
        return jsonResult;
    }

    /**
     * post请求
     * 参数：url请求地址
     * jsonParam
     */
    public String sendHttpPost(String url, JSONObject jsonParam) {
        log.info("请求7bao地址{}" + url);
        CloseableHttpClient httpClient = httpClientPool.getHttpClient();
        HttpPost method = new HttpPost(url);
        HttpClientPool.config(method);
        //utf-8格式编码，防止乱码
        CloseableHttpResponse result = null;
        String reponse = "";
        try {
            if (null != jsonParam) {
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("utf-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            //发起请求
            result = httpClient.execute(method);

            //判断请求结果是否是200响应成功
            if (result.getStatusLine().getStatusCode() == 200) {
                //读取7bao接口返回过来的json字符串
                reponse = EntityUtils.toString(result.getEntity());
                log.info("发送至7bao返回信息：" + reponse);
            }
            log.info("发送至7bao返回statusCode：" + result.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
            log.info("发送至7bao的http请求出错：{}", e);
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("发送至7bao的http请求成功");
        return reponse;
    }
}

