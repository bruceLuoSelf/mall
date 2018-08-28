package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.gamegold.common.httpClientPool.HttpClientPool;
import com.wzitech.gamegold.repository.business.IHttpToSevenBao;
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
 * Created by 340032 on 2017/8/26.
 */
@Component
public class HttpToSevenBao implements IHttpToSevenBao {

    @Autowired
    private HttpClientPool httpClientPool;
    protected static final Log log = LogFactory.getLog(HttpToSevenBao.class);
    @Override
    public JSONObject httpPost(String url, JSONObject jsonParam) {

        CloseableHttpClient httpClient = httpClientPool.getHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        HttpClientPool.config(method);
        CloseableHttpResponse result = null;
        //utf-8格式编码，防止乱码
        try {
            if (null != jsonParam) {
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("utf-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            //发起请求
            result = httpClient.execute(method);
            String resultStr = EntityUtils.toString(result.getEntity());
            log.info("发送至7bao的http请求成功返回信息："+ resultStr);
            url = URLDecoder.decode(url, "UTF-8");

            //判断请求结果是否是200响应成功
            if (result.getStatusLine().getStatusCode() == 200){
                //读取7bao接口返回过来的json字符串
                //将json字符串转为json对象
                jsonResult = JSONObject.fromObject(resultStr);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.info("发送至7bao的http请求出错:{}",e);
        }finally {
            try {
                if (result != null)
                    result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonResult;
    }

    @Override
    public String sendHttpPost(String url, JSONObject jsonParam) {
        CloseableHttpClient httpClient = httpClientPool.getHttpClient();
        HttpPost method = new HttpPost(url);
        HttpClientPool.config(method);
        CloseableHttpResponse result = null;
        String reponse = "";
        //utf-8格式编码，防止乱码
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
            if (result.getStatusLine().getStatusCode() == 200){
                reponse = EntityUtils.toString(result.getEntity());
                log.info("发送至7bao的http请求成功返回信息："+ reponse);
//                url = URLDecoder.decode(url, "UTF-8");
//                //读取7bao接口返回过来的json字符串
//                //将json字符串转为json对象
//                jsonResult = JSONObject.fromObject(resultStr);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.info("发送至7bao的http请求出错:{}",e);
        }finally {
            try {
                if (result != null)
                    result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reponse;
    }

}
