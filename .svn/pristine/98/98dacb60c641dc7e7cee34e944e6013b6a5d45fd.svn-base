package com.wzitech.gamegold.common.expection;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.RefundReason;
import com.wzitech.gamegold.common.enums.ResponseCodes;

/**
 * Created by 339931 on 2017/3/20.
 */
public class NotEnoughRepertoryException extends SystemException {


    public static final Integer ERROR_CODE = RefundReason.NotEnoughCapacity.getCode();

    public static final String MESSAGE = RefundReason.NotEnoughCapacity.getName();

    public NotEnoughRepertoryException() {
        super(ERROR_CODE.toString(), MESSAGE);
        super.setErrorMsg(MESSAGE);
    }

    public NotEnoughRepertoryException(String errorCode, Throwable t) {
        super(ERROR_CODE.toString(), t);
        super.setErrorMsg(errorCode);
        this.setStackTrace(t.getStackTrace());
    }
}
