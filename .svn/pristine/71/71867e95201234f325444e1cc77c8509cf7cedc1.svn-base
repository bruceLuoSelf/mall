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
package com.wzitech.gamegold.gamegold.app.interceptor;

import com.wzitech.chaos.framework.server.common.CommonServiceResponse;
import com.wzitech.chaos.framework.server.common.IServiceResponse;
import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.constants.ServicesContants;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
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

    private void response(Message message, HttpServletResponse response, ResponseCodes responseCodes) {
        try {
            // 返回Json类型的错误信息
            IServiceResponse jsonResp = new CommonServiceResponse(
                    responseCodes.getCode(), responseCodes.getMessage());
            response.getOutputStream().write(JsonMapper.nonEmptyMapper().toJson(jsonResp).getBytes("utf-8"));
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
