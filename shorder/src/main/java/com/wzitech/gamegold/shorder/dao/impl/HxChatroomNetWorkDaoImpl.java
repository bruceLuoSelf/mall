package com.wzitech.gamegold.shorder.dao.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import com.wzitech.gamegold.shorder.dao.IHuanXinRedisShorderDao;
import com.wzitech.gamegold.shorder.dao.IHxChatroomNetWorkDao;
import com.wzitech.gamegold.shorder.dao.IOrderPushRedisDao;
import com.wzitech.gamegold.shorder.utils.SSLClient;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/2/19.
 */
@Repository
public class HxChatroomNetWorkDaoImpl extends AbstractBusinessObject implements IHxChatroomNetWorkDao {


    @Value("${huanxin.user.url}")
    private String huanXinUserUrl;
    @Value("${huanxin.token.url}")
    private String huanXinTokenUrl;
    @Value("${huanxin.clientId}")
    private String clientId;
    @Value("${huanxin.clientSecret}")
    private String clientSecret;
    @Value("${huanxin.5173.url}")
    private String huanXinUrl;

    public static final String SELLER = "seller";

    public static final String BUYER = "buyer";

    @Autowired
    private IHuanXinRedisShorderDao huanXinRedisDao;

    @Autowired
    private IOrderPushRedisDao orderPushRedisDao;

    /**
     * 删除群组
     *
     * @param chatroomId
     */
    @Async
    public void deleteChatroom(String chatroomId) {
        InputStream content = null;
        String responseStr = null;
        try {
            HttpClient httpClient = new SSLClient();
            String huanXinToken = getHuanXinToken();
            HttpDelete httpDel = new HttpDelete(huanXinUrl + "/chatgroups/" + chatroomId);
            //http删除提交 添加头信息
            httpDel.addHeader("Content-Type", "application/json");
            httpDel.addHeader("Authorization", "Bearer " + huanXinToken);
            HttpResponse httpResponse = httpClient.execute(httpDel);
            //获取状态信息
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            //根据服务器的状态码进行判断当前服务器的状态
            if (statusCode == 404) {
                orderPushRedisDao.saveHxChatRoomId(chatroomId);
                logger.warn("群组不存在,或已经删除");
                return;
            } else if (statusCode == 401) {
                orderPushRedisDao.saveHxChatRoomId(chatroomId);
                logger.warn("未授权[无token、token错误、token过期]");
                return;
            }
            content = httpResponse.getEntity().getContent();
            //获取服务器的返回信息
            responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
        } catch (Exception e) {
            orderPushRedisDao.saveHxChatRoomId(chatroomId);
            logger.warn("删除群组时出现未知错误", e);
            throw new SystemException("删除群组时出现未知错误", e);
        }finally {
            if (content!=null){
                try {
                    content.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            //进行json字符串的解析
            String data = JSONObject.fromObject(responseStr).getString("data");
            if (!JSONObject.fromObject(data).getBoolean("success")) {
                orderPushRedisDao.saveHxChatRoomId(chatroomId);
                logger.warn("删除群组环信失败");
                return;
            }
        } catch (JSONException e) {
            orderPushRedisDao.saveHxChatRoomId(chatroomId);
            logger.warn("删除群组的json解析出错", e);
        }
    }

    public String getHuanXinToken() {
        String huanXinToken = huanXinRedisDao.getHuanXinToken();
//        String huanXinToken =null;
        Long hxTokenTimeout = getHXTokenTimeout();
        //redis里无token数据或者token即将过期。重新获取
        if (StringUtils.isBlank(huanXinToken) || hxTokenTimeout == null || hxTokenTimeout - System.currentTimeMillis() / 1000 < 50) {
            huanXinToken = httpsGetToken();
            setHuanXinToken(huanXinToken);
        }
        return huanXinToken;
    }

    public void setHuanXinToken(String token) {
        huanXinRedisDao.setHuanXinToken(token);
        //重新设置token的同时更新过期时间
        Long tokenExpiresIn = httpsGetTokenExpiresIn();
        setHXTokenTimeout(tokenExpiresIn);
    }

    private void setHXTokenTimeout(Long expiresIn) {
        long timeout = expiresIn + System.currentTimeMillis() / 1000;
        huanXinRedisDao.setHXTokenTimeout(timeout);
    }

    private Long httpsGetTokenExpiresIn() {
        Long expiresIn = null;
        try {
            HttpResponse httpResponse = tokenRequest();
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObject = JSONObject.fromObject(strResult);
            expiresIn = jsonObject.getLong("expires_in");
        } catch (Exception e) {
            logger.warn("获取环信token失效时间出错", e);
            throw new SystemException("获取环信token失效时间出错", e);
        }
        return expiresIn;
    }

    private HttpResponse tokenRequest() {
        HttpClient httpClient = null;
        HttpResponse httpResponse = null;
        try {
            httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(huanXinTokenUrl);
            httpPost.addHeader("Content-Type", "application/json");
            // 接收参数json列表
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("grant_type", "client_credentials");
            jsonParam.put("client_id", clientId);
            jsonParam.put("client_secret", clientSecret);
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
        } catch (Exception e) {
            throw new SystemException("获取环信token请求出错", e);
        }
        return httpResponse;
    }

    public Long getHXTokenTimeout() {
        return huanXinRedisDao.getHuaxnXinTokenTimeout();
    }

    private String httpsGetToken() {
        String accessToken = null;
        try {
            HttpResponse httpResponse = tokenRequest();
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObject = JSONObject.fromObject(strResult);
            accessToken = jsonObject.getString("access_token");
        } catch (Exception e) {
            logger.warn("获取环信token时出错", e);
            throw new SystemException("获取环信token时出错", e);
        }
        return accessToken;
    }

    public void sendHxSystemMessage(String userHxId,String msg,String userType) {
        this.sendHxSystemMessage(userHxId, msg, null,userType);
    }


    //发送系统消息
    @Async
    public void sendHxSystemMessage(String roomId, String msg, String id,String userType) {
        InputStream content = null;
        try {
            //创建http请求
            HttpClient httpClient = new SSLClient();
            String huanXinToken = getHuanXinToken();
            HttpPost httpPost = new HttpPost(huanXinUrl + "/messages");
            //添加头信息
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + huanXinToken);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("target_type", "chatgroups");     //聊天室名称，此属性为必须的
            jsonParam.put("target", "['" + roomId + "']"); //聊天室描述，此属性为必须的
            jsonParam.put("msg", "{'type':'txt','msg':'" + msg + "'}");
            jsonParam.put("from", "系统消息");
            //判断id是否未null
            if (id == null) {
                jsonParam.put("ext", "{'userType':'"+userType+"System'}");
            } else {
                jsonParam.put("ext", "{'userType':'"+userType+"System','id':'" + id + "'}");
            }
            //string实体类处理
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            //返回http响应
            HttpResponse httpResponse = httpClient.execute(httpPost);
            content = httpResponse.getEntity().getContent();
            String responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            logger.info("发送系统消息sendHxSystemMessage：{}", responseStr);
        } catch (Exception e) {
            logger.warn("发送系统消息sendHxSystemMessage：{}", e);
        } finally {
            try {
                if (content != null) {
                    //关流
                    content.close();
                }
            } catch (Exception e) {
                logger.warn("发送系统消息sendHxSystemMessage：{}", e);
            }
        }
    }
}
