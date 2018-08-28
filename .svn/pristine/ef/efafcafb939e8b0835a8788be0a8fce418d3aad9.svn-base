package com.wzitech.gamegold.common.expection;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;


/**
 * 
 * 
 ******************************************* 
 * <b style="font-family:微软雅黑"><small>Description:类型不匹配异常</small></b> </br> <b
 * style="font-family:微软雅黑"><small>HISTORY</small></b></br> <b
 * style="font-family:微软雅黑"><small> ID DATE PERSON REASON</small></b><br>
 ******************************************** 
 * <div style="font-family:微软雅黑,font-size:70%"> 1 2011-4-9 steven.cheng 新增
 * </div>
 ******************************************** 
 */
public final class TypeMismatchAccessException extends SystemException {

	private static final long serialVersionUID = -3526762703628318946L;

	public static final String ERROR_CODE = ResponseCodes.TypeMismatchAccessError
			.getCode();

	public static final String MESSAGE = ResponseCodes.TypeMismatchAccessError
			.getMessage();

	public TypeMismatchAccessException() {
		super(ERROR_CODE, MESSAGE);
		super.setErrorMsg(MESSAGE);
	}

	public TypeMismatchAccessException(String message, Throwable cause) {
		super(ERROR_CODE, cause);
		super.setErrorMsg(message);
		this.setStackTrace(cause.getStackTrace());
	}

}
