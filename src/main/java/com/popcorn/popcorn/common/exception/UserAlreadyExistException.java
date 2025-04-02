package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.UserErrorCode;

public class UserAlreadyExistException extends PopcornException {
    public static final PopcornException EXCEPTION = new UserAlreadyExistException();

    public UserAlreadyExistException(){
        super(UserErrorCode.USER_ALREADY_EXIST_INFO);
    }
}
