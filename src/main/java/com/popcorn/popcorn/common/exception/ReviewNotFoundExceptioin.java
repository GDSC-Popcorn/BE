package com.popcorn.popcorn.common.exception;

import com.popcorn.popcorn.common.error.ErrorCode;

public class ReviewNotFoundExceptioin extends PopcornException {
    public static final PopcornException EXCEPTION = new ReviewNotFoundExceptioin();


    public ReviewNotFoundExceptioin() {

        super(ErrorCode.REVIEW_NOT_FOUND);
    }
}
