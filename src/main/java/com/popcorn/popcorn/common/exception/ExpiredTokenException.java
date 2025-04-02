package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.ErrorCode;
import com.popcorn.popcorn.common.error.ErrorCodeIfs;

public class ExpiredTokenException extends PopcornException{
    public static final PopcornException EXCEPTION = new ExpiredTokenException();


    public ExpiredTokenException() {
        super(ErrorCode.TOKEN_EXPIRED);
    }
}
