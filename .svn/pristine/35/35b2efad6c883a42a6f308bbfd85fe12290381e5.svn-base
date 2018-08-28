package com.wzitech.gamegold.facade.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.context.CurrentLogContext;
import com.wzitech.gamegold.common.context.CurrentUserContext;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.expection.UnknowException;
import com.wzitech.gamegold.common.expection.UserNotLoginException;
import com.wzitech.gamegold.facade.backend.contant.WebServerContants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ExceptionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 2645380772641480245L;

	private final Log logger = LogFactory.getLog(getClass());

	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 异常拦截器初始化方法
	 *
	 * @see com.deppon.foss.framework.server.web.interceptor.AbstractInterceptor#init()
	 *      init
	 * @since:
	 */
	@Override
	public void init() {
	}

	/**
	 * 异常拦截器的主要方法
	 * 
	 * @see com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
	 *      intercept
	 * @param invocation
	 * @return
	 * @throws Exception
	 * @since:
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		try {
			return invocation.invoke();
		} catch (UserNotLoginException e) {
			//logger.error(e.getMessage(), e);
			return parseClientLoginResult(e, invocation);
		} catch (Throwable e) {
			writeErrorMessageLog(invocation, e);
			SystemException iex = convert(e);
			return parseClientErrorResult(iex, invocation);
		} finally{
			CurrentUserContext.clean();
			CurrentLogContext.clean();
		}
	}
	
	SystemException convert(Throwable target) {
        if (SystemException.class.isAssignableFrom(target.getClass())) {
            return (SystemException) target;
        }
        return new UnknowException(target.toString(),target);
    }

	/*
	 * 转换错误信息为指定的result
	 */
	String parseClientErrorResult(SystemException iex,
			ActionInvocation invocation) {
		ServletActionContext.getRequest();
		final String localizationMsg = ResponseCodes.getResponseByCode(iex.getErrorCode()).getMessage();
		final String clientContentType = getClientContentType();
		Class<?> clazz = invocation.getAction().getClass();
		String methodName = invocation.getProxy().getMethod();
		Method method = ReflectionUtils.findMethod(clazz, methodName);
		// 如果在Action方法上加入JSON的注解，那么就可以在应用这个方法的时候，把错误的结果信息转换为json
		if (WebServerContants.JSON_CONTENT_TYPE.equalsIgnoreCase(clientContentType)
				|| AnnotationUtils.isAnnotationDeclaredLocally(ExceptionToJSON.class,
						clazz)
				|| (method != null && method.isAnnotationPresent(ExceptionToJSON.class))) {
			setJsonResultErrorMessage(localizationMsg, iex);
			return WebServerContants.JSON_ERROR_RESULT;
		} else {
			setJspResultErrorMessage(localizationMsg, iex);
			return WebServerContants.JSP_ERROR_RESULT;
		}
	}

	/*
	 * 转换登录的错误信息为指定的result
	 */
	String parseClientLoginResult(UserNotLoginException iex,
			ActionInvocation invocation) {
		final String localizationMsg = iex.getErrorMsg();
		final String clientContentType = getClientContentType();
		Class<?> clazz = invocation.getAction().getClass();
		String methodName = invocation.getProxy().getMethod();
		Method method = ReflectionUtils.findMethod(clazz, methodName);
		// 如果在Action方法上加入JSON的注解，那么就可以在应用这个方法的时候，把错误的结果信息转换为json
		if (WebServerContants.JSON_CONTENT_TYPE.equalsIgnoreCase(clientContentType)
				|| AnnotationUtils.isAnnotationDeclaredLocally(ExceptionToJSON.class,
						clazz)
				|| (method != null && method.isAnnotationPresent(ExceptionToJSON.class))) {
			setJsonResultErrorMessage(localizationMsg,iex);
			return WebServerContants.JSON_LOGIN_RESULT;
		} else {
			setJspResultErrorMessage(localizationMsg,iex);
			return WebServerContants.JSP_LOGIN_RESULT;
		}
	}

	/*
	 * 获得客户端的ContentType
	 */
	String getClientContentType() {
		String clientContentType = ServletActionContext.getRequest().getHeader(
				"content-type");
		if (clientContentType != null) {
			int iSemicolonIdx = clientContentType.indexOf(';');
			if (iSemicolonIdx != -1) {
				clientContentType = clientContentType.substring(0,iSemicolonIdx);
			}
		}
		return clientContentType;
	}

	/*
	 * 设置错误信息，并以JSON格式把错误信息放入request中
	 */
	void setJsonResultErrorMessage(String msg, Exception iex){
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuffer sb = new StringBuffer();
		this.printStackTraceAsCause(sb, iex);
		Map<String, Object> errorMap = new HashMap<String,Object>();
		errorMap.put("success", false);
		errorMap.put("message", msg);
		errorMap.put("isException", true);
		errorMap.put("stackTrace", sb.toString());
		request.setAttribute("ERROR", errorMap);
	}

	/*
	 * 设置错误信息，以页面转向的形式，把页面返回到之前的页面，并把之前的页面路径（referer，ERROR）放到request中
	 */
	void setJspResultErrorMessage(String msg, Exception iex){
		HttpServletRequest request = ServletActionContext.getRequest();
		StringBuffer sb = new StringBuffer();
		this.printStackTraceAsCause(sb, iex);
		request.setAttribute("referer", request.getHeader("Referer"));
		request.setAttribute("ERROR", msg);
		request.setAttribute("stackTrace", sb.toString());
	}

	/*
     * 打印错误信息
     */
	void printStackTraceAsCause(StringBuffer s,
			Throwable iex) {
		s.append(iex.toString());
        StackTraceElement[] trace = iex.getStackTrace();
        for (int i=0; i < trace.length; i++) {
            s.append("\tat ").append(trace[i]);
        }
		Throwable ourCause = iex.getCause();
		if (ourCause != null) {
			this.printStackTraceAsCause(s, ourCause);
		}
	}

	void writeErrorMessageLog(ActionInvocation invocation, Throwable e) {
		StringBuilder s = new StringBuilder();
		String clazzName = invocation.getAction().getClass().getSimpleName();
		String methodName = invocation.getProxy().getMethod();
		Map<String, Object> paramMap = invocation.getInvocationContext().getParameters();
		String params = null;
		try {
			params = objectMapper.writeValueAsString(paramMap);
		} catch (IOException ex) {
			logger.warn("将参数转换成JSON字符串出错了", e);
		}
		String msg = String.format("[异常]Action:%s,Method:%s,Params:%s,Message:%s", clazzName, methodName, params, e.getMessage());
		logger.error(msg, e);
	}

}
