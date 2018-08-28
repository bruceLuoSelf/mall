package com.wzitech.gamegold.repository.business.impl;

import com.wzitech.chaos.framework.server.common.AbstractBusinessObject;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.enums.ShOpenState;
import com.wzitech.gamegold.common.utils.StreamIOHelper;
import com.wzitech.gamegold.repository.business.IHuanXinManager;
import com.wzitech.gamegold.repository.dao.IHuanXinRedisDao;
import com.wzitech.gamegold.repository.dao.ISellerDBDAO;
import com.wzitech.gamegold.repository.entity.SellerInfo;
import com.wzitech.gamegold.repository.util.SSLClient;
import com.wzitech.gamegold.shorder.business.IHxAccountManager;
import com.wzitech.gamegold.shorder.dao.IDeliveryOrderDao;
import com.wzitech.gamegold.shorder.dao.IHxAccountRedisDao;
import com.wzitech.gamegold.shorder.entity.DeliveryOrder;
import com.wzitech.gamegold.shorder.entity.HxAccount;
import com.wzitech.gamegold.usermgmt.dao.rdb.IUserInfoDBDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 环信聊天
 * 孙杨 on 2017/2/15.
 */
@Component
public class HuanXinManagerImpl extends AbstractBusinessObject implements IHuanXinManager {

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

    @Autowired
    IHuanXinRedisDao huanXinRedisDao;
    @Autowired
    ISellerDBDAO sellerDBDAO;
    //    private final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private IDeliveryOrderDao deliveryOrderDao;

    @Autowired
    IUserInfoDBDAO userInfoDBDAO;

    @Autowired
    private IHxAccountManager hxAccountManager;

    @Autowired
    private IHxAccountRedisDao hxAccountRedisDao;

    /*
    批量初始化环信账号
     */
    public void registerHuanXinUserAll() throws InterruptedException {
        HashMap<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("date", "loginAccount is not null");
        int totalCount = userInfoDBDAO.countByUserMap(queryMap);
        if (totalCount == 0) {
            return;
        }
        int pageSize = 100;//每次读取100笔
        int totalPage = 0;
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }

        //递归获取所有需要分仓的帐号
        synAllPurchaseListByPag(0, pageSize, totalPage, queryMap, "ID", true);

    }

    private void synAllPurchaseListByPag(int index, int pageSize, int totalPage, Map<String, Object> queryMap, String id, Boolean t) throws InterruptedException {
        if (index < totalPage) {
            GenericPage<UserInfoEO> genericPage = userInfoDBDAO.selectUserByMap(queryMap, pageSize, index * pageSize, "ID", true);
            List<UserInfoEO> pageData = genericPage.getData();
            if (pageData != null && pageData.size() > 0) {
                for (UserInfoEO userInfoEO : pageData) {
                    registerHuanXinLoginAccount(userInfoEO);
                    Thread.sleep(300L);
                }
            }
            index++;
            synAllPurchaseListByPag(index, pageSize, totalPage, queryMap, "ID", true);
        }

    }


    /**
     * 根据登陆用户账号来注册
     *
     * @param userInfoEO
     */
    public void registerHuanXinLoginAccount(UserInfoEO userInfoEO) {
        String str = userInfoEO.getLoginAccount().replace("@", "gold");
        String hxAccount = "jb_hxaccount" + str;
        String hxPassword = "jb_hxpassword" + str;
        HttpResponse httpResponse = null;
        InputStream content = null;
        try {
            String huanXinToken = getHuanXinToken();
            HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(huanXinUserUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + huanXinToken);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", hxAccount);
            jsonParam.put("password", hxPassword);
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            content = httpResponse.getEntity().getContent();
            String responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            logger.info("初始化UserInfo环信用户registerHuanXinLoginAccount：{}", responseStr);
            //更新用户信息，保存环信账号密码
            userInfoEO.setHxAccount(hxAccount);
            userInfoEO.setHxPwd(hxPassword);
            userInfoDBDAO.update(userInfoEO);
        } catch (Exception e) {
            throw new SystemException("注册环信时出错", e);
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    logger.warn("释放流时出错", e);
                    throw new SystemException("释放流时出错", e);
                }
            }
        }
    }


    public String registerHuanXin(Long id) {
        SellerInfo seller = sellerDBDAO.selectById(id);
        if (seller == null) {
            throw new SystemException("用户为空");
        }
        if (isHaveRegisterHX(seller)) {
            return null;
        }
        String hxAccount = "jb_hxaccount" + seller.getUid();
        String hxPassword = "jb_hxpassword" + seller.getUid();
        HttpResponse httpResponse = null;
        InputStream content = null;
        try {
            String huanXinToken = getHuanXinToken();
            HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(huanXinUserUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + huanXinToken);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", hxAccount);
            jsonParam.put("password", hxPassword);
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            content = httpResponse.getEntity().getContent();
            String responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            logger.info("初始化Seller环信用户registerHuanXin：{}", responseStr);
            //更新用户信息，保存环信账号密码
            seller.setHxAccount(hxAccount);
            seller.setHxPwd(hxPassword);
            sellerDBDAO.update(seller);
            return hxAccount;
        } catch (Exception e) {
            throw new SystemException("注册环信时出错", e);
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    logger.warn("释放流时出错", e);
                    throw new SystemException("释放流时出错", e);
                }
            }
        }
    }

    //注册出货商的环信账号跟密码
    public DeliveryOrder registerHuanXinByDeliveryOrder(DeliveryOrder d) {
        String hxAccount = "jb_hxaccount" + d.getSellerUid();
        String hxPassword = "jb_hxpassword" + d.getSellerUid();
        HttpResponse httpResponse = null;
        InputStream content = null;
        try {
            String huanXinToken = getHuanXinToken();
            HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(huanXinUserUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + huanXinToken);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", hxAccount);
            jsonParam.put("password", hxPassword);
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            content = httpResponse.getEntity().getContent();
            String responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            logger.info("创建环信用户registerHuanXinByDeliveryOrder：{}", responseStr);
        } catch (Exception e) {
            logger.warn("注册环信时出错", e);
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    logger.warn("释放流时出错", e);
//                    throw new SystemException("释放流时出错", e);
                }
            }
        }
        d.setSellerHxAccount(hxAccount);
        d.setSellerHxPwd(hxPassword);
        return d;
    }

    //注册收货商账号跟密码
    public DeliveryOrder registerHuanXinByDeliveryOrderBuyerHxAndAdminHx(DeliveryOrder d) {
        String hxAccount = "jb_hxaccount" + d.getBuyerUid();
        String hxPassword = "jb_hxpassword" + d.getBuyerUid();
//        HttpResponse httpResponse = null;
        registerNoBy(hxAccount, hxPassword);
        String adminHxacc = "jb_hxaccount_system";
        String adminHxPwd = "jb_hxpassword_system";
        registerNoBy(adminHxacc, adminHxPwd);
        d.setBuyerHxAccount(hxAccount);
        d.setBuyerHxPwd(hxPassword);
        d.setAdminHxAccount(adminHxacc);
        d.setAdminHxPwd(adminHxPwd);
        return d;
    }

    @Async
    private void registerNoBy(String hxAccount, String hxPassword) {
        HttpResponse httpResponse;
        InputStream content = null;
        String responseStr = null;
        try {
            String huanXinToken = getHuanXinToken();
            HttpClient httpClient = new SSLClient();
            HttpPost httpPost = new HttpPost(huanXinUserUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + huanXinToken);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("username", hxAccount);
            jsonParam.put("password", hxPassword);
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpResponse = httpClient.execute(httpPost);
            content = httpResponse.getEntity().getContent();
            responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            logger.info("创建环信用户registerNoBy：{}", responseStr);
        } catch (Exception e) {
            throw new SystemException("注册环信时出错", e);
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    logger.warn("释放流时出错", e);
                    throw new SystemException("释放流时出错", e);
                }
            }
        }
    }


    public Boolean isHaveRegisterHX(SellerInfo seller) {
        return StringUtils.isNotBlank(seller.getHxAccount()) || StringUtils.isNotBlank(seller.getHxPwd());
    }

    @Override
    public String getHuanXinToken() {
        String huanXinToken = huanXinRedisDao.getHuanXinToken();
        Long hxTokenTimeout = getHXTokenTimeout();
        //redis里无token数据或者token即将过期。重新获取
        if (StringUtils.isBlank(huanXinToken) || hxTokenTimeout == null || hxTokenTimeout - System.currentTimeMillis() / 1000 < 50) {
            huanXinToken = httpsGetToken();
            setHuanXinToken(huanXinToken);
        }
        return huanXinToken;
    }

    @Override
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
            throw new SystemException("获取环信token时出错", e);
        }
        return accessToken;
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

    @Override
    public void setHuanXinToken(String token) {
        huanXinRedisDao.setHuanXinToken(token);
        //重新设置token的同时更新过期时间
        Long tokenExpiresIn = httpsGetTokenExpiresIn();
        setHXTokenTimeout(tokenExpiresIn);
    }

    private Long httpsGetTokenExpiresIn() {
        Long expiresIn = null;
        try {
            HttpResponse httpResponse = tokenRequest();
            String strResult = EntityUtils.toString(httpResponse.getEntity());
            JSONObject jsonObject = JSONObject.fromObject(strResult);
            expiresIn = jsonObject.getLong("expires_in");
        } catch (Exception e) {
            throw new SystemException("获取环信token失效时间出错", e);
        }
        return expiresIn;
    }

    private void setHXTokenTimeout(Long expiresIn) {
        long timeout = expiresIn + System.currentTimeMillis() / 1000;
        huanXinRedisDao.setHXTokenTimeout(timeout);
    }

    public SellerInfo selectHxAccountById(String id) {
        return sellerDBDAO.selectHxAccountById(id);
    }


    /**
     * 创建环信的群组id
     * String orderSellerUid 生成 收货商环信id
     */
    public void createChatroomId(String chHxId, String shHxId, String xtHxId, DeliveryOrder deliveryOrder) {
        InputStream content = null;
        String responseStr = null;
        try {
            if (deliveryOrder == null) {
                throw new Exception("当前订单不存在");
            }
            HttpClient httpClient = new SSLClient();
            String huanXinToken = getHuanXinToken();
            HttpPost httpPost = new HttpPost(huanXinUrl + "/chatgroups");
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Authorization", "Bearer " + huanXinToken);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("groupname", "jb_5173chatgroups" + deliveryOrder.getOrderId());     //聊天室名称，此属性为必须的
            jsonParam.put("desc", deliveryOrder.getOrderId()); //聊天室描述，此属性为必须的
            jsonParam.put("maxusers", 3);  //聊天室成员最大数（包括群主），值为数值类型，默认值200，最大值5000，此属性为可选的
            jsonParam.put("public", false);
            jsonParam.put("approval", true);

            if (xtHxId == null) {
                throw new SystemException("系统环信账号不存在");
            }
            //随机在数据库中user中获取hxaccount（）；
//            if(xtHxId.contains("sh")){
//                xtHxId.replace("sh","SH");
//            }


//            if(userInfoEOs==null){
//                thr
//            }

//            String xtHxAccount="";
//            for(int i = 0 ;i<userInfoEOs.size();i++){
//                if(userInfoEOs.get(i).getHxAccount()!=null){
//                    xtHxAccount=userInfoEOs.get(i).getHxAccount();
//                    break;
//                }
//            }
            List<UserInfoEO> userInfoEOs = userInfoDBDAO.randomQuery();
            //判断是否为null
            if (userInfoEOs == null || userInfoEOs.size() == 0) {
                throw new SystemException("系统环信生成出错，请联系管理员");
            }

            jsonParam.put("owner", userInfoEOs.get(0).getHxAccount());//聊天室的管理员，此属性为必须的
            if (shHxId == null) {
                throw new SystemException("收货商环信账号不存在");
            }
            if (chHxId == null) {
                throw new SystemException("出货商环信账号不存在");
            }
            jsonParam.put("members", "[\"" + shHxId + "\",\"" + chHxId + "\"]"); //聊天室成员，此属性为可选的，但是如果加了此项，数组元素至少一个（注：群主jma1不需要写入到members里面）
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            content = httpResponse.getEntity().getContent();
            responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
            logger.info("创建群组：{}", responseStr);
            String id = null;
            try {
                JSONObject jsonObject = JSONObject.fromObject(responseStr);
                String data = jsonObject.getString("data");
                id = JSONObject.fromObject(data).getString("groupid");
            } catch (Exception e) {
                JSONObject jsonObject = JSONObject.fromObject(responseStr);
                String errorMassage = jsonObject.getString("error_description");
                logger.warn(errorMassage, e);
                throw new SystemException(errorMassage, e);
            }
            //DeliveryOrder deliveryOrder = new DeliveryOrder();
            //deliveryOrder.setOrderId(orderId);
            deliveryOrder.setChatroomId(id);
            deliveryOrderDao.update(deliveryOrder);
        } catch (IOException e) {
            logger.warn("创建环信群组id出错", e);
            //throw new SystemException("创建环信聊天室id出错", e);
        } catch (Exception e) {
            JSONObject jsonObject = JSONObject.fromObject(responseStr);
            String errorMassage = jsonObject.getString("error_description");
            logger.warn(errorMassage, e);
            //throw new SystemException(errorMassage, e);
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    logger.warn("释放流时出错", e);
                    //throw new SystemException("释放流时出错", e);
                }
            }
        }
    }

//    public void deleteChatroom(String chatroomId){
//        InputStream content = null;
//        try {
//            HttpClient httpClient = new SSLClient();
//            String huanXinToken = getHuanXinToken();
//            HttpPost httpPost = new HttpPost(huanXinUrl + "/chatrooms/" + chatroomId);
//            httpPost.addHeader("Content-Type", "application/json");
//            httpPost.addHeader("Authorization", "Bearer " + huanXinToken);
//            HttpResponse httpResponse = httpClient.execute(httpPost);
//            content = httpResponse.getEntity().getContent();
//            String responseStr = StreamIOHelper.inputStreamToStr(content, "utf-8");
//            //JSONObject.fromObject(responseStr).getString("");
//
//        }catch (Exception e){
//            logger.warn("删除聊天室时出错",e);
//            throw new SystemException("删除聊天室时出错", e);
//        }
//    }
//
//    public void deleteChatroomByOrderId(String mainOrderId){
//        // 客户手动结单结束聊天室
//        DeliveryOrder deliveryOrder = deliveryOrderDao.selectByOrderId(mainOrderId);
//        if(deliveryOrder!=null &&deliveryOrder.getChatroomId()!=null)
//        deleteChatroom(deliveryOrder.getChatroomId());
//    }

    public void loopCreateChatroomId(final String chHxId, final String shHxId, final String xtHxId, final DeliveryOrder deliveryOrder) {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i <= 5; i++) {
                    try {
                        createChatroomId(chHxId, shHxId, xtHxId, deliveryOrder);
                        break;
                    } catch (Exception e) {
                        try {
                            Thread.sleep(300L);
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                        continue;
                    }
                }
            }
        }.run();
    }

    /**
     * 开通收货的商家批量生成环信账号和密码
     */
    @Override
    public void registerHuanXinByIdS() throws InterruptedException {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("openShState", ShOpenState.OPEN.getCode());
        int totalCount = sellerDBDAO.countByMap(queryMap);
        if (totalCount == 0) {
            return;
        }
        int pageSize = 100;//每次读取100笔
        int totalPage = 0;
        if (totalCount % pageSize == 0) {
            totalPage = totalCount / pageSize;
        } else {
            totalPage = totalCount / pageSize + 1;
        }

        //递归获取所有需要分仓的帐号
        synAllPurchaseListByPage(0, pageSize, totalPage, queryMap);

    }

    private void synAllPurchaseListByPage(int index, int pageSize, int totalPage, Map<String, Object> queryMap) throws InterruptedException {
        if (index < totalPage) {
            GenericPage<SellerInfo> genericPage = sellerDBDAO.selectOpenSellerByMap(queryMap, pageSize, index * pageSize);
            List<SellerInfo> pageData = genericPage.getData();
            if (pageData != null && pageData.size() > 0) {
                for (SellerInfo sellerInfo : pageData) {
                    registerHuanXin(sellerInfo.getId());
                    Thread.sleep(300L);
                }
            }
            index++;
            synAllPurchaseListByPage(index, pageSize, totalPage, queryMap);
        }

    }

    @Async
    @Override
    public void registerHx(DeliveryOrder deliveryOrder) {
        if (deliveryOrder == null) {
            throw new SystemException(ResponseCodes.NoDeliveryOrder.getCode(),
                    ResponseCodes.NoDeliveryOrder.getMessage());
        }
        int count = 1;
        //补偿4次
        while (count < 5) {
            //根据出货商账号信息查询出货商环信用户信息

            HxAccount account = hxAccountRedisDao.selectBySellerName(deliveryOrder.getSellerAccount());
            if (account == null || org.apache.commons.lang3.StringUtils.isBlank(account.getHxAccounter())) {
                //redis为空再去数据库查
                account = hxAccountManager.selectHxAccountByAccount(deliveryOrder.getSellerAccount());
                //查到了就存redis一份
                if (account != null && org.apache.commons.lang3.StringUtils.isNotBlank(account.getHxAccounter())) {
                    hxAccountRedisDao.saveHxAccount(account);

                    deliveryOrder.setSellerHxAccount(account.getHxAccounter());
                    deliveryOrder.setSellerHxPwd(account.getHxPwd());
                }
            } else {
                deliveryOrder.setSellerHxAccount(account.getHxAccounter());
                deliveryOrder.setSellerHxPwd(account.getHxPwd());
            }

            logger.info("创建订单，出货商环信信息：{}", account);
            registerHuanXin(account, deliveryOrder);
            //account = registerHuanXin(account, deliveryOrder);
            //将收货商跟出货商的环信id账号以及密码插入订单表中
            //根据收货商id注册环信账号
            deliveryOrder = registerHuanXinByDeliveryOrderBuyerHxAndAdminHx(deliveryOrder);
            logger.info("创建订单，订单信息：{}", deliveryOrder);
//                deliveryOrder.setSellerAccount(account.getHxAccounter());
//                deliveryOrder.setSellerHxPwd(account.getHxPwd());
            //向订单表中插入数据
            //deliveryOrderManager.updateOrder(deliveryOrder);
            //收货商的环信id 要获得 收货商是否有环信账号
//             huanXinManager.createChatroomId(h.getHxAccounter(),orderId);
            //创建的聊天室全部在order表中获取
            if (org.apache.commons.lang3.StringUtils.isNotBlank(deliveryOrder.getSellerHxAccount()) && org.apache.commons.lang3.StringUtils.isNotBlank(deliveryOrder.getBuyerHxAccount()) && org.apache.commons.lang3.StringUtils.isNotBlank(deliveryOrder.getAdminHxAccount())) {
                try {
                    loopCreateChatroomId(deliveryOrder.getSellerHxAccount(), deliveryOrder.getBuyerHxAccount(), deliveryOrder.getAdminHxAccount(), deliveryOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            count++;
        }
    }

    public HxAccount registerHuanXin(HxAccount h, DeliveryOrder deliveryOrder) {
//
//        //判断出货商环信用户是否为空
        if (h == null) {
            h = new HxAccount();
            deliveryOrder = registerHuanXinByDeliveryOrder(deliveryOrder);
            h.setId(deliveryOrder.getId());
            h.setCreateTime(new Date());
            h.setHxAccounter(deliveryOrder.getSellerHxAccount());
            h.setHxPwd(deliveryOrder.getSellerHxPwd());
            h.setPurchaseId(deliveryOrder.getSellerUid());
            h.setPurchaseName(deliveryOrder.getSellerAccount());
            hxAccountManager.insert(h);
            hxAccountRedisDao.saveHxAccount(h);
            return h;
        }
        //判断是否有环信id
        if (org.apache.commons.lang3.StringUtils.isBlank(h.getHxAccounter())) {
            DeliveryOrder d = registerHuanXinByDeliveryOrder(deliveryOrder);
            h.setHxAccounter(d.getSellerHxAccount());
            h.setHxPwd(d.getSellerHxPwd());
            hxAccountManager.insertInto(h);
            hxAccountRedisDao.saveHxAccount(h);
            return h;
        }
        return h;
    }
}


