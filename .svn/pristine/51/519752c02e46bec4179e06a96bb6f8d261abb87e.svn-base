package com.wzitech.gamegold.common.expection;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;

/**
 * Created by 340082 on 2017/5/31.
 * ZW_C_JB_00004 jiyx 用于跑出异常进行机器收货部分完单操作
 */
public class OrderToCompletePartException extends SystemException {

    public static final String ERROR_CODE = ResponseCodes.OrderToCompletePart.getCode();

    public static final String MESSAGE = ResponseCodes.OrderToCompletePart.getMessage();

    private static final long serialVersionUID = -1068009761840755432L;

    public OrderToCompletePartException() {
        super(ERROR_CODE, MESSAGE);
        super.setErrorMsg(MESSAGE);
    }

    public OrderToCompletePartException(String message, Throwable cause) {
        super(ERROR_CODE, cause);
        super.setErrorMsg(message);
        this.setStackTrace(cause.getStackTrace());
    }
}
