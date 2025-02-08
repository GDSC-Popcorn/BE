package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.UserErrorCode;

public class UserNotFoundException extends PopcornException{
    public static final PopcornException EXCEPTION = new UserNotFoundException();

    public UserNotFoundException(){
        super(UserErrorCode.NOT_FOUND_USER);
    }

}
