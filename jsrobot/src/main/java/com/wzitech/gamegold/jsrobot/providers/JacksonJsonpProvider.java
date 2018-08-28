/************************************************************************************
 *  Copyright 2013 WZITech Corporation. All rights reserved.
 *	
 *	模	块：		JacksonJsonpProvider
 *	包	名：		com.wzitech.gamegold.facade.frontend.providers
 *	项目名称：	gamegold-facade-frontend
 *	作	者：		SunChengfei
 *	创建时间：	2014-1-13
 *	描	述：		
 *	更新纪录：	1. SunChengfei 创建于 2014-1-13 下午5:58:57
 * 				
 ************************************************************************************/
package com.wzitech.gamegold.jsrobot.providers;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author SunChengfei
 *
 */
public class JacksonJsonpProvider extends JacksonJsonProvider {
	/**
	 * 注入HttpServletRequest对象
	 */
	@Context HttpServletRequest request; 
	
	/**
	 * URL中JSONP回调函数参数明
	 */
	private final static String jsonpUrlParam = "callback";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider#writeTo(java.lang
	 * .Object, java.lang.Class, java.lang.reflect.Type,
	 * java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType,
	 * javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)
	 */
	@Override
	public void writeTo(Object value, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException {
		// 获取前端请求中的JSONP回调函数名
		String jsonpFunc = this.request.getParameter(jsonpUrlParam);
		
		// 根据JSONP CALLBACK参数判断当前请求是否为JSONP调用
		// 如果是则设置JSONP回调函数名
		if(StringUtils.isNotBlank(jsonpFunc)){
			this.setJSONPFunctionName(jsonpFunc);
		}else {
			this.setJSONPFunctionName(null);// 当不包含callback时必须设置为空，否则在后续请求时仍会是使用JSONP方式
		}
		
		super.writeTo(value, type, genericType, annotations, mediaType,
				httpHeaders, entityStream);
	}
}
