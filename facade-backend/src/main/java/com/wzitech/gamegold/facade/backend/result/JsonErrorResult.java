package com.wzitech.gamegold.facade.backend.result;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.codehaus.jackson.map.ObjectMapper;

import com.opensymphony.xwork2.ActionInvocation;

public class JsonErrorResult extends AbstractResult {
    
    private static final long serialVersionUID = -6724704054805634357L;
    
	/**
	 * 序列化及缓存类
	 */
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    /**
     * 错误信息
     */
    private static final String ERROR = "ERROR";
    
    /**
     * 把执行过程中存在的错误信息，以JSON的形式写出
     * @see com.deppon.foss.framework.server.web.result.AbstractResult#execute(com.opensymphony.xwork2.ActionInvocation)
     * execute
     * @param invocation
     * @throws Exception
     * @since:
     */
    @Override
    public void execute(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        Object object = request.getAttribute(ERROR);
        if (object != null) {
        	StringWriter writer = new StringWriter();
            MAPPER.writeValue(writer, object);
            StringBuffer sb = writer.getBuffer();
            //MAPPER序列化对象后，自动加上双引号，对象序列化后直接返回前台，需要过滤掉引号
            if(sb.toString().startsWith("\"")){
            	sb.substring(1, sb.length()-1);
            }
            
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.print(sb);
            out.flush();
            out.close();
        }

    }
    
}
