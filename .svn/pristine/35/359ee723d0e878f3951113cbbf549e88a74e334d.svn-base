package com.wzitech.gamegold.common.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 汪俊杰 on 2018/5/2.
 */
public class InitContent implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SysContent.setRequest((HttpServletRequest) request);
        SysContent.setResponse((HttpServletResponse) response);
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
