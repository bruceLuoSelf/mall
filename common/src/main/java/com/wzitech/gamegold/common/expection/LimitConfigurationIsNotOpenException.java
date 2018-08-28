package com.wzitech.gamegold.common.expection;

import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.gamegold.common.enums.ResponseCodes;

/**
 * Created by jhlcitadmin on 2017/3/20.
 */
public class LimitConfigurationIsNotOpenException extends SystemException {


    public static final String ERROR_CODE = ResponseCodes.LimitConfigurationIsNotOpen
            .getCode();

    public static final String MESSAGE = ResponseCodes.LimitConfigurationIsNotOpen
            .getMessage();

    public LimitConfigurationIsNotOpenException() {
        super(ERROR_CODE, MESSAGE);
        super.setErrorMsg(MESSAGE);
    }

    public LimitConfigurationIsNotOpenException(String errorCode, Throwable t) {
        super(ERROR_CODE, t);
        super.setErrorMsg(errorCode);
        this.setStackTrace(t.getStackTrace());
    }
}
