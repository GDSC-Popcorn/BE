package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.ErrorCode;
import com.popcorn.popcorn.common.error.ErrorCodeIfs;
import com.popcorn.popcorn.common.error.UserErrorCode;

public class PopUpNotFoundExceptioin extends PopcornException {
    public static final PopcornException EXCEPTION = new PopUpNotFoundExceptioin();


    public PopUpNotFoundExceptioin() {
        super(ErrorCode.POPUP_NOT_FOUND);
    }
}
