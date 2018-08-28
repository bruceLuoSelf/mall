/************************************************************************************
 *  Copyright 2014 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		GetPasspodServlet
 *	包	名：		com.wzitech.gamegold.facade.service.order
 *	项目名称：	gamegold-facade
 *	作	者：		SunChengfei
 *	创建时间：	2014-3-11
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-3-11 下午3:05:45
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.facade.service.order;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.wzitech.chaos.framework.server.common.utils.JsonMapper;
import com.wzitech.gamegold.common.utils.EncryptHelper;
import com.wzitech.gamegold.facade.service.order.dto.PasspodResponse;
import com.wzitech.gamegold.facade.utils.DESHelper;
import com.wzitech.gamegold.order.business.IOrderConfigManager;
import com.wzitech.gamegold.order.entity.ConfigResultInfoEO;
import com.wzitech.gamegold.repository.entity.RepositoryInfo;

/**
 * 获取密保卡
 * @author SunChengfei
 *
 */
public class GetPasspodServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	 /**
     * 日志记录器
     */
    protected final Logger logger = LoggerFactory.getLogger(GetPasspodServlet.class);

    @Value("${encrypt.5173.key}")
	private String encryptKey="";
    
    @Value("${file.service.url}")
    private String serviceUrl="";
    
    @Autowired
	private IOrderConfigManager orderConfigQuery;
	
	@Override
    public void init(ServletConfig config) {
        // 为Servlet添加Spring支持
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
		String orderId = request.getParameter("orderid");//订单Id
		String opid = request.getParameter("opid");      //交易员Id
		String oppwd = request.getParameter("oppwd");    //操作员密码
		String sign = request.getParameter("sign");      //签名值
		String gameAccount = request.getParameter("account");
		
		// 错误返回内容
		PasspodResponse passpodResponse = new PasspodResponse();
		passpodResponse.setMsg("操作失败");
		passpodResponse.setStatus(false);
		passpodResponse.setYxbMall("1");
		
		try {
			// 校验MD5
			String decPwd = DESHelper.decrypt(oppwd, encryptKey);
			logger.debug("获取密保卡请求，操作员密码解密后为：",decPwd);
			String toEncrypt = EncryptHelper.md5(String.format("%s_%s_%s_%s", orderId,
					opid, encryptKey, decPwd));
			logger.debug("获取密保卡请求，md5加密后的sign为：",toEncrypt);
			if(StringUtils.equals(toEncrypt, sign)){
				logger.debug("获取密保卡请求，md5校验成功");
				// 根据订单号及卖家信息，查询密保卡
				List<ConfigResultInfoEO> resultInfoEOs = orderConfigQuery.orderConfigList(orderId);
				if(resultInfoEOs != null && resultInfoEOs.size() > 0){
					for (ConfigResultInfoEO configResultInfoEO : resultInfoEOs) {
						RepositoryInfo repositoryInfo = configResultInfoEO.getRepositoryInfo();
						if(StringUtils.equals(repositoryInfo.getGameAccount(), gameAccount)){
							response.sendRedirect(serviceUrl+repositoryInfo.getPasspodUrl());
						}
					}
				}
			}
			logger.debug("MD5校验失败，返回false");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		response.getOutputStream().write(JsonMapper.nonEmptyMapper().toJson(passpodResponse).getBytes("utf-8"));
		response.getOutputStream().flush();
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException{
		this.doGet(request, response);
	}
}
