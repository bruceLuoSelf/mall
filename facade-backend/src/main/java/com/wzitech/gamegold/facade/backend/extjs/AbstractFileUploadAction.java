package com.wzitech.gamegold.facade.backend.extjs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;

public class AbstractFileUploadAction extends ActionSupport{

	private static final long serialVersionUID = 2724502818082777178L;
	
	protected static final Logger logger = LoggerFactory.getLogger(AbstractFileUploadAction.class);

	/**
     * ext调用错误标识
     */
    private boolean success = false;
    
    /**
     * ext调用异常标识 
     * 业务异常该标识为false，只有在exceptionInterceptor中出现的异常才会是true
     */
    private boolean isException = true;

	/**
	 * 用于返回执行的结果提示信息
	 */
    protected String message;
	
    /**
     * 分页最大记录数
     */
    protected int limit;
    
    /**
     * 排序信息列表
     */
    protected String sort;
    
    /**
     * 分页开始记录数
     */
    protected int start;
    
    protected int page;
    
    /**
     * 总记录数
     */
    protected Long totalCount;
    
    private boolean afterValidate = false;
    
    protected ObjectMapper objectMapper = new ObjectMapper();
    
	public List<Sort> sortList() {
		List<Sort> sortList = new ArrayList<Sort>();
		try {
			if(sort==null){
				return sortList;
			}
			Sort[] sortArray = objectMapper.readValue(this.sort, Sort[].class);
			return Arrays.asList(sortArray);
		} catch (Exception e) {
			logger.debug("排序JSON串转换异常", e);
			return sortList;
		}
	}

	public void setSort(String sort) {
		this.sort = sort;
	}


	public void setPage(int page) {
		this.page = page;
	}

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public String getMessage() {
		return message;
	}

    public boolean isSuccess() {
    	this.afterValidate();
		return success;
	}
    
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getIsException() {
		this.afterValidate();
		return isException;
	}

	public void setIsException(boolean isException) {
		this.isException = isException;
	}

	private void afterValidate(){
		if(afterValidate){
			return;
		}
		Collection<String> collection = this.getActionErrors();
		Map<String, List<String>> fieldErrorMap = this.getFieldErrors();
		StringBuffer sb = new StringBuffer();
		Set<String> messageSet = new HashSet<String>();
		boolean flag = false;
		if(collection!=null && collection.size()>0){
			messageSet.addAll(collection);
			flag = true;
		}
		if(fieldErrorMap!=null && fieldErrorMap.size()>0){
			for(List<String> fieldError : fieldErrorMap.values()){
				messageSet.addAll(fieldError);
			}
			flag = true;
		}
		if(flag){
			if(message!=null){
				messageSet.add(message);
			}
			int i = 1;
			for(String msg : messageSet){
				sb.append(msg);
				if(messageSet.size()>i++){
					sb.append(",");
				}
			}
			message = sb.toString();
			success = false;
			isException = false;
		}
		afterValidate = true;
	}
	
    /**
	 * <p>返回"success"</p> 
	 * @date 2012-8-30 上午10:57:09
	 * @return String SUCCESS
	 */
	protected String returnSuccess() {
	    return returnSuccess(null);
	}
    
	/**
	 * 通过传入消息类型，得到消息，并返回"success"
	 * @date 2012-8-30 上午11:20:52
	 * @param messageType 消息的KEY
	 * @return String SUCCESS
	 */
	protected String returnSuccess(String message) {
		this.setSuccess(true);
        this.setIsException(false);
        this.message = message;
        return "success";
	}
	
	/**
	 * <p>传入消息类型，得到信息后返回"error"</p> 
	 * @author Ningyu
	 * @date 2012-8-30 上午11:27:49
	 * @param messageType 消息的KEY
	 * @return String ERROR
	 */
	protected String returnError(String message) {
		this.setSuccess(false);
        this.setIsException(false);
        this.message = message;
        return "error";
	}
	
	/**
	 * 通过传入业务异常，从异常中得到异常类型，得到异常类信息
	 * @date 2012-8-30 上午11:28:35
	 * @param SystemException 业务异常
	 * @return String ERROR
	 */
	protected String returnError(SystemException e) {
		this.setSuccess(false);
		this.setIsException(false);
		if(e.getArgs()!=null){
			this.message = String.format(ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage(), (Object[])e.getArgs());			
		}else{
			this.message = ResponseCodes.getResponseByCode(e.getErrorCode()).getMessage();
		}
		return "error";
	}
}
