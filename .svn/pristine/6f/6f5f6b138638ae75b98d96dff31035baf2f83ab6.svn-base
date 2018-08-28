package com.wzitech.gamegold.common.expection;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;


public class UserNotLoginException extends SystemException {

	private static final long serialVersionUID = -8447576671797891073L;

	public static final String ERROR_CODE = ResponseCodes.NotExistedUser.getCode();

	public static final String MESSAGE = ResponseCodes.NotExistedUser.getMessage();

	public UserNotLoginException() {
		super(ERROR_CODE, MESSAGE);
		super.setErrorMsg(MESSAGE);
	}

	public UserNotLoginException(String message, Throwable cause) {
		super(ERROR_CODE, cause);
		super.setErrorMsg(message);
	}
}
