/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *
 *	模	块：		AuthenticationInterceptor
 *	包	名：		com.wzitech.gamegold.facade.frontend.interceptor
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-13
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-13 下午5:59:36
 *
 ************************************************************************************/
package com.wzitech.gamegold.rc8.interceptor;

import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.common.utils.JaxbMapper;
import com.wzitech.gamegold.rc8.dto.Response;
import com.wzitech.gamegold.rc8.utils.DESHelper;
import com.wzitech.gamegold.usermgmt.business.IAuthentication;
import com.wzitech.gamegold.usermgmt.business.ISessionManager;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yemq
 */
public class AuthenticationInterceptor extends AbstractPhaseInterceptor<Message> {
    /**
     * 日志输出
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Autowired
    ISessionManager userSessionMger;

    @Autowired
    IUserInfoManager useInfoManager;

    @Value("${RC.5173.key}")
    private String RCKey = "";
    @Autowired
    IUserInfoManager userInfoManager;

    @Autowired
    IAuthentication authentication;

    /**
     * 对于列表中Url不做authkey检查
     */
    private List<String> ignoreAuthUrlsList;

    public AuthenticationInterceptor() {
        super(Phase.RECEIVE);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
     */
    @Override
    public void handleMessage(Message message) throws Fault {
        // 获取当前的http请求
        HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
        // 获取返回值
        HttpServletResponse response = (HttpServletResponse) message
                .getExchange().getInMessage()
                .get(AbstractHTTPDestination.HTTP_RESPONSE);

        // 获取当前请求的路径
        String path = (String) message
                .get(Message.PATH_INFO);
        logger.info("当前拦截的URL为 {}", path);

        if (null != ignoreAuthUrlsList && !ignoreAuthUrlsList.isEmpty()) {
            // 如果当前访问路径在ignoreUrlsList中则跳过检查（注意Url为小写）
            for (String ignoreUrl : ignoreAuthUrlsList) {
                if (path.toLowerCase().contains(ignoreUrl.toLowerCase())) {
                    logger.info("当前请求URL {}在IgnoreUrls列表中,跳过权限检查.", path);
                    CurrentUserContext.clean();
                    return;
                }
            }
        }

        //对于 登入账号 密码 认证 进行校验
        String name=request.getParameter("name");
        String pwd=request.getParameter("pwd");
        String token=request.getParameter("token");
        String version=request.getParameter("version");
        try {
            //判断新老版本
            if (StringUtils.isNotBlank(token)){

                String decPwd = DESHelper.decrypt(pwd, RCKey);
                // 校验MD5
                String toEncrypt = EncryptHelper.md5(
                        String.format("%s_%s_%s", name, decPwd, RCKey)
                );

                if (!StringUtils.equals(toEncrypt, token)) {
                    this.response(message, response, ResponseCodes.InvalidSign);
                    return;
                }

                // 根据账号查找用户信息
                UserInfoEO userInfo = userInfoManager.findUserByAccount(StringUtils.lowerCase(name));
                if (null == userInfo) {
                    this.response(message, response, ResponseCodes.InvalidAuthkey);
                    return;
                }
                if (!authentication.authenticate(userInfo.getLoginAccount(), userInfo.getPassword(), decPwd)) {
                    // 如果用户登录名密码不匹配设置密码错误
                    this.response(message, response, ResponseCodes.WrongPassword);
                    return;
                }
                CurrentUserContext.setUser(userInfo);
            }else {

                // 获取authkey
                String authkey = (String) request.getSession().getAttribute(ServicesContants.SERVICE_REQUEST_AUTHKEY);
                logger.info("当前拦截请求session的authkey为 {}", authkey);
        /*if (StringUtils.isEmpty(authkey)) {
            Cookie[] cookies = request.getCookies();
            logger.info("cookies:{}", request.getHeader("cookie"));
            if (null != cookies && cookies.length > 0) {
                for (Cookie acookie : cookies) {
                    if (StringUtils.equals(acookie.getName(), ServicesContants.SERVICE_REQUEST_AUTHKEY)) {
                        authkey = acookie.getValue();
                        break;
                    }
                }
            }
        }
        if (StringUtils.isEmpty(authkey)) {
            authkey = request.getHeader(ServicesContants.SERVICE_REQUEST_AUTHKEY);
        }
        if (StringUtils.isEmpty(authkey)) {
            authkey = request.getParameter(ServicesContants.SERVICE_REQUEST_AUTHKEY);
        }

        logger.info("当前拦截请求的authkey为 {}", authkey);*/

                UserInfoEO loginUser = null;
                if (authkey != null) {
                    loginUser = useInfoManager.findUserByUid(authkey);
                }
                // 查找authkey对应用户
                //UserInfoEO loginUser = userSessionMger.getUserByAuthkey(authkey);
                if (null == loginUser) {
                    logger.info("请求中包含的authkey {} 没有找到对应的用户信息", authkey);
                    CurrentUserContext.clean();
                    this.response(message, response, ResponseCodes.InvalidAuthkey);
                    return;
                } else {
                    // 附加用户信息至当前线程
                    logger.info("附加用户信息 {} 到当前请求User Session.", loginUser);
                    CurrentUserContext.setUser(loginUser);

                    // 设置IP信息
                    // 获取用户真实IP地址（配置在nginx）
                    String ip = request.getHeader("X-Real-IP");
                    if (StringUtils.isBlank(ip)) {
                        ip = request.getRemoteAddr();
                    }
                    CurrentIpContext.setIp(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.response(message, response, ResponseCodes.WrongPassword);
            return;
        }



    }

    private void response(Message message, HttpServletResponse response, ResponseCodes responseCodes) {
        try {
            Response result = new Response();
            result.setStatus(false);
            result.setMsg(responseCodes.getCode() + "/" + responseCodes.getMessage());
            String xml = JaxbMapper.toXml(result, "UTF-8");

            response.setContentType("application/xml;charset=utf-8");
            response.getOutputStream().write(xml.getBytes("utf-8"));
            response.getOutputStream().flush();

            // 中断此次请求
            message.getInterceptorChain().abort();
        } catch (Exception e) {
            logger.error("AuthenticationInterceptor返回响应时发生异常:{}", e);
        }
    }

    /**
     * @return the ignoreAuthUrlsList
     */
    public List<String> getIgnoreAuthUrlsList() {
        return ignoreAuthUrlsList;
    }

    /**
     * @param ignoreAuthUrlsList the ignoreAuthUrlsList to set
     */
    public void setIgnoreAuthUrlsList(List<String> ignoreAuthUrlsList) {
        this.ignoreAuthUrlsList = ignoreAuthUrlsList;
    }

}
