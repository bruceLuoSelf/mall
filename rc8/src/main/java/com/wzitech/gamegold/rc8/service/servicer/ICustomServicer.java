/**
 *
 */
package com.wzitech.gamegold.rc8.service.servicer;

import com.wzitech.gamegold.rc8.dto.Response;
import com.wzitech.gamegold.rc8.service.servicer.dto.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yemq
 */
public interface ICustomServicer {
    /**
     * 登录
     *
     * @param loginRequest
     * @param request
     * @return
     */
    Response login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse servletResponse);
}
