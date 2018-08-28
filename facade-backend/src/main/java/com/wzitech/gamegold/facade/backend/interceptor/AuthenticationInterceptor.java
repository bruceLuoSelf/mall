package com.wzitech.gamegold.facade.backend.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.expection.UserNotLoginException;
import com.wzitech.gamegold.common.utils.CookieHelper;
import com.wzitech.gamegold.usermgmt.business.ISessionManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
public class AuthenticationInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = -5665511978967345874L;

    @Autowired
    ISessionManager userSessionMger;

    /**
     * 性能监控与权限控件拦截器初始化方法
     *
     * @since:
     * @see com.deppon.foss.framework.server.web.interceptor.AbstractInterceptor#init()
     * init
     */
    @Override
    public void init() {
    }

    /**
     * 性能监控与权限控制拦截器主方法，实现性能监控与权限控制
     *
     * @param invocation
     * @return
     * @throws Exception
     * @since:
     * @see com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
     * intercept
     */
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        Object target = invocation.getAction();
        String methodName = invocation.getProxy().getMethod();
        Method method = ReflectionUtils.findMethod(target.getClass(), methodName);
//        String authkey = null;
        if (!method.isAnnotationPresent(NoCheckRequired.class)) {
            Cookie cookie = CookieHelper.getCookie(request);
            if (cookie == null) {
                throw new UserNotLoginException();
            }
            String token = cookie.getValue();
//            String cookie = null;
//            Cookie[] cookies = request.getCookies();
//
//            if (null != cookies && cookies.length > 0) {
//                for (Cookie acookie : cookies) {
////					if (StringUtils.equals(acookie.getName(), ServicesContants.BACK_SERVICE_REQUEST_COOKIE)) {
////						cookie = acookie.getValue();
////						break;
////					}
//                }
//            }

//			authkey =(String)request.getSession().getAttribute(ServicesContants.SERVICE_REQUEST_AUTHKEY);
            if (StringUtils.isEmpty(token)) {
                throw new UserNotLoginException();
            } else {
                // 查找authkey对应用户
                UserInfoEO loginUser = userSessionMger.getUserByAuthkey(token);
                if (null == loginUser) {
                    CurrentUserContext.clean();
                    throw new UserNotLoginException();
                } else {
                    // 附加用户信息至当前线程
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
        }
        return invocation.invoke();
    }

}
