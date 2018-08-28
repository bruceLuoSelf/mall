package com.wzitech.gamegold.common.utils;

import com.wzitech.gamegold.common.constants.ServicesContants;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 汪俊杰 on 2018/7/8.
 */
public final class CookieHelper {

    public static String saveCookie(HttpServletRequest request, HttpServletResponse response, String token) {
        Cookie cookie = getCookie(request);
        if (cookie != null) {
            // 修改cookie时间戳
            cookie.setValue(token);
        } else {
            // 重新new一个Cookie
            cookie = new Cookie(ServicesContants.BACK_SERVICE_REQUEST_COOKIE, token);
        }
        cookie.setPath("/");// 同一个域名所有url cookie共享
        cookie.setMaxAge(18 * 60 * 60);
        response.addCookie(cookie);
        return token;
    }

    public static void invalidateCookie(HttpServletRequest request, HttpServletResponse response) {
        // 失效掉token的cookie
        Cookie cookie_token = getCookie(request);
        if (cookie_token != null) {
            cookie_token.setMaxAge(0);// 设置为0立即删除
            response.addCookie(cookie_token);
        }
        Cookie cookie_jsession = getCookie(request, CommonConstants.JSESSIONID);
        if (cookie_jsession != null) {
            cookie_jsession.setMaxAge(0);// 设置为0立即删除
            response.addCookie(cookie_jsession);
        }
    }


    public static Cookie getCookie(HttpServletRequest request) {
        return getCookie(request, ServicesContants.BACK_SERVICE_REQUEST_COOKIE);
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                if (name.equals(cookies[i].getName())) {
                    return cookies[i];
                }
            }
        }
        return null;
    }
}
