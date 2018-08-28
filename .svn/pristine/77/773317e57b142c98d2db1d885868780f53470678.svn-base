package com.wzitech.gamegold.rc8.service.servicer.impl;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.rc8.service.order.dto.LoginResponse;
import com.wzitech.gamegold.rc8.service.servicer.INewCustomServicer;
import com.wzitech.gamegold.rc8.service.servicer.dto.LoginRequest;
import com.wzitech.gamegold.rc8.utils.DESHelper;
import com.wzitech.gamegold.usermgmt.business.IAuthentication;
import com.wzitech.gamegold.usermgmt.business.ISessionManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

/**
 * Created by 340032 on 2018/3/6.
 */
@Service("NewCustomServicer")
@Path("Newuser")
@Produces({"application/xml;charset=UTF-8", "application/json;charset=UTF-8"})
@Consumes("application/json;charset=UTF-8")
public class NewCustomServicerImpl extends AbstractBaseService implements INewCustomServicer{
    @Autowired
    IAuthentication authentication;

    @Autowired
    IUserInfoManager userInfoManager;

    @Autowired
    ISessionManager sessionManager;

    @Value("${RC.5173.key}")
    private String RCKey = "";

    @Path("login")
    @GET
    @Override
    public LoginResponse login(@QueryParam("") LoginRequest loginRequest,
                               @Context HttpServletRequest servletRequest,
                               @Context HttpServletResponse servletResponse) {
        // 初始化返回数据
        LoginResponse response = new LoginResponse();
        response.setMsg("失败");
        response.setStatus(false);

        try {
            // 校验参数加密
            // 解密出用户密码
            String name = loginRequest.getName();//DESHelper.decrypt(loginRequest.getName(), encryptKey);
            String decPwd = DESHelper.decrypt(loginRequest.getPwd(), RCKey);

            // 校验MD5
            String toEncrypt = EncryptHelper.md5(
                    String.format("%s_%s_%s_%s", name, decPwd, loginRequest.getVersion(), RCKey)
            );

            if (!StringUtils.equals(toEncrypt, loginRequest.getSign())) {
                response.setMsg(response.getMsg() + " (签名不一致)");
                return response;
            }

            // 根据账号查找用户信息
            UserInfoEO userInfo = userInfoManager.findUserByAccount(StringUtils.lowerCase(name));
            if (null == userInfo) {
                response.setMsg(response.getMsg() + " (没有该用户)");
                return response;
            }

            // 该用户是否被禁用
            if (userInfo.getIsDeleted() != null && userInfo.getIsDeleted() == true) {
                response.setMsg(response.getMsg() + " (该用户已被禁用)");
                return response;
            }

            if (!authentication.authenticate(userInfo.getLoginAccount(), userInfo.getPassword(), decPwd)) {
                // 如果用户登录名密码不匹配设置密码错误
                response.setMsg(response.getMsg() + " (用户名或密码错误)");
                return response;
            } else {
                // 创建会话Session
                int timeout = 60 * 60 * 12;
                //String authkey = sessionManager.createSession(userInfo.getId().toString(), timeout);
                String authkey = userInfo.getId().toString();
                servletRequest.getSession().setAttribute(ServicesContants.SERVICE_REQUEST_AUTHKEY, authkey);
                // 创建一个cookie
                Cookie cookie = new Cookie(ServicesContants.SERVICE_REQUEST_AUTHKEY, authkey);
                cookie.setPath("/");
                cookie.setMaxAge(timeout);
                servletResponse.addCookie(cookie);
            }

            // 登录成功
            response.setMsg("成功");
            response.setStatus(true);
            response.setHxAppAccount(userInfo.getHxAppAccount());
            response.setHxAppPwd(userInfo.getHxAppPwd());
            response.setYxAccount(userInfo.getYxAccount());
            response.setYxPwd(userInfo.getYxPwd());
            response.setQq(userInfo.getQq());
            response.setQqPwd(userInfo.getQqPwd());
        } catch (SystemException ex) {
            // 捕获系统异常
            response.setMsg(ex.getArgs()[0]);
            logger.error("查询参数：{}", loginRequest);
            logger.error("登录发生异常", ex);
        } catch (Exception ex) {
            // 捕获未知异常
            response.setMsg("系统发生未知异常");
            logger.error("查询参数：{}", loginRequest);
            logger.error("登录发生异常", ex);
        }

        return response;
    }
}
