package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.ErrorCode;

public class InvalidTokenException extends PopcornException{

    public static final PopcornException EXCEPTION = new InvalidTokenException();


    private InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
