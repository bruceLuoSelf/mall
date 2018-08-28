package com.wzitech.gamegold.common.main;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.BusinessException;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.httpClientPool.HttpClientPool;
import com.wzitech.gamegold.common.redis.IRedisCommon;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by chengXY on 2017/8/29.
 * 请求主站方法封装
 */
@Component
public class MainStationManagerImpl extends AbstractBaseService implements IMainStationManager {
    @Value("${main.APP_ID}")
    private String APP_ID;

    @Value("${main.KAYVALUE}")
    private String KAYVALUE;

    @Autowired
    private IRedisCommon redisCommon;

    @Autowired
    private HttpClientPool httpClientPool;

    /**
     * 步骤1、获取授权
     */
    public SRRequsestTokenResponse GetRequestToken2() {
//        logger.info(LogCategory.SYSTEM,"获取授权开始");
        CloseableHttpResponse response = null;
        InputStream is = null;
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        long timeNow = System.currentTimeMillis();
//        logger.info(LogCategory.SYSTEM,"时间戳为:{}", timeNow);
        String sign = MainStationUtil.ComputeSignature(SpitConstant.getBaseSign(APP_ID) + "&" + MainStationConstant.RESULT_MD5 + "&" + timeNow
                , KAYVALUE, "");
        String url = MainStationConstant.URL_REQUESTTOKEN + SpitConstant.getRequestStr(APP_ID) + "&timestamp=" + timeNow + "&sign=" + sign;
        CloseableHttpClient httpclient = httpClientPool.getHttpClient();
        logger.info(url);
        HttpGet get = new HttpGet(url);
        HttpClientPool.config(get);
        get.setHeader(MainStationConstant.HEADER_CONTENT_ENCODING, MainStationConstant.CONTANT_CODE);
        get.setHeader(MainStationConstant.HEADER_CONTENT_TYPE, MainStationConstant.CONTANT_CONTENT_TYPE);
        try {
            response = httpclient.execute(get);
            logger.info("主站返回http状态码：{}", response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
                String result = MainStationConstant.inStream2String(is);
//                logger.info(LogCategory.SYSTEM,"result为:{}", result);
//                SRRequsestTokenResponse token= MainStationConstant.JSONToObj(result,SRRequsestTokenResponse.class);
                SRRequsestTokenResponse token = JsonMapper.nonEmptyMapper().fromJson(result, SRRequsestTokenResponse.class);
//                logger.info(LogCategory.SYSTEM,"获取授权成功: "+ ToStringBuilder.reflectionToString(token));
                return token;
            }
        } catch (Exception e) {
            //logger.info(LogCategory.SYSTEM,"时间戳为:{}", timeNow);
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 步骤2、获取令牌
     */
    public SRRequsestTokenResponse GetAccess() {
        logger.info("获取令牌开始");
        SRRequsestTokenResponse srrt = GetRequestToken2();//调用获取授权方法
        System.out.println(srrt);
        String requesttoken = srrt.getBizResult().getRequestToken();
        String rquestSecret = srrt.getBizResult().getRequestSecret();
        CloseableHttpResponse response = null;
        InputStream is = null;

        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        long timeNow = System.currentTimeMillis();
        try {

            String sign = MainStationUtil.ComputeSignature(SpitConstant.getBaseSign(APP_ID) + "&" + requesttoken
                    + "&" + MainStationConstant.RESULT_MD5 + "&" + timeNow, KAYVALUE, rquestSecret);

            String urlStr = MainStationConstant.URL_ACCESS + SpitConstant.getRequestStr(APP_ID) + "&timestamp=" + URLEncoder.encode(String.valueOf(timeNow)) + "&requesttoken="
                    + URLEncoder.encode(String.valueOf(requesttoken)) + "&sign=" + URLEncoder.encode(sign);

            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);

            CloseableHttpClient httpclient = httpClientPool.getHttpClient();

            logger.info("HttpClient");
            HttpGet get = new HttpGet(uri);
            HttpClientPool.config(get);
            get.setHeader(MainStationConstant.HEADER_CONTENT_ENCODING, MainStationConstant.CONTANT_CODE);
            get.setHeader(MainStationConstant.HEADER_CONTENT_TYPE, MainStationConstant.CONTANT_CONTENT_TYPE);
            response = httpclient.execute(get);
            logger.info("HttpResponse结束");
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                is = response.getEntity().getContent();
                String result = MainStationConstant.inStream2String(is);
                logger.info("URL结束" + result);
                SRRequsestTokenResponse token = JsonMapper.nonEmptyMapper().fromJson(result, SRRequsestTokenResponse.class);
                return token;
            }
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException异常{}", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception异常{}", e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info("获取令牌结束");
        return null;
    }

    public String GetMainRest(String uri, String authVers, String format, String method, String jsonParaInfo
            , String signMethod, String token, String vers) {
        logger.info("获取主站调用接口通用方法开始");
        String result = null;
        try {
            MainStationKeyEO srrt = GetAccessToken();//调用获取授权方法
            if (srrt == null) {
                logger.info("授权失败{}", srrt);
                return "";
            }

            String accesstoken = srrt.getAccessToken();
            String accessSecret = srrt.getAccessSecret();
            TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
            TimeZone.setDefault(tz);
            long timeNow = System.currentTimeMillis();
            String paraInfo = URLEncoder.encode(jsonParaInfo);
            String fields = "";
            String clientIP = IpContext.getCurrentContext().getIp();
            if (StringUtils.isBlank(clientIP)) {
                clientIP = MainStationUtil.getLocalIpAddress();
            }
            String baseSign = String.format(accesstoken + "&" + APP_ID + "&" + authVers + "&" + clientIP + "&" + fields + "&" + format + "&" +
                    method + "&" + jsonParaInfo + "&" + signMethod + "&" + timeNow + "&" + token + "&GET:" + uri + "&" + vers);
            String sign = MainStationUtil.ComputeSignature(baseSign, KAYVALUE, accessSecret);

            try {
                Map<String, String> queryMap = new HashMap<String, String>();
                queryMap.put("accesstoken", accesstoken);
                queryMap.put("appid", APP_ID);
                queryMap.put("authVers", authVers);
                queryMap.put("clientIP", clientIP);
                queryMap.put("fields", fields);
                queryMap.put("format", format);
                queryMap.put("method", method);
                queryMap.put("paraInfo", paraInfo);
                queryMap.put("signmethod", signMethod);
                queryMap.put("timestamp", String.valueOf(timeNow));
                queryMap.put("token", token);
                queryMap.put("vers", vers);
                queryMap.put("sign", sign);
                String url = MainStationUtil.keyValueJoin(uri + "?", queryMap, "&");
                System.out.println(url);
                logger.info("金币商城请求用主站接口调用url{}", url);
                result = MainStationUtil.loadJSON(url);
                if (result.indexOf("AccessToken is not exist or expired") > 0)//发现AccessToken 过期 重新请求
                    result = GetMainRestToNew(uri, authVers, format, method, jsonParaInfo
                            , signMethod, token, vers);
                logger.info("调用主站接口:{}", result);
            } catch (Exception ex) {
                logger.error("请求主站接口: " + method + ",参数: " + jsonParaInfo + ", 错误信息: " + result, ex);
            }
            //region 请求记录 记录错误日志   抛出BusinessException
            JSONObject mainObjStr = JSONObject.fromObject(result);

            String status = "";
            String feedBackResult = "";
            if (mainObjStr != null) {
                status = mainObjStr.get("Status").toString();//主站响应信息状态 非0 都为不成功   0为成功
                feedBackResult = mainObjStr.get("FeedBackResult").toString();
            } else {
                logger.error("请求主站接口: {},参数: {}, 错误信息: {}", new String[]{method, jsonParaInfo, result});
                try {
                    throw new BusinessException("请求主站接口响应结果为空");
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
            if (!status.equals("0")) {
                logger.error("请求主站接口: {},参数: {}, 错误信息: {}", new String[]{method, jsonParaInfo, feedBackResult});
                try {
                    throw new BusinessException("主站请求错误信息" + feedBackResult);
                } catch (BusinessException e) {
                    e.printStackTrace();
                }
            }
            logger.info("获取主站调用接口通用方法结束");
        } catch (Exception ex) {
            logger.error("获取主站调用接口通用方法发生异常，{}", ex);
        }
        return result;
    }

    public MainStationKeyEO GetAccessToken() {
        MainStationKeyEO mainStationKeyEO = redisCommon.queryUserThough();
        if (mainStationKeyEO == null || StringUtils.isBlank(mainStationKeyEO.getAccessToken()) || StringUtils.isBlank(mainStationKeyEO.getAccessSecret())) {
            mainStationKeyEO = new MainStationKeyEO();
            SRRequsestTokenResponse srrt = GetAccess();//调用获取授权方法
            mainStationKeyEO.setAccessSecret(srrt.getBizResult().getAccessSecret());
            mainStationKeyEO.setAccessToken(srrt.getBizResult().getAccessToken());
            redisCommon.addUserThough(mainStationKeyEO);
        }
        return mainStationKeyEO;
    }

    private String GetMainRestToNew(String uri, String authVers, String format, String method, String jsonParaInfo
            , String signMethod, String token, String vers) {
        logger.info("new获取主站调用接口通用方法开始");
        SRRequsestTokenResponse srrt = GetAccess();//调用获取授权方法
        if (srrt == null) {
            logger.info("new授权失败{}", srrt);
            return "";
        }
        String accesstoken = srrt.getBizResult().getAccessToken();
        String accessSecret = srrt.getBizResult().getAccessSecret();

        MainStationKeyEO mainStationKeyEO = new MainStationKeyEO();
        mainStationKeyEO.setAccessSecret(accessSecret);
        mainStationKeyEO.setAccessToken(accesstoken);
        redisCommon.addUserThough(mainStationKeyEO);

        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        long timeNow = System.currentTimeMillis();
        String paraInfo = URLEncoder.encode(jsonParaInfo);
        String fields = "";
        String clientIP = IpContext.getCurrentContext().getIp();
        if (StringUtils.isBlank(clientIP)) {
            clientIP = MainStationUtil.getLocalIpAddress();
        }
        String baseSign = String.format(accesstoken + "&" + APP_ID + "&" + authVers + "&" + clientIP + "&" + fields + "&" + format + "&" +
                method + "&" + jsonParaInfo + "&" + signMethod + "&" + timeNow + "&" + token + "&GET:" + uri + "&" + vers);
        String sign = MainStationUtil.ComputeSignature(baseSign, KAYVALUE, accessSecret);

        String result = null;
        try {
            Map<String, String> queryMap = new HashMap<String, String>();
            queryMap.put("accesstoken", accesstoken);
            queryMap.put("appid", APP_ID);
            queryMap.put("authVers", authVers);
            queryMap.put("clientIP", clientIP);
            queryMap.put("fields", fields);
            queryMap.put("format", format);
            queryMap.put("method", method);
            queryMap.put("paraInfo", paraInfo);
            queryMap.put("signmethod", signMethod);
            queryMap.put("timestamp", String.valueOf(timeNow));
            queryMap.put("token", token);
            queryMap.put("vers", vers);
            queryMap.put("sign", sign);
            String url = MainStationUtil.keyValueJoin(uri + "?", queryMap, "&");

            result = MainStationUtil.loadJSON(url);
        } catch (Exception ex) {
            // 捕获未知异常
            logger.error("请求主站接口: " + method + ",参数: " + jsonParaInfo + ", 错误信息: " + result, ex);
        }

        //region 请求记录 记录错误日志   抛出BusinessException
        JSONObject mainObjStr = JSONObject.fromObject(result);
        String status = "";
        String feedBackResult = "";
        if (mainObjStr != null) {
            status = mainObjStr.get("Status").toString();//主站响应信息状态 非0 都为不成功   0为成功
            feedBackResult = mainObjStr.get("FeedBackResult").toString();
        } else {
            logger.error("请求主站接口: " + method + ",参数: " + jsonParaInfo + ", 错误信息: " + result);
            try {
                throw new BusinessException("请求主站接口响应结果为空");
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        if (!status.equals("0")) {
            logger.error("请求主站接口: " + method + ",参数: " + jsonParaInfo + ", 错误信息: " + feedBackResult);
            try {
                throw new BusinessException("主站请求错误信息" + feedBackResult);
            } catch (BusinessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
