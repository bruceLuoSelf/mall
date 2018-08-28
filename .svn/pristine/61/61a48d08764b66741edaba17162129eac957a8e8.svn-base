package com.wzitech.gamegold.facade.service.servicer.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.wzitech.gamegold.common.context.CurrentIpContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wzitech.chaos.framework.server.common.AbstractBaseService;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.facade.service.servicer.ICustomServicer;
import com.wzitech.gamegold.facade.service.servicer.dto.LoginRequest;
import com.wzitech.gamegold.facade.service.servicer.dto.LoginResponse;
import com.wzitech.gamegold.facade.utils.DESHelper;
import com.wzitech.gamegold.usermgmt.business.IAuthentication;
import com.wzitech.gamegold.usermgmt.business.IUserInfoManager;
import com.wzitech.gamegold.usermgmt.entity.UserInfoEO;

@Service("UserService")
@Path("user")
@Produces("application/xml;charset=gb2312")
@Consumes("application/json;charset=UTF-8")
public class CustomServicerImpl extends AbstractBaseService implements ICustomServicer {
	@Autowired
	IAuthentication authentication;
	
	@Autowired
	IUserInfoManager userInfoManager;
	
	@Value("${encrypt.5173.key}")
	private String encryptKey="";
	
	@Path("login")
	@GET
	@Override
	public LoginResponse login(@QueryParam("") LoginRequest loginRequest, @Context HttpServletRequest request) {
		logger.debug("当前登录用户信息:{}", loginRequest);
		// 初始化返回数据
		LoginResponse response = new LoginResponse();
		response.setMsg("验证失败");
		response.setStatus(false);
		response.setYxbMall("1");
		
		try {
			// 校验参数加密
			// 解密出用户密码
            String decPwd = DESHelper.decrypt(loginRequest.getUserPwd(), encryptKey);
            String requestAccount = DESHelper.decrypt(loginRequest.getLoginId(), encryptKey);
            
            // 校验MD5
            String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s", requestAccount, decPwd,
					loginRequest.getPasspod(), encryptKey, decPwd));
            
			if(!StringUtils.equals(toEncrypt, loginRequest.getSign())){
				return response;
			}
			
            // 根据账号查找用户信息
			UserInfoEO userInfo = userInfoManager.findUserByAccount(StringUtils.lowerCase(requestAccount));
			if (null == userInfo) {
				return response;
			}
			
			// 该用户是否被禁用
			if(userInfo.getIsDeleted() != null && userInfo.getIsDeleted() == true){
				return response;
			}
			
			if(!authentication.authenticate(userInfo.getLoginAccount(),
					userInfo.getPassword(), decPwd)) {
				// 如果用户登录名密码不匹配设置密码错误
				return response;
			} else {
                CurrentUserContext.setUser(userInfo);

                // 设置IP信息
                // 获取用户真实IP地址（配置在nginx）
                String ip = request.getHeader("X-Real-IP");
                if (StringUtils.isBlank(ip)) {
                    ip = request.getRemoteAddr();
                }
                CurrentIpContext.setIp(ip);
            }
			
			// 登录成功
			response.setMsg("验证成功");
			response.setStatus(true);
		} catch (SystemException ex) {
			// 捕获系统异常
			logger.error("登录发生异常:{}", ex);
		} catch (Exception ex) {
			// 捕获未知异常
			logger.error("登录发生异常:{}", ex);
		}
		logger.debug("当前登录用户登录响应信息:{}", response);
		return response;
	}
}
