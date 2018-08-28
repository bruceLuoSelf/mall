package com.wzitech.gamegold.common.userinfo;

import com.wzitech.gamegold.common.userinfo.entity.UserInfo;

import java.io.IOException;

/**
 * 获取5173用户信息
 * @author yemq
 */
public interface IUserInfoService {

    /**
     * 获取用户信息
     * @param uid
     * @return
     */
    UserInfo getUserInfo(String uid) throws IOException;
}
