package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.UserErrorCode;

public class InvalidOauthProviderException extends PopcornException {
    public static final PopcornException EXCEPTION = new InvalidOauthProviderException();

    public InvalidOauthProviderException() {
        super(UserErrorCode.OAUTH_PROVIDER_NOT_MATCH);
    }
}
