/************************************************************************************
 * Copyright 2013 WZITech Corporation. All rights reserved.
 * <p>
 * 模	块：		UserInfoRedisDAOImpl
 * 包	名：		com.wzitech.gamegold.usermgmt.dao.redis.impl
 * 项目名称：	gamegold-usermgmt
 * 作	者：		SunChengfei
 * 创建时间：	2014-2-15
 * 描	述：
 * 更新纪录：	1. SunChengfei 创建于 2014-2-15 下午1:42:07
 ************************************************************************************/
package com.wzitech.gamegold.usermgmt.dao.redis.impl;

import com.wzitech.chaos.framework.server.dataaccess.redis.AbstractRedisDAO;
import com.wzitech.gamegold.common.utils.RedisKeyHelper;
import com.wzitech.gamegold.usermgmt.dao.redis.IUserInfoRedisDAO;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author SunChengfei
 */
@Repository
public class UserInfoRedisDAOImpl extends AbstractRedisDAO<UserInfoEO> implements IUserInfoRedisDAO {
    private static final String COUNT_NUMBER= "gamegold:orderCenter:countNumber";
    private static final String COUNT_NUMBER_WITH_DATE="gamegold:orderCenterWithDate:countNumber";
    @Autowired
    @Qualifier("userRedisTemplate")
    @Override
    public void setTemplate(StringRedisTemplate template) {
        super.template = template;
    }

    ;

    @Override
    public UserInfoEO findUserByUid(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return null;
        }
        // 从redis中获取对应uid的UserInfo
        BoundHashOperations<String, String, String> userOps = template
                .boundHashOps(RedisKeyHelper.uid(uid));

        return userOps.entries().isEmpty() != true ? mapper.fromHash(userOps.entries()) : null;
    }

    @Override
    public UserInfoEO findUserByLoginAccount(String loginAccount) {
        // 先获取loginAccount对应的Uid
        String uid = findUidByLoginAccount(loginAccount);
        if (StringUtils.isEmpty(uid)) {
            return null;
        }

        // 从Redis中获取对应uid的UserInfo
        return this.findUserByUid(uid);
    }

    @Override
    public void saveUser(UserInfoEO userInfo) {
        if (userInfo == null) {
            return;
        }
        // 将userInfo保存至Redis
        BoundHashOperations<String, String, String> userOps = template
                .boundHashOps(RedisKeyHelper.uid(userInfo.getId().toString()));

        if (StringUtils.isNotBlank(userInfo.getId().toString())) {
            userOps.put("id", userInfo.getId().toString());
            // 设置帐号名对应的uid
            valueOps.set(RedisKeyHelper.account(StringUtils.lowerCase(userInfo.getLoginAccount())),
                    userInfo.getId().toString());
        }

        if (userInfo.getUserType() != null) {
            userOps.put("userType", userInfo.getUserType().toString());
        }
        if (StringUtils.isNotEmpty(userInfo.getLoginAccount())) {
            userOps.put("loginAccount", userInfo.getLoginAccount());
        }
        if (StringUtils.isNotEmpty(userInfo.getPassword())) {
            userOps.put("password", userInfo.getPassword());
        }
        if (StringUtils.isNotEmpty(userInfo.getFundsAccount())) {
            userOps.put("fundsAccount", userInfo.getFundsAccount());
        }
        if (userInfo.getFundsAccountId() != null) {
            userOps.put("fundsAccountId", String.valueOf(userInfo.getFundsAccountId()));
        }
        if (StringUtils.isNotEmpty(userInfo.getRealName())) {
            userOps.put("realName", userInfo.getRealName());
        }
        if (StringUtils.isNotEmpty(userInfo.getNickName())) {
            userOps.put("nickName", userInfo.getNickName());
        }
        if (StringUtils.isNotEmpty(userInfo.getSex())) {
            userOps.put("sex", userInfo.getSex());
        }
        if (StringUtils.isNotEmpty(userInfo.getQq())) {
            userOps.put("qq", userInfo.getQq());
        }
        if (StringUtils.isNotEmpty(userInfo.getWeiXin())) {
            userOps.put("weiXin", userInfo.getWeiXin());
        }
        if (StringUtils.isNotEmpty(userInfo.getPhoneNumber())) {
            userOps.put("phoneNumber", userInfo.getPhoneNumber());
        }
        if (StringUtils.isNotEmpty(userInfo.getSign())) {
            userOps.put("sign", userInfo.getSign());
        }
        if (StringUtils.isNotEmpty(userInfo.getAvatarUrl())) {
            userOps.put("avatarUrl", userInfo.getAvatarUrl());
        }
        if (userInfo.getLastUpdateTime() != null) {
            userOps.put("lastUpdateTime", String.valueOf(userInfo.getLastUpdateTime().getTime()));
        }
        if (userInfo.getCreateTime() != null) {
            userOps.put("createTime", String.valueOf(userInfo.getCreateTime().getTime()));
        }
        if (userInfo.getIsDeleted() != null) {
            userOps.put("isDeleted", userInfo.getIsDeleted().toString());
        }
        if (userInfo.getMainAccountId() != null) {
            userOps.put("mainAccountId", userInfo.getMainAccountId().toString());
        }
        if (userInfo.getYy() != null) {
            userOps.put("yy", userInfo.getYy());
        }
        userOps.put("serviceCharge", userInfo.getServiceCharge() + "");
        userOps.put("star", userInfo.getStar() + "");

        //start 环信一对一 by汪俊杰 20170515
        userOps.put("hxAppAccount", userInfo.getHxAppAccount() == null ? "" : userInfo.getHxAppAccount());
        userOps.put("hxAppPwd", userInfo.getHxAppPwd() == null ? "" : userInfo.getHxAppPwd());
        //end
        userOps.put("yxAccount", userInfo.getYxAccount() == null ? "" : userInfo.getYxAccount());
        userOps.put("yxPwd", userInfo.getYxPwd() == null ? "" : userInfo.getYxPwd());
        //userOps.put("communicationTools", userInfo.getCommunicationTools() == null ? "" : userInfo.getCommunicationTools());
        //MTQ添加qq返回值
        userOps.put("qq", userInfo.getQq() == null ? "" : userInfo.getQq());
        userOps.put("qqPwd", userInfo.getQqPwd() == null ? "" : userInfo.getQqPwd());

    }

    @Override
    public void setUserAuthkey(String uid, String authkey) {
        // 删除原先的authkey
        String origAuthkey = valueOps.get(RedisKeyHelper.auth(uid));
        if (StringUtils.isNotEmpty(origAuthkey)) {
            template.delete(RedisKeyHelper.authkey(origAuthkey));
        }

        // 创建authkey
        valueOps.set(RedisKeyHelper.auth(uid), authkey);
        valueOps.set(RedisKeyHelper.authkey(authkey), uid.toString());
    }

    @Override
    public void setUserAuthkey(String uid, String authkey, int timeout) {
        // 删除原先的authkey
        String origAuthkey = valueOps.get(RedisKeyHelper.auth(uid));
        if (StringUtils.isNotEmpty(origAuthkey)) {
            template.delete(RedisKeyHelper.authkey(origAuthkey));
        }

        // 创建authkey
        valueOps.set(RedisKeyHelper.auth(uid), authkey);
        valueOps.set(RedisKeyHelper.authkey(authkey), uid.toString());

        template.expire(RedisKeyHelper.auth(uid), timeout, TimeUnit.SECONDS);
        template.expire(RedisKeyHelper.authkey(authkey), timeout, TimeUnit.SECONDS);
    }

    /*
     * (non-Javadoc)
     * @see com.wzitech.chinabeauty.usermgmt.dao.redis.IUserInfoRedisDAO#removeUserAuthkey(java.lang.String)
     */
    @Override
    public void removeUserAuthkey(String uid) {
        String authkey = valueOps.get(RedisKeyHelper.auth(uid));

        template.delete(RedisKeyHelper.auth(uid));
        template.delete(RedisKeyHelper.authkey(authkey));
    }

    /*
     * (non-Javadoc)
     * @see com.wzitech.chinabeauty.usermgmt.dao.redis.IUserInfoRedisDAO#getUidByAuthkey(java.lang.String)
     */
    @Override
    public String getUidByAuthkey(String authkey) {
        return valueOps.get(RedisKeyHelper.authkey(authkey));
    }

    /**
     * 根据帐号名查找uid
     *
     * @param loginAccount
     * @return
     */
    private String findUidByLoginAccount(String loginAccount) {
        return valueOps.get(RedisKeyHelper.account(StringUtils.lowerCase(loginAccount)));
    }

    /*
     * (non-Javadoc)
     * @see com.wzitech.chinabeauty.usermgmt.dao.redis.IUserInfoRedisDAO#deleteUser(java.lang.String)
     */
    @Override
    public void deleteUser(String uid) {
        // 删除用户信息
        this.template.delete(RedisKeyHelper.uid(uid));

        // 删除用户session
        this.removeUserAuthkey(uid);
    }

    /**
     * 获取所有的客服
     *
     * @return
     */
    @Override
    public List<UserInfoEO> findAllService() {
        return null;
    }

    /**
     * 查询指定游戏担保客服ID列表，待发货订单数量少的客服靠前排序
     *
     * @param gameName
     */
    @Override
    public List<String> findAssureServiceByGame(String gameName) {
        List<String> ids = listOps.range(RedisKeyHelper.assureServiceByGameName(gameName), 0, -1);
        return ids;
    }

    /**
     * 保存担保客服ID，按忙碌程度
     *
     * @param gameName
     * @param users
     */
    @Override
    public void saveAssureService(String gameName, List<UserInfoEO> users) {
        if (!CollectionUtils.isEmpty(users)) {
            for (UserInfoEO user : users)
                listOps.rightPush(RedisKeyHelper.assureServiceByGameName(gameName), String.valueOf(user.getId()));
            // 缓存30秒
            template.expire(RedisKeyHelper.assureServiceByGameName(gameName), 30, TimeUnit.SECONDS);
        }
    }

    /**
     * 清除前台订单页担保客服ID排序
     */
    @Override
    public void removeAssureService() {
        Set<String> keys = template.keys(RedisKeyHelper.assureServiceByGameName("*"));
        if (keys != null && keys.size() > 0) {
            Iterator<String> keysIter = keys.iterator();
            while (keysIter.hasNext()) {
                String key = keysIter.next();
                template.delete(key);
            }
        }
    }

    public String getCount(){
        return valueOps.get(COUNT_NUMBER);
    }


    public void setCount(String orderId, Long begin,Long end){
        valueOps.set(COUNT_NUMBER+begin+"-"+end,orderId);
    }

    public String getCountWithDate(){
        return valueOps.get(COUNT_NUMBER_WITH_DATE);
    }

    public void incrementWithDate(int listSize){
        valueOps.increment(COUNT_NUMBER_WITH_DATE,listSize);
    }

    @Override
    public boolean lock(long timeout, TimeUnit timeUnit, String lockKey) {
        if (StringUtils.isNotBlank(lockKey)) {
            Boolean result = valueOps.setIfAbsent(RedisKeyHelper.jobLock(lockKey), lockKey);
            if (result != null && result.booleanValue()) {
                template.expire(RedisKeyHelper.jobLock(lockKey), timeout, timeUnit);
            }
            return result;
        }
        return false;
    }

    @Override
    public boolean unlock(String unlockKey) {
        if (StringUtils.isNotBlank(unlockKey)) {
            template.delete(RedisKeyHelper.jobLock(unlockKey));
            return true;
        }
        return false;
    }
}
