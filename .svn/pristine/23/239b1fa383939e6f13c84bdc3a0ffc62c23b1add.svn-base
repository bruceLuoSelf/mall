package com.wzitech.gamegold.common.expection;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;


public final class UnknowException extends SystemException {
    
    private static final long serialVersionUID = -7547624267762545457L;
    
    public static final String ERROR_CODE = ResponseCodes.UnKnownError.getCode();
    
    public static final String MESSAGE = ResponseCodes.UnKnownError.getMessage();
    
    public UnknowException() {
    	super(ERROR_CODE, MESSAGE);
		super.setErrorMsg(MESSAGE);
    }
    public UnknowException(String message, Throwable cause) {
    	super(ERROR_CODE, cause);
		super.setErrorMsg(message);
        this.setStackTrace(cause.getStackTrace());
    }
   
}
