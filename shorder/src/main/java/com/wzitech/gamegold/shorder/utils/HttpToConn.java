package com.wzitech.gamegold.shorder.utils;

import com.wzitech.chaos.framework.server.common.ResponseStatus;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.entity.BaseResponse;
import com.wzitech.gamegold.common.enums.ResponseCodes;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by chengXY on 2017/8/21.
 */
@Component
public class HttpToConn {

    @Autowired
    private HttpClientPool httpClientPool;

    protected static final Log log = LogFactory.getLog(HttpToConn.class);

    public void httpPostReduce(String url, String params) throws IOException {
        String result = "";
        DataOutputStream out = null;
        BufferedReader reader = null;
        InputStream is = null;
        try {
            //发起请求
            URL url1 = new URL(url);
            HttpURLConnection urlcon = (HttpURLConnection) url1.openConnection();
            // http正文内，因此需要设为true
            urlcon.setDoOutput(true);
            // Read from the connection. Default is true.
            urlcon.setDoInput(true);
            // 默认是 GET方式
            urlcon.setRequestMethod("POST");

            // Post 请求不能使用缓存
            urlcon.setUseCaches(false);

            urlcon.setInstanceFollowRedirects(true);

            urlcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            urlcon.connect();//获取连接
            out = new DataOutputStream(urlcon.getOutputStream());
            // The URL-encoded contend
            // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
//             String content = params;
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            out.write(params.getBytes());
            is = urlcon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result = result + line;
            }
            log.info("請求7bao減款返回信息{}：" + result);
//            JSONObject jsonResult = JSONObject.fromObject(result);
//            String responseStatusStr = jsonResult.getString("responseStatus");
//            JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
//            ResponseStatus responseStatus = jsonMapper.fromJson(responseStatusStr, ResponseStatus.class);
//            if (!"00".equals(responseStatus.getCode())) {
//                throw new SystemException(responseStatus.getCode(), responseStatus.getMessage());
//            }

            JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
            BaseResponse baseResponse = jsonMapper.fromJson(result, BaseResponse.class);
            log.info("httpPostReduce,baseResponse," + baseResponse);
            if (baseResponse == null) {
                throw new SystemException(ResponseCodes.ResponseError.getCode(), ResponseCodes.ResponseError.getMessage());
            }
            ResponseStatus responseStatus = baseResponse.getResponseStatus();
            if (responseStatus == null || !responseStatus.getCode().equals("00")) {
                throw new SystemException(responseStatus.getCode(), responseStatus.getMessage());
            }
        } catch (Exception e) {
            log.error("发送的http请求出错:" + url);
            throw new SystemException("发送的http请求出错:" + url + "异常信息{}：" + e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (is != null){
                is.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        log.info("发送的http请求成功:" + url);
    }


    public JSONObject httpPost(String url, JSONObject jsonParam) {
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
                // multipart/form-data
                method.setEntity(entity);
            }
            //发起请求
            log.info("发送的http请求:" + url);
            result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");

            log.info("发送的http请求返回:" + result);
            //判断请求结果是否是200响应成功
//            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                //读取7bao接口返回过来的json字符串
                str = EntityUtils.toString(result.getEntity());
                //将json字符串转为json对象
                jsonResult = JSONObject.fromObject(str);
//            }
//            if (result.getStatusLine().getStatusCode() != 200) {
//                throw new SystemException("HTTP请求出错！！！");
//            }

        } catch (Exception e) {
            log.error("发送的http请求出错:" + url);
            throw new SystemException("发送的http请求出错:" + url);
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("发送的http请求成功:" + url);
        return jsonResult;
    }

    public JSONObject httpPost(String url, String jsonParam) {
        CloseableHttpClient httpClient = httpClientPool.getHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        HttpClientPool.config(method);
        //utf-8格式编码，防止乱码
        CloseableHttpResponse result = null;
        try {
            if (null != jsonParam) {
                StringEntity entity = new StringEntity(jsonParam, "utf-8");
                entity.setContentEncoding("utf-8");
                entity.setContentType("application/json");
                // multipart/form-data
                method.setEntity(entity);
            }
            //发起请求
            log.info("发送的http请求:" + url);
            result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");

            log.info("发送的http请求返回:" + result);
            //判断请求结果是否是200响应成功
//            if (result.getStatusLine().getStatusCode() == 200) {
            String str = "";
            //读取7bao接口返回过来的json字符串
            str = EntityUtils.toString(result.getEntity());
            //将json字符串转为json对象
            jsonResult = JSONObject.fromObject(str);
//            }
//            if (result.getStatusLine().getStatusCode() != 200) {
//                throw new SystemException("HTTP请求出错！！！");
//            }

        } catch (Exception e) {
            log.error("发送的http请求出错:" + url,e);
            throw new SystemException("发送的http请求出错:" + url,e);
        } finally {
            try {
                if (result != null)
                    result.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("发送的http请求成功:" + url);
        return jsonResult;
    }


    public String sendHttpPost(String url, JSONObject jsonParam) {
        CloseableHttpClient httpClient = httpClientPool.getHttpClient();
        JSONObject jsonResult = null;
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
            log.info("发送的http请求:" + url);
            result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");

            log.info("发送的http请求返回:" + result);
            //判断请求结果是否是200响应成功
            if (result.getStatusLine().getStatusCode() == 200) {
                //读取7bao接口返回过来的json字符串
                reponse = EntityUtils.toString(result.getEntity());
//                //将json字符串转为json对象
//                jsonResult = JSONObject.fromObject(str);
            }
            if (result.getStatusLine().getStatusCode() != 200) {
                log.error("发送的http请求返回:" + result.getStatusLine() + "," + EntityUtils.toString(result.getEntity()));
                throw new SystemException("HTTP请求出错！！！");
            }

        } catch (Exception e) {
            log.error("发送的http请求出错:" + url);
            throw new SystemException("发送的http请求出错:" + e);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("发送的http请求成功:" + url);
        return reponse;
    }

    public String sendHttpPost(String url, String jsonParam) throws IOException {
        String result = "";
        DataOutputStream out = null;
        BufferedReader reader = null;
        InputStream is = null;
        try {
            //发起请求
            URL url1 = new URL(url);
            HttpURLConnection urlcon = (HttpURLConnection) url1.openConnection();
            // http正文内，因此需要设为true
            urlcon.setDoOutput(true);
            // Read from the connection. Default is true.
            urlcon.setDoInput(true);
            // 默认是 GET方式
            urlcon.setRequestMethod("POST");

            // Post 请求不能使用缓存
            urlcon.setUseCaches(false);

            urlcon.setInstanceFollowRedirects(true);

            urlcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            urlcon.connect();//获取连接
            out = new DataOutputStream(urlcon.getOutputStream());
            // The URL-encoded contend
            // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
//             String content = params;
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            out.write(jsonParam.getBytes());
            is = urlcon.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e) {
            log.error("发送的http请求出错:" + url);
            throw new SystemException("发送的http请求出错:" + url + "异常信息：{}" + e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (is != null){
                is.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }
}
